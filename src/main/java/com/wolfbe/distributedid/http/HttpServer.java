package com.wolfbe.distributedid.http;

import com.wolfbe.distributedid.core.SnowFlake;
import com.wolfbe.distributedid.core.BaseServer;
import com.wolfbe.distributedid.util.GlobalConfig;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.net.InetSocketAddress;

/**
 * Http服务器，使用Netty中的Http协议栈，
 * 实现中支持多条请求路径，对于不存在的请求路径返回404状态码
 * 如：http://localhost:8099/getTime
 * @author Andy
 */
public class HttpServer extends BaseServer {

    private SnowFlake snowFlake;


    public HttpServer(SnowFlake snowFlake) {
        this.snowFlake = snowFlake;
        this.port = GlobalConfig.HTTP_PORT;
    }

    @Override
    public void init() {
        super.init();
        b.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(defLoopGroup,
                                new HttpRequestDecoder(),       //请求解码器
                                new HttpObjectAggregator(65536),//将多个消息转换成单一的消息对象
                                new HttpResponseEncoder(),      // 响应编码器
                                new HttpServerHandler(snowFlake)//自定义处理器
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
