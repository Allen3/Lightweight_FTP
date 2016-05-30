package core.handlers.pi;

import core.StateContext;
import util.requests.LIST;
import core.tasks.ListRunnableTask;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by Allen on 5/1/2016.
 */
public class ListCmdHandler extends SimpleChannelInboundHandler<LIST> implements PIChannelHandler {
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
    protected void channelRead0(ChannelHandlerContext ctx, LIST msg) throws Exception {
        //TODO
        // Gather information of files and send through DTP Channel.
        //System.out.println(msg.toString());

        // Set the task that the List operation should do.
        if (stateContext.getChannelSyncEvent().getTask() == null) {
            stateContext.getChannelSyncEvent().setTask(new ListRunnableTask());
        }
        stateContext.setPiChannel(ctx.channel());
        stateContext.getChannelSyncEvent().setPIMsgArrived(true);

        if (stateContext.getChannelSyncEvent().isDTPConnected() && !stateContext.getChannelSyncEvent().isConsumed()) {
            System.out.println("Task done inside ListCmdHandler");
            stateContext.getChannelSyncEvent().setConsumed(true);

            stateContext.getChannelSyncEvent().getTask().run();
        }

    }
}
