package core.handlers.pi;

import core.StateContext;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by allen on 5/4/16.
 */
public class PITerminateHandler extends ChannelInboundHandlerAdapter implements PIChannelHandler {
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

        ctx.flush();
    }
}
