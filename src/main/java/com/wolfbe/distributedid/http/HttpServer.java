package com.wolfbe.distributedid.http;

import com.wolfbe.netty.BaseServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Http服务器，使用Netty中的Http协议栈，
 * 实现中支持多条请求路径，对于不存在的请求路径返回404状态码
 * 如：http://localhost:8099/getTime
 * @author Andy
 * @date 2017/1/12
 */
public class HttpServer extends BaseServer {

    @Override
    public void init() {
        defLoopGroup = new DefaultEventLoopGroup(8, new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "DEFAULTEVENTLOOPGROUP_" + index.incrementAndGet());
            }
        });
        bossGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors(), new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "BOSS_" + index.incrementAndGet());
            }
        });
        workGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 10, new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "WORK_" + index.incrementAndGet());
            }
        });

        b = new ServerBootstrap();
        b.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .localAddress(new InetSocketAddress(8099))
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(defLoopGroup,
                                new HttpRequestDecoder(),   //请求解码器
                                new HttpObjectAggregator(65536),//将多个消息转换成单一的消息对象
                                new HttpResponseEncoder(),  // 响应编码器
                                new ChunkedWriteHandler(),  //支持异步发送大的码流，一般用于发送文件流
                                new HttpServerHandler()     //自定义处理器
                        );
                    }
                });
    }

    @Override
    public void start() {
        try {
            cf = b.bind().sync();
            InetSocketAddress addr = (InetSocketAddress) cf.channel().localAddress();
            logger.info("HttpServer start success, port is:{}", addr.getPort());
        } catch (InterruptedException e) {
            logger.error("HttpServer start fail,", e);
        }
    }

    @Override
    public void shutdown() {
        if (defLoopGroup != null) {
            defLoopGroup.shutdownGracefully();
        }
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }


}
