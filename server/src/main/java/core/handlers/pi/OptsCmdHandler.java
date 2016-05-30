package core.handlers.pi;

import core.StateContext;
import util.requests.OPTS;
import util.responses.Res_200;
import util.responses.Res_501;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by Allen on 2016/5/10.
 */
public class OptsCmdHandler extends SimpleChannelInboundHandler<OPTS> implements PIChannelHandler {
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
    protected void channelRead0(ChannelHandlerContext ctx, OPTS msg) throws Exception {
        String arg = msg.getParamsAt(0);
        ByteBuf byteBuf = ctx.alloc().directBuffer();
        if (arg.equalsIgnoreCase("UTF8 ON")) {
            stateContext.getConnectionConfig().setCharsetName("UTF-8");
            byteBuf.writeBytes(new Res_200("Always in UTF8 mode.").toString().getBytes(stateContext.getConnectionConfig().getCharsetName()));
        } else {
            // Unknown OPTS
            byteBuf.writeBytes(new Res_501("Option not understood.").toString().getBytes(stateContext.getConnectionConfig().getCharsetName()));
        }

        ctx.writeAndFlush(byteBuf);
    }
}
