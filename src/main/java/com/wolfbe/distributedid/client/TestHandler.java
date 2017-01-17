package com.wolfbe.distributedid.client;

import com.wolfbe.distributedid.core.SnowFlake;
import com.wolfbe.distributedid.sdks.SdkServer;
import com.wolfbe.distributedid.util.GlobalConfig;
import com.wolfbe.distributedid.util.NettyUtil;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 由于请求的协议很简单，只有5字符：getid
 * 解码时使用FixedLengthFrameDecoder即可
 *
 * @author Andy
 */
public class TestHandler extends ChannelDuplexHandler {
    private static final Logger logger = LoggerFactory.getLogger(TestHandler.class);

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        TestServer.map.put(ctx.channel(), NettyUtil.parseRemoteAddr(ctx.channel()));
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        logger.error("SdkServerHandler channel [{}] error and will be closed", NettyUtil.parseRemoteAddr(channel),cause);
        NettyUtil.closeChannel(channel);
    }
}
