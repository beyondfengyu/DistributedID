package com.wolfbe.distributedid.client;

import com.wolfbe.distributedid.sdks.SdkServerDecoder;
import com.wolfbe.distributedid.sdks.SdkServerEncoder;
import com.wolfbe.distributedid.util.GlobalConfig;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author Andy
 */
public class SdkClient extends BaseClient{


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
            InetSocketAddress address = (InetSocketAddress) cf.channel().remoteAddress();
            logger.info("SdkClient start success, host is {}, port is {}", address.getHostName(),
                    address.getPort());
        } catch (InterruptedException e) {
            logger.error("SdkClient start error", e);
        }
    }
}
