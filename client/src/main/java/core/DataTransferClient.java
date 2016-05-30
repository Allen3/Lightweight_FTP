package core;

import core.handlers.dtp.GeneralDataHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.net.InetSocketAddress;

/**
 * Created by Allen on 2016/5/30.
 */
public class DataTransferClient {
    private FtpClient ftpClient;
    private String ipAddress;
    private int port;

    public DataTransferClient(FtpClient ftpClient, String ipAddress, int port) {
        this.ftpClient = ftpClient;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public Channel start() {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        bootstrap
                .group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(ipAddress, port))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("streamer", new ChunkedWriteHandler());
                        ch.pipeline().addLast("generalHandler", new GeneralDataHandler(ftpClient));
                    }
                });

        try {
            ChannelFuture channelFuture = bootstrap.connect().sync();

            return channelFuture.channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }
}
