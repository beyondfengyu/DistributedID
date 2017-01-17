package com.wolfbe.distributedid.client;

import com.wolfbe.distributedid.sdks.SdkProto;
import com.wolfbe.distributedid.util.NettyUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andy
 */
public class SdkClientHandler extends SimpleChannelInboundHandler<SdkProto> {

    private static final Logger logger = LoggerFactory.getLogger(SdkClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, SdkProto s) throws Exception {
        logger.info("SdkProto is : {}", s.toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("SdkHandler error", cause);
        NettyUtil.closeChannel(ctx.channel());
    }


}
