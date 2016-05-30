package core.handlers.pi;

import core.StateContext;
import util.responses.Res_220;
import core.impl.DefaultStateContext;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by Allen on 4/30/2016.
 */
public class PIInitiateHandler extends ChannelInboundHandlerAdapter implements PIChannelHandler {
    private StateContext stateContext;

    /**
     * Channel connection established(Underlying socket established), then instantiates an instance of StateContext
     * intended for this channel.
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        stateContext = new DefaultStateContext();

        // Pop back a welcoming message.
        ByteBuf outputByteBuf = ctx.alloc().directBuffer();
        outputByteBuf.writeBytes(new Res_220().toString().getBytes(stateContext.getConnectionConfig().getCharsetName()));
        ctx.write(outputByteBuf);

        // Must forward channelActive() as the following channelHandlers in the pipeline need to set their stateContext.
        ctx.fireChannelActive();
    }

    public StateContext getStateContext() {
        return stateContext;
    }

    /**
     * This method should never be invoked. Only to implements the interface.
     *
     * @param stateContext
     */
    @Deprecated
    public void setStateContext(StateContext stateContext) {
    }
}
