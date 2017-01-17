package com.wolfbe.distributedid.sdks;

import com.wolfbe.distributedid.core.BaseServer;
import com.wolfbe.distributedid.core.SnowFlake;
import com.wolfbe.distributedid.util.GlobalConfig;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author Andy
 */
public class SdkServer extends BaseServer {
    private SnowFlake snowFlake;

    public SdkServer(SnowFlake snowFlake) {
        this.snowFlake = snowFlake;
        this.port = GlobalConfig.SDKS_PORT;
    }

    @Override
    public void init() {
        super.init();
        b.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(defLoopGroup,
                                new SdkServerDecoder(12),  // 自定义解码器
                                new SdkServerEncoder(),   // 自定义编码器
                                new SdkServerHandler(snowFlake) // 自定义处理器
                        );
                    }
                });
    }

    @Override
    public void start() {
        try {
            cf = b.bind().sync();
            InetSocketAddress addr = (InetSocketAddress) cf.channel().localAddress();
            logger.info("SdkServer start success, port is:{}", addr.getPort());
        } catch (InterruptedException e) {
            logger.error("SdkServer start fail,", e);
        }
    }
}
