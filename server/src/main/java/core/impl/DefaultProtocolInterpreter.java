package core.impl;

import core.FtpServer;
import core.ProtocolInterpreter;
import core.handlers.pi.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by allen on 4/23/16.
 */
public class DefaultProtocolInterpreter implements ProtocolInterpreter {
    private final int port;

    public DefaultProtocolInterpreter() {
        port = 2121;
    }

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.
                    group(bossGroup, workerGroup).
                    channel(NioServerSocketChannel.class).
                    localAddress(new InetSocketAddress(port)).
                    childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            //TODO
                            // Handle the IP channel data, including
                            // 1). Initiate the DTP channel if not exist
                            // 2). Do corresponding operations based on the commands.

                            socketChannel.pipeline().addLast("PIInitiateHandler", new PIInitiateHandler());
                            socketChannel.pipeline().addLast("Command analyze handler", new CmdAnalyzeHandler());

                            socketChannel.pipeline().addLast("FEAT handler", new FeatCmdHandler());
                            socketChannel.pipeline().addLast("TYPE handler", new TypeCmdHandler());
                            socketChannel.pipeline().addLast("SYST handler", new SystCmdHandler());
                            socketChannel.pipeline().addLast("AUTH handler", new AuthCmdHandler());
                            socketChannel.pipeline().addLast("USER handler", new UserCmdHandler());
                            socketChannel.pipeline().addLast("PASS handler", new PassCmdHandler());
                            socketChannel.pipeline().addLast("PWD handler", new PwdCmdHandler());
                            socketChannel.pipeline().addLast("PORT handler", new PortCmdHandler());
                            socketChannel.pipeline().addLast("LIST handler", new ListCmdHandler());
                            socketChannel.pipeline().addLast("PASV handler", new PasvCmdHandler());
                            socketChannel.pipeline().addLast("QUIT handler", new QuitCmdHandler());
                            socketChannel.pipeline().addLast("CWD handler", new CwdCmdHandler());
                            socketChannel.pipeline().addLast("RETR handler", new RetrCmdHandler());
                            socketChannel.pipeline().addLast("EPRT handler", new EprtCmdHandler());
                            socketChannel.pipeline().addLast("EPSV handler", new EpsvCmdHandler());
                            socketChannel.pipeline().addLast("OPTS handler", new OptsCmdHandler());
                            socketChannel.pipeline().addLast("STOR handler", new StorCmdHandler());
                            socketChannel.pipeline().addLast("DELE handler", new DeleCmdHandler());

                            socketChannel.pipeline().addLast("PITerminate handler", new PITerminateHandler());

                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind().sync();

            System.out.println(FtpServer.class.getName() + " started and listened on " + channelFuture.channel().localAddress());

            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
