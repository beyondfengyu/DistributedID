package com.wolfbe.distributedid.core;

import com.wolfbe.distributedid.util.GlobalConfig;
import com.wolfbe.distributedid.util.NettyUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Andy
 */
public abstract class BaseClient implements Client {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected NioEventLoopGroup workGroup;
    protected ChannelFuture cf;
    protected Bootstrap b;
    protected int port;

    public void init(){
        workGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 10, new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "WORK_" + index.incrementAndGet());
            }
        });

        b = new Bootstrap();
    }

    @Override
    public void shutdown() {
        workGroup.shutdownGracefully();
    }


    @Override
    public void invokeSync(long timeoutMillis) {
        logger.info("invokeSync {}",cf.channel().isActive());

        cf.channel().writeAndFlush(GlobalConfig.SDKS_REQUEST).addListener(new ChannelFutureListener(){

            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
//                    logger.info("operationComplete ,remoteAddress is {}", NettyUtil.parseRemoteAddr(channelFuture.channel()));
                }
            }
        });
    }

    @Override
    public void invokeAsync(long timeoutMillis, InvokeCallback invokeCallback) {

    }

    @Override
    public void invokeOneWay(long timeoutMillis) {

    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
