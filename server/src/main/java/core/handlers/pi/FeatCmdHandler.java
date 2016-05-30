package core.handlers.pi;

import core.StateContext;
import util.requests.FEAT;
import util.responses.Res_211;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by Allen on 2016/5/13.
 */
public class FeatCmdHandler extends SimpleChannelInboundHandler<FEAT> implements PIChannelHandler {
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
    protected void channelRead0(ChannelHandlerContext ctx, FEAT msg) throws Exception {
        // 211-Features Response.
        ByteBuf byteBuf = ctx.alloc().directBuffer();
        //TODO
        // Add more features to the 211 response list.
        byteBuf.writeBytes(new Res_211("UTF8").toString().getBytes(stateContext.getConnectionConfig().getCharsetName()));
        ctx.writeAndFlush(byteBuf);
    }
}
