package core.handlers.pi;

import core.StateContext;
import util.requests.SYST;
import util.responses.Res_215;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by Allen on 4/30/2016.
 */
public class SystCmdHandler extends SimpleChannelInboundHandler<SYST> implements PIChannelHandler {
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
    protected void channelRead0(ChannelHandlerContext ctx, SYST msg) throws Exception {
        ByteBuf byteBuf = ctx.alloc().directBuffer();
        byteBuf.writeBytes(new Res_215().toString().getBytes(stateContext.getConnectionConfig().getCharsetName()));

        ctx.writeAndFlush(byteBuf);
    }
}
