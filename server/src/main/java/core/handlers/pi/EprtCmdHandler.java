package core.handlers.pi;

import core.DataTransferProcessor;
import core.StateContext;
import util.requests.EPRT;
import util.responses.Res_200;
import core.impl.ChannelSyncEvent;
import core.impl.ClientDataTransferProcessor;
import core.tasks.DTPChannelTerminator;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import util.NetworkPair;
import util.NetworkPairv6;

import java.nio.charset.Charset;

/**
 * Created by Allen on 2016/5/10.
 */
public class EprtCmdHandler extends SimpleChannelInboundHandler<EPRT> implements PIChannelHandler {
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
    protected void channelRead0(final ChannelHandlerContext ctx, EPRT msg) throws Exception {
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

        System.out.println("args: " + msg.getParamsAt(0));
        NetworkPair remoteDTPNetworkPair = NetworkPairv6.parse(msg.getParamsAt(0));
        stateContext.getConnectionConfig().setPassiveMode(false);

        stateContext.setChannelSyncEvent(new ChannelSyncEvent());
        DataTransferProcessor dataTransferProcessor = new ClientDataTransferProcessor(
                stateContext,
                remoteDTPNetworkPair.getIpAddress(),
                remoteDTPNetworkPair.getPort());

        ChannelFuture dtpChannelFuture = dataTransferProcessor.initiate();
        dtpChannelFuture.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                // 200 Response.

                ByteBuf byteBuf = ctx.alloc().directBuffer();
                byteBuf.writeBytes(new Res_200("EPRT command successful. Consider using EPSV.").
                        toString().getBytes(stateContext.getConnectionConfig().getCharsetName()));
                System.out.println(byteBuf.toString(Charset.forName("UTF-8")));
                ctx.writeAndFlush(byteBuf);

            }
        });

    }
}
