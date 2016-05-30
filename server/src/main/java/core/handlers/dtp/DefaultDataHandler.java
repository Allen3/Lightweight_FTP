package core.handlers.dtp;

import core.StateContext;
import util.responses.Res_500;
import core.tasks.DTPChannelTerminator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;

/**
 * Created by Allen on 4/30/2016.
 */
public class DefaultDataHandler extends ChannelInboundHandlerAdapter {
    private StateContext stateContext;

    public DefaultDataHandler(StateContext stateContext) {
        this.stateContext = stateContext;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("DTP Channel Activated!");

        InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        System.out.println("Remote DTP Address : " + inetSocketAddress.getAddress() + ":" + inetSocketAddress.getPort());


        stateContext.setDtpChannel(ctx.channel());
        stateContext.getChannelSyncEvent().setDTPConnected(true);
        if (stateContext.getChannelSyncEvent().isPIMsgArrived() && !stateContext.getChannelSyncEvent().isConsumed()) {
            System.out.println("Task done inside DefaultHandler");
            stateContext.getChannelSyncEvent().setConsumed(true);

            if (stateContext.getChannelSyncEvent().getTask() != null) {
                stateContext.getChannelSyncEvent().getTask().run();
            }
        }

        // Add cancel logic here.
        if (stateContext.getChannelSyncEvent().isCancel() && !stateContext.getChannelSyncEvent().isCanceled() && stateContext.getDtpChannel() != null) {
            stateContext.getChannelSyncEvent().setCanceled(true);
            System.out.println("DTP channel cancel inside DefaultDataHandler");
            // Shutdown the DTP Channel.
            stateContext.getDtpChannel().close().addListener(new DTPChannelTerminator(
                    ctx.channel(),
                    new Res_500("File not Found.").toString().getBytes(stateContext.getConnectionConfig().getCharsetName())
                    ));
        }

        ctx.fireChannelActive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("DTP Channel: " + msg.toString().getBytes("UTF-8"));
        ctx.fireChannelRead(msg);
    }

}
