package com.wolfbe.distributedid.sdk;

import com.wolfbe.distributedid.util.GlobalConfig;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 由于请求的协议很简单，只有5字符：getid
 * 解码时使用FixedLengthFrameDecoder即可
 *
 * @author Andy
 */
public class SdkServerHandler extends SimpleChannelInboundHandler {
    private static final Logger logger = LoggerFactory.getLogger(SdkServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("SdkServerHandler msg is: {}", msg);
        if (GlobalConfig.SDK_REQUEST.equals(msg)) {

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
