package core.handlers.pi;

import core.StateContext;
import util.requests.PWD;
import util.responses.Res_257;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by Allen on 4/30/2016.
 */
public class PwdCmdHandler extends SimpleChannelInboundHandler<PWD> implements PIChannelHandler {
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
    protected void channelRead0(ChannelHandlerContext ctx, PWD msg) throws Exception {
        ByteBuf byteBuf = ctx.alloc().directBuffer();
        byteBuf.writeBytes(new Res_257(stateContext.getUserState().getCurrentPath()).toString().getBytes(stateContext.getConnectionConfig().getCharsetName()));
        ctx.writeAndFlush(byteBuf);
    }
}
