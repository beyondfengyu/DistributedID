package com.wolfbe.distributedid.http;

import com.wolfbe.netty.util.DateTimeUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  自定义的处理器，目前支持三种请求：
 *  getTime: 获取服务器当前时间；
 *  clientInfo: 获取请求客户端的User-Agent信息
 *  其它： 返回404状态，并且提示404信息
 * @author Andy
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request)
            throws Exception {
        String uri = request.uri();
        logger.info(">>>>>> request uri is: {}", uri);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        switch (uri) {
            case "/getTime": //获取当前的时间
                String curTime = DateTimeUtil.getCurrentTime();
                StringBuffer buffer = new StringBuffer();
                buffer.append("<div>当前时间：").append(curTime).append("</div>");
                response.content().writeBytes(buffer.toString().getBytes());
                break;
            case "/clientInfo": //获取客户端的信息
                String client = request.headers().get("User-Agent");
                response.content().writeBytes(client.getBytes());
                break;
            default:// 404，请求找不到
                String info = "<div style='align:center;font-size:30px;'>404, Can not found the page</div>";
                response.content().writeBytes(info.getBytes());
                response.setStatus(HttpResponseStatus.NOT_FOUND);
                break;
        }
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("connection error", cause);
        ctx.channel().close().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                logger.info("connection close complete!!");
            }
        });
    }
}
