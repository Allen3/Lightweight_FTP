package core.handlers.pi;

import core.DataTransferProcessor;
import core.StateContext;
import util.requests.EPSV;
import util.responses.Res_229;
import core.impl.ChannelSyncEvent;
import core.impl.ServerDataTransferProcessor;
import core.tasks.DTPChannelTerminator;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import util.NetworkPairv6;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * Created by Allen on 2016/5/10.
 */
public class EpsvCmdHandler extends SimpleChannelInboundHandler<EPSV> implements PIChannelHandler {
    private StateContext stateContext;

    public StateContext getStateContext() {
        return stateContext;
    }

    public void setStateContext(StateContext stateContext) {
        this.stateContext = stateContext;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        PIChannelHandler PIInitiator = (PIChannelHandler) ctx.pipeline().get(StateContext.PI_INITIATE_HANDLER);
        this.stateContext = PIInitiator.getStateContext();

        ctx.fireChannelActive();
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, EPSV msg) throws Exception {
        if (stateContext.getChannelSyncEvent() != null && !stateContext.getChannelSyncEvent().isConsumed()) {
            //TODO
            // Some channel sync event has not been consumed yet.
            // Concrete this Exception as {@code ChannelSyncEventNotHandledException}.
            throw new Exception();
        }

        if (stateContext.getDtpChannel() != null && stateContext.getDtpChannel().isActive()) {
            // Shutdown the preexisted dtp channel.
            stateContext.getDtpChannel().close().addListener(new DTPChannelTerminator(null, null));
        }

        stateContext.getConnectionConfig().setPassiveMode(true);
        stateContext.setChannelSyncEvent(new ChannelSyncEvent());
        DataTransferProcessor dataTransferProcessor = new ServerDataTransferProcessor(stateContext);

        ChannelFuture dtpChannelFuture = dataTransferProcessor.initiate();

        dtpChannelFuture.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                InetSocketAddress localInetAddress = (InetSocketAddress) future.channel().localAddress();
                System.out.println("Server DTP Channel started and listened on port " + localInetAddress.getPort());

                // 227 Response.
                ByteBuf byteBuf = ctx.alloc().directBuffer();
                NetworkPairv6 networkPairv6 = new NetworkPairv6();
                networkPairv6.setPort(localInetAddress.getPort());
                byteBuf.writeBytes(
                        new Res_229("Entering Extended Passive Mode (" + NetworkPairv6.compress(networkPairv6) + ").").
                                toString().getBytes(stateContext.getConnectionConfig().getCharsetName()));
                System.out.println(byteBuf.toString(Charset.forName("UTF-8")));
                ctx.writeAndFlush(byteBuf);
            }
        });
    }
}
