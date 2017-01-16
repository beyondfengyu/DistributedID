package com.wolfbe.distributedid.sdk;

import com.wolfbe.distributedid.http.HttpServerHandler;
import com.wolfbe.distributedid.server.BaseServer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * @author Andy
 */
public class SdkServer extends BaseServer {

    @Override
    public void init() {
        super.init();
        b.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .localAddress(new InetSocketAddress(8099))
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(defLoopGroup,
                                new FixedLengthFrameDecoder(5),
                                new StringDecoder(Charset.forName("utf-8")),
                                new SdkServerHandler(5)     //自定义处理器
                        );
                    }
                });

    }
    @Override
    public void start() {

    }
}
