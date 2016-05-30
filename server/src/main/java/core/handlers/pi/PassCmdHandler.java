package core.handlers.pi;

import core.StateContext;
import util.requests.PASS;
import util.responses.Res_230;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by Allen on 4/30/2016.
 */
public class PassCmdHandler extends SimpleChannelInboundHandler<PASS> implements PIChannelHandler {
    private StateContext stateContext;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        PIChannelHandler PIInitiator = (PIChannelHandler) ctx.pipeline().get(StateContext.PI_INITIATE_HANDLER);
        this.stateContext = PIInitiator.getStateContext();

        ctx.fireChannelActive();
    }

    public StateContext getStateContext() {
        return stateContext;
    }

    public void setStateContext(StateContext stateContext) {
        this.stateContext = stateContext;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PASS msg) throws Exception {
        stateContext.getUserState().setPassword(msg.getParamsAt(0));
        if (stateContext.getUserState().isAuthenticated()) {
            ByteBuf byteBuf = ctx.alloc().directBuffer();
            byteBuf.writeBytes(new Res_230().toString().getBytes(stateContext.getConnectionConfig().getCharsetName()));
            ctx.writeAndFlush(byteBuf);
        }
    }
}
