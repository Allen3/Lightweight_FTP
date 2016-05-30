package core.handlers.pi;

import core.StateContext;
import util.requests.STOR;
import core.tasks.StorRunnableTask;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by allen on 5/11/16.
 * <p>
 * The <p>STOR<p/> command will leave the implementation of file check to client.
 */
public class StorCmdHandler extends SimpleChannelInboundHandler<STOR> implements PIChannelHandler {
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
    protected void channelRead0(ChannelHandlerContext ctx, STOR msg) throws Exception {
        if (stateContext.getChannelSyncEvent().getTask() == null) {
            stateContext.getChannelSyncEvent().setTask(new StorRunnableTask(msg.getParamsAt(0)));
        }

        stateContext.setPiChannel(ctx.channel());
        stateContext.getChannelSyncEvent().setPIMsgArrived(true);

        if (stateContext.getChannelSyncEvent().isDTPConnected() && !stateContext.getChannelSyncEvent().isConsumed()) {
            System.out.println("Task done inside StorCmdHandler");
            stateContext.getChannelSyncEvent().setConsumed(true);

            stateContext.getChannelSyncEvent().getTask().run();
        }
    }
}
