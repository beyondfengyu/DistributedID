package com.wolfbe.distributedid.client;

import com.wolfbe.distributedid.core.BaseClient;
import com.wolfbe.distributedid.util.GlobalConfig;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * @author laochunyu
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
                        pipeline.addLast(new StringEncoder(Charset.defaultCharset()))
                                .addLast(new StringDecoder(Charset.defaultCharset()))
//                                .addLast("LongHandler", new LongHandler())
                                .addLast("SdkHandler", new SdkHandler());
//                                .addLast(new FixedLengthFrameDecoder(8))
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
