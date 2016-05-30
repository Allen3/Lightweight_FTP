package core.handlers.pi;

import core.StateContext;
import util.requests.QUIT;
import util.responses.Res_221;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by allen on 5/4/16.
 */
public class QuitCmdHandler extends SimpleChannelInboundHandler<QUIT> implements PIChannelHandler {
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
    protected void channelRead0(ChannelHandlerContext ctx, QUIT msg) throws Exception {
        ByteBuf byteBuf = ctx.alloc().directBuffer();

        byteBuf.writeBytes(new Res_221().toString().getBytes(stateContext.getConnectionConfig().getCharsetName()));
        ctx.writeAndFlush(byteBuf);

        // Shutdown the PI Channel.
        ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
