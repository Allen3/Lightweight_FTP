package core.handlers.pi;

import core.StateContext;
import util.requests.DELE;
import util.responses.Res_250;
import util.responses.Res_550;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;

/**
 * Created by Allen on 2016/5/13.
 */
public class DeleCmdHandler extends SimpleChannelInboundHandler<DELE> implements PIChannelHandler {
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
    protected void channelRead0(ChannelHandlerContext ctx, DELE msg) throws Exception {
        String fileName = msg.getParamsAt(0);
        File file = new File(stateContext.getUserState().getCurrentPath() + System.getProperty("file.separator") + fileName);

        // TODO
        // Check whether file.delte() is a blocking process as it requests file I/O operation.

        try {
            if (file.delete()) {
                //Delete successfully.
                // 250 Response.
                ByteBuf byteBuf = ctx.alloc().directBuffer();
                byteBuf.writeBytes(new Res_250("Delete operation successful.").toString().getBytes(stateContext.getConnectionConfig().getCharsetName()));
                ctx.writeAndFlush(byteBuf);
            } else {
                //Delete unsuccessfully.
                // 550 Response.
                ByteBuf byteBuf = ctx.alloc().directBuffer();
                byteBuf.writeBytes(new Res_550("Delete operation failed.").toString().getBytes(stateContext.getConnectionConfig().getCharsetName()));
                ctx.writeAndFlush(byteBuf);
            }
        } catch (SecurityException e) {
            //TODO
            // Handle the Security Exception.
            // 550 Response.
            ByteBuf byteBuf = ctx.alloc().directBuffer();
            byteBuf.writeBytes(new Res_550("Permission denied.").toString().getBytes(stateContext.getConnectionConfig().getCharsetName()));
            ctx.writeAndFlush(byteBuf);
            //e.printStackTrace();
        }
    }
}
