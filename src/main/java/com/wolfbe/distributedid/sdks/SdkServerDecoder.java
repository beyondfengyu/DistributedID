package com.wolfbe.distributedid.sdks;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wolfbe.distributedid.core.SnowFlake;
import com.wolfbe.distributedid.util.GlobalConfig;
import com.wolfbe.distributedid.util.NettyUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Andy
 */
public class SdkServerDecoder extends FixedLengthFrameDecoder {
    private static final Logger logger = LoggerFactory.getLogger(SdkServerDecoder.class);

    public SdkServerDecoder(int frameLength) {
        super(frameLength);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf buf = null;
        try {
            buf = (ByteBuf) super.decode(ctx, in);
            if (buf == null) {
                return null;
            }
            return new SdkProto(buf.readInt(), buf.readLong());
        } catch (Exception e) {
            logger.error("decode exception, " + NettyUtil.parseRemoteAddr(ctx.channel()), e);
            NettyUtil.closeChannel(ctx.channel());
        }finally {
            if (buf != null) {
                buf.release();
            }
        }
        return null;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        logger.error("SdkServerDecoder channel [{}] error and will be closed", NettyUtil.parseRemoteAddr(channel),cause);
        NettyUtil.closeChannel(channel);
    }
}
