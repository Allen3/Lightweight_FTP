package core.impl;

import core.DataTransferProcessor;
import core.StateContext;
import core.handlers.dtp.DefaultDataHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.net.InetSocketAddress;

/**
 * Created by Allen on 4/30/2016.
 */
public class ClientDataTransferProcessor implements DataTransferProcessor {
    private final StateContext stateContext;
    private final String ipAddress;
    private final int port;

    public ClientDataTransferProcessor(StateContext stateContext, String ipAddress, int port) {
        this.stateContext = stateContext;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public ChannelFuture initiate() {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        bootstrap
                .group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(ipAddress, port))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //TODO
                        // Add the channelHandlers to handle the DTP data.
                        ch.pipeline().addLast("streamer", new ChunkedWriteHandler());
                        ch.pipeline().addLast("default", new DefaultDataHandler(stateContext));
                    }
                });

        return bootstrap.connect();
    }
}
