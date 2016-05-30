package core;

import core.handlers.dtp.GeneralDataHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Created by Allen on 2016/5/30.
 */
public class DataTransferServer {
    private FtpClient ftpClient;

    public DataTransferServer(FtpClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    public Channel start() {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap
                .group(eventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .localAddress(0)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("streamer", new ChunkedWriteHandler());
                        ch.pipeline().addLast("generalHandler", new GeneralDataHandler(ftpClient));
                    }
                });

        try {
            return bootstrap.bind().sync().channel();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
