package com.wolfbe.distributedid;

import com.wolfbe.distributedid.core.SnowFlake;
import com.wolfbe.distributedid.http.HttpServer;
import com.wolfbe.distributedid.sdks.SdkServer;
import com.wolfbe.distributedid.util.GlobalConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 两个服务器进程最好用同一个SnowFlake实例，
 * 部署在分布式环境时，SnowFlake的datacenterId和machineId作为联合键必须全局唯一,
 * 否则多个节点的服务可能产生相同的ID
 * @author Andy
 */
public class ServerStartup {
    private static final Logger logger = LoggerFactory.getLogger(ServerStartup.class);

    public static void main(String[] args) {
        long datacenterId = GlobalConfig.DATACENTER_ID;
        long machineId = GlobalConfig.MACHINES_SIGN;

        if (args != null && args.length == 2) {
            datacenterId = Long.valueOf(args[0]);
            machineId = Long.valueOf(args[1]);
        }else{
            System.out.println(">>>>>You don't appoint the datacenterId and machineId argement,will use default value");
        }

        final SnowFlake snowFlake = new SnowFlake(datacenterId, machineId);

        // 启动Http服务器
        final HttpServer httpServer = new HttpServer(snowFlake);
        httpServer.init();
        httpServer.start();

        // 启动Sdk服务器
        final SdkServer sdkServer = new SdkServer(snowFlake);
        sdkServer.init();
        sdkServer.start();

        System.out.println(String.format(">>>>>Server start success, SnowFlake datacenterId is %d, machineId is %d",
                datacenterId,
                machineId
        ));

        // TODO 雪花算法数据中心标识、机器标识、服务IP、服务端口上报到配置中心

        // 注册进程钩子，在JVM进程关闭前释放资源
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                httpServer.shutdown();
                logger.warn(">>>>>>>>>> httpServer shutdown");
                sdkServer.shutdown();
                logger.warn(">>>>>>>>>> sdkServer shutdown");
                System.exit(0);
            }
        });

    }
}
