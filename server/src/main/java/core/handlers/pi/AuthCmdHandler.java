package core.handlers.pi;

import core.StateContext;
import util.requests.AUTH;
import util.responses.Res_530;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by Allen on 4/30/2016.
 */
public class AuthCmdHandler extends SimpleChannelInboundHandler<AUTH> implements PIChannelHandler {
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
    protected void channelRead0(ChannelHandlerContext ctx, AUTH msg) throws Exception {
        ByteBuf byteBuf = ctx.alloc().directBuffer();
        //TODO
        // Add TLS or SSL Encription handler.
        // Now only omit these settings requires.
        byteBuf.writeBytes(new Res_530().toString().getBytes(stateContext.getConnectionConfig().getCharsetName()));
        ctx.writeAndFlush(byteBuf);
    }
}
