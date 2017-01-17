package com.wolfbe.distributedid.client;

import com.wolfbe.distributedid.core.BaseServer;
import com.wolfbe.distributedid.core.SnowFlake;
import com.wolfbe.distributedid.sdks.SdkServerHandler;
import com.wolfbe.distributedid.util.GlobalConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Andy
 */
public class TestServer extends BaseServer {
    public static Map<Channel,String> map;
    private SnowFlake snowFlake;

    public TestServer() {
        this.port = GlobalConfig.SDKS_PORT;
        this.map = new HashMap<>();
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
//                                new FixedLengthFrameDecoder(5),
//                                new StringDecoder(Charset.forName("utf-8")),
//                                new StringEncoder(Charset.forName("utf-8")),
                                new TestHandler()
//                                new TestHandler2()    //自定义处理器
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

            Executors.newScheduledThreadPool(2).scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    Set<Channel> keySet = map.keySet();
                    for (Channel ch : keySet) {
                        logger.info("send 2L");
                        ByteBuffer buffer = ByteBuffer.allocate(8);
                        buffer.putLong(2L);
                        ch.writeAndFlush(buffer);
                    }
                }
            }, 2, 3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error("SdkServer start fail,", e);
        }
    }

    public static void main(String[] args) {
        TestServer server = new TestServer();
        server.init();
        server.start();
    }
}
