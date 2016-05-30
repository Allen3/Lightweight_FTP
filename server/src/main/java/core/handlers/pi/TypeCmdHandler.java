package core.handlers.pi;

import core.StateContext;
import util.requests.TYPE;
import util.responses.Res_200;
import util.responses.Res_500;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by Allen on 4/30/2016.
 */
public class TypeCmdHandler extends SimpleChannelInboundHandler<TYPE> implements PIChannelHandler {
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
    protected void channelRead0(ChannelHandlerContext ctx, TYPE msg) throws Exception {
        String arg = msg.getParamsAt(0);
        ByteBuf byteBuf = ctx.alloc().directBuffer();

        if (arg.equalsIgnoreCase("a")) {
            stateContext.getConnectionConfig().setBinaryMode(false);
            byteBuf.writeBytes(new Res_200("Switching to ASCII mode.").toString().getBytes(stateContext.getConnectionConfig().getCharsetName()));
        } else if (arg.equalsIgnoreCase("i")) {
            stateContext.getConnectionConfig().setBinaryMode(true);
            byteBuf.writeBytes(new Res_200("Switching to Binary mode.").toString().getBytes(stateContext.getConnectionConfig().getCharsetName()));
        } else {
            byteBuf.writeBytes(new Res_500().toString().getBytes(stateContext.getConnectionConfig().getCharsetName()));
        }

        ctx.writeAndFlush(byteBuf);
    }
}
