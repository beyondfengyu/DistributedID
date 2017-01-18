package com.wolfbe.distributedid.client;


import com.wolfbe.distributedid.sdks.SdkServerDecoder;
import com.wolfbe.distributedid.sdks.SdkServerEncoder;
import com.wolfbe.distributedid.util.GlobalConfig;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Andy
 */
public class SdkClient extends BaseClient{

    protected AtomicInteger rqid = new AtomicInteger(0);

    @Override
    public void start() {
        b.group(workGroup)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("SdkServerDecoder", new SdkServerDecoder(12))
                                .addLast("SdkServerEncoder", new SdkServerEncoder())
                                .addLast("SdkClientHandler", new SdkClientHandler());
                    }
                });
        try {
            cf = b.connect(GlobalConfig.DEFAULT_HOST, GlobalConfig.SDKS_PORT).sync();
            cf.channel().closeFuture().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    logger.info("client channel close");
                    shutdown();
                }
            });

            InetSocketAddress address = (InetSocketAddress) cf.channel().remoteAddress();
            logger.info("SdkClient start success, host is {}, port is {}", address.getHostName(),
                    address.getPort());
        } catch (InterruptedException e) {
            logger.error("SdkClient start error", e);
            shutdown(); //关闭并释放资源
        }
    }


}
