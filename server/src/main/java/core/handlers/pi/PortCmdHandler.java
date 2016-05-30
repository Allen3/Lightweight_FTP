package core.handlers.pi;

import core.DataTransferProcessor;
import core.StateContext;
import util.requests.PORT;
import util.responses.Res_200;
import core.impl.ChannelSyncEvent;
import core.impl.ClientDataTransferProcessor;
import core.tasks.DTPChannelTerminator;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import util.NetworkPairv4;

import java.nio.charset.Charset;

/**
 * Created by Allen on 4/30/2016.
 */
public class PortCmdHandler extends SimpleChannelInboundHandler<PORT> implements PIChannelHandler {
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
    protected void channelRead0(final ChannelHandlerContext ctx, PORT msg) throws Exception {

        if (stateContext.getChannelSyncEvent() != null &&
                !stateContext.getChannelSyncEvent().isConsumed() &&
                !stateContext.getChannelSyncEvent().isCanceled()
                ) {
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

        NetworkPairv4 remoteDTPNetworkPair = NetworkPairv4.parse(msg.getParamsAt(0));
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
                byteBuf.writeBytes(new Res_200("PORT command successful. Consider using PASV.").
                        toString().getBytes(stateContext.getConnectionConfig().getCharsetName()));
                System.out.println(byteBuf.toString(Charset.forName("UTF-8")));
                ctx.writeAndFlush(byteBuf);

            }
        });

    }

}
