package com.wolfbe.distributedid.client;

import com.wolfbe.distributedid.util.NettyUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author laochunyu
 */
public class LongHandler extends ByteToMessageDecoder{

    private static final Logger logger = LoggerFactory.getLogger(LongHandler.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("SdkHandler error", cause);
        NettyUtil.closeChannel(ctx.channel());
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        long len = byteBuf.readableBytes();
        logger.info("len is: {}", len);
        if (len < 8) {
            return;
        }
        long id = byteBuf.readLong();
        logger.info("getid result is: {}", id);
        list.add(id);
    }
}
