package core.impl;

import core.DataTransferProcessor;
import core.StateContext;
import core.handlers.dtp.DefaultDataHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Created by Allen on 4/30/2016.
 */
public class ServerDataTransferProcessor implements DataTransferProcessor {
    private final int port;
    private StateContext stateContext;

    public ServerDataTransferProcessor(StateContext stateContext) {
        this.stateContext = stateContext;
        port = 0;
    }

    public ChannelFuture initiate() {
        EventLoopGroup group = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap
                .group(group)
                .channel(NioServerSocketChannel.class)
                .localAddress(port)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //TODO
                        // Add the channelHandlers to handle the DTP data.
                        ch.pipeline().addLast("streamer", new ChunkedWriteHandler());
                        ch.pipeline().addLast("default", new DefaultDataHandler(stateContext));
                    }
                });

        return serverBootstrap.bind();
    }
}
