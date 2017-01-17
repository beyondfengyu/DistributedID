package com.wolfbe.distributedid.client;

import com.wolfbe.distributedid.util.NettyUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * 由于请求的协议很简单，只有5字符：getid
 * 解码时使用FixedLengthFrameDecoder即可
 *
 * @author Andy
 */
public class TestHandler2 extends MessageToByteEncoder<Long> {
    private static final Logger logger = LoggerFactory.getLogger(TestHandler2.class);


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        logger.error("SdkServerHandler channel [{}] error and will be closed", NettyUtil.parseRemoteAddr(channel),cause);
        NettyUtil.closeChannel(channel);
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Long o, ByteBuf byteBuf) throws Exception {
        byteBuf.writeLong(o);
        logger.info("testhanler2 msg is: {}", o);
    }
}
