package com.wolfbe.distributedid.client;

import com.alibaba.fastjson.JSON;
import com.wolfbe.distributedid.exception.RemotingConnectException;
import com.wolfbe.distributedid.exception.RemotingTimeoutException;
import com.wolfbe.distributedid.exception.RemotingTooMuchRequestException;
import com.wolfbe.distributedid.sdks.SdkProto;
import com.wolfbe.distributedid.util.GlobalConfig;
import com.wolfbe.distributedid.util.NettyUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Andy
 */
public abstract class BaseClient implements Client {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected ConcurrentMap<Integer,ChannelFuture> asyncResponse;
    protected NioEventLoopGroup workGroup;
    protected ChannelFuture cf;
    protected Bootstrap b;
    protected int port;

    public void init() {
        asyncResponse = new ConcurrentHashMap<>();
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
        if (workGroup != null) {
            workGroup.shutdownGracefully();
        }
    }

    @Override
    public SdkProto invokeSync(SdkProto sdkProto,long timeoutMillis) throws RemotingConnectException,
            RemotingTimeoutException {
        Channel channel = cf.channel();
        if(channel.isActive()) {
            int rqid = sdkProto.getRqid();
            cf.channel().writeAndFlush(sdkProto).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {

                    }
                }
            });

        }else {
            NettyUtil.closeChannel(channel);
            throw new RemotingConnectException(NettyUtil.parseRemoteAddr(channel));
        }
        return null;
    }

    @Override
    public void invokeAsync(SdkProto sdkProto,long timeoutMillis, final InvokeCallback invokeCallback)
            throws RemotingConnectException, RemotingTooMuchRequestException,RemotingTimeoutException {
        Channel channel = cf.channel();
        if(channel.isActive()) {

            logger.info("send msg sdkproto : {}", sdkProto.toString());
            cf.channel().writeAndFlush(sdkProto).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        if (invokeCallback != null) {
                            invokeCallback.operationComplete(channelFuture);
                        }
                    }
                }
            });
        }else {
            NettyUtil.closeChannel(channel);
            throw new RemotingConnectException(NettyUtil.parseRemoteAddr(channel));
        }
    }

    @Override
    public void invokeOneWay(SdkProto sdkProto,long timeoutMillis) throws RemotingConnectException,RemotingTooMuchRequestException,
            RemotingTimeoutException{
        Channel channel = cf.channel();
        if(channel.isActive()) {
//            SdkProto sdkProto = new SdkProto(rqid.incrementAndGet(), 0);
            logger.info("send msg sdkproto : {}", sdkProto.toString());
            cf.channel().writeAndFlush(sdkProto);
        }else {
            NettyUtil.closeChannel(channel);
            throw new RemotingConnectException(NettyUtil.parseRemoteAddr(channel));
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
