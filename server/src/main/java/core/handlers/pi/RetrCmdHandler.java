package core.handlers.pi;

import core.StateContext;
import util.requests.RETR;
import util.responses.Res_500;
import core.tasks.DTPChannelTerminator;
import core.tasks.RetrRunnableTask;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;

/**
 * Created by allen on 5/4/16.
 */
public class RetrCmdHandler extends SimpleChannelInboundHandler<RETR> implements PIChannelHandler {
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
    protected void channelRead0(ChannelHandlerContext ctx, RETR msg) throws Exception {
        String fileName = msg.getParamsAt(0);
        System.out.println("Filename: " + fileName);

        File currentDir = new File(stateContext.getUserState().getCurrentPath());
        for (File file : currentDir.listFiles()) {
            if (!file.isDirectory() && !file.isHidden() && file.getName().equals(fileName)) {

                if (stateContext.getChannelSyncEvent().getTask() == null) {
                    stateContext.getChannelSyncEvent().setTask(new RetrRunnableTask(file));
                }

                stateContext.setPiChannel(ctx.channel());
                stateContext.getChannelSyncEvent().setPIMsgArrived(true);

                if (stateContext.getChannelSyncEvent().isDTPConnected() && !stateContext.getChannelSyncEvent().isConsumed()) {
                    System.out.println("Task done inside RetrCmdHandler");
                    stateContext.getChannelSyncEvent().setConsumed(true);

                    stateContext.getChannelSyncEvent().getTask().run();
                }

                return;
            }
        }

        // Respond that file not exist Exception.
        stateContext.getChannelSyncEvent().setCancel(true);
        if (stateContext.getChannelSyncEvent().isCancel() && !stateContext.getChannelSyncEvent().isCanceled() && stateContext.getDtpChannel() != null) {
            stateContext.getChannelSyncEvent().setCanceled(true);
            System.out.println("DTP channel cancel inside RetrCmdHandler");
            // Shutdown the DTP Channel.
            stateContext.getDtpChannel().close().addListener(
                    new DTPChannelTerminator(ctx.channel(),
                            new Res_500("File not Found.").toString().getBytes(stateContext.getConnectionConfig().getCharsetName()))
            );

        }

    }
}
