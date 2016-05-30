package core.impl;


import core.ClientContext;
import core.FtpClient;
import core.ResponseListener;
import core.handlers.pc.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import util.requests.AUTH;
import util.requests.BaseRequest;
import util.responses.BaseResponse;

import java.nio.charset.Charset;

/**
 * Created by Allen on 2016/5/30.
 */
public class DefaultFtpClient implements FtpClient {
    private String host;
    private int port;
    private EventLoopGroup eventLoopGroup;
    private Channel channel;

    private ResponseListener responseListener;
    private ClientContext clientContext;
    private BaseRequest lastRequest;
    private BaseResponse response;

    public DefaultFtpClient(ResponseListener responseListener) {
        port = 21;

        this.responseListener = responseListener;
        clientContext = new ClientContext();
        lastRequest = null;
        response = null;
    }

    public void connect(String... args) {
        host = args[0];
        if (args.length > 1) {
            port = Integer.parseInt(args[1]);
        }

        eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap
                    .group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast("ResAnalyzeHandler", new ResAnalyzeHandler(DefaultFtpClient.this));
                            socketChannel.pipeline().addLast("UserResHandler", new UserResHandler(DefaultFtpClient.this));
                            socketChannel.pipeline().addLast("PassResHandler", new PassResHandler(DefaultFtpClient.this));
                            socketChannel.pipeline().addLast("SystResHandler", new SystResHandler(DefaultFtpClient.this));
                            socketChannel.pipeline().addLast("PasvResHandler", new PasvResHandler(DefaultFtpClient.this));
                        }
                    });

            // Wait until connection responds.
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();

            channel = channelFuture.channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
            eventLoopGroup.shutdownGracefully();
        }

    }

    public void disconnect() {
        if (channel != null) {
            try {
                channel.disconnect().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                eventLoopGroup.shutdownGracefully();
            }
        }
    }

    public void operate(BaseRequest request) {
        if (channel != null) {
            ByteBuf byteBuf = channel.alloc().directBuffer();
            byteBuf.writeBytes(request.toString().getBytes(clientContext.getCharset()));
            channel.writeAndFlush(byteBuf);
            lastRequest = request;
        }
    }

    public boolean isConnectionEstablished() {
        return (channel != null);
    }

    public Channel getChannel() {
        return channel;
    }

    public void setResponseListener(ResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    public ResponseListener getResponseListener() {
        return responseListener;
    }

    public void setClientContext(ClientContext clientContext) {
        this.clientContext = clientContext;
    }

    public ClientContext getClientContext() {
        return clientContext;
    }

    public void setLastRequest(BaseRequest lastRequest) {
        this.lastRequest = lastRequest;
    }

    public BaseRequest getLastRequest() {
        return lastRequest;
    }

    public void setResponse(BaseResponse response) {
        this.response = response;
    }

    public BaseResponse getResponse() {
        return response;
    }
}
