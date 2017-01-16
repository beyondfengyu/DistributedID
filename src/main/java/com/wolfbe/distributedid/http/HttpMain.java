package com.wolfbe.distributedid.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andy
 */
public class HttpMain {
    private static final Logger logger = LoggerFactory.getLogger(HttpMain.class);

    public static void main(String[] args) {
        final HttpServer server = new HttpServer();
        server.init();
        server.start();
        // 注册进程钩子，在JVM进程关闭前释放资源
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run(){
                server.shutdown();
                logger.warn(">>>>>>>>>> jvm shutdown");
                System.exit(0);
            }
        });

    }
}
