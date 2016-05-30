package core.handlers.pi;

import core.StateContext;
import util.requests.BaseRequest;
import util.responses.Res_502;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Allen on 2016/4/25.
 *
 * This ChannelHandler poses as the first ChannelHandler for PI communication, mainly analyzes the request commands and
 * forwards to the following handlers.
 */
public class CmdAnalyzeHandler extends ChannelInboundHandlerAdapter implements PIChannelHandler {

    private StateContext stateContext;

    private String command;
    private String[] params;


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        PIChannelHandler PIInitiator = (PIChannelHandler) ctx.pipeline().get(StateContext.PI_INITIATE_HANDLER);
        this.stateContext = PIInitiator.getStateContext();

        System.out.println(stateContext);

        ctx.fireChannelActive();
    }

    /**
     * Obtain message and then parse into commands and params, with which to analyze the type of request command. The
     * analyzed command type would be specific to a type that following IPChannelHandler that can handle.
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String message = ((ByteBuf) msg).toString(Charset.forName(stateContext.getConnectionConfig().getCharsetName()));

        // Remove the line separator.
        message = message.replaceAll("(\\r|\\n)", "");
        System.out.println("Server received: " + message);

        parseCommand(message);

        try {
            BaseRequest baseRequest = (BaseRequest) Class.forName(BaseRequest.class.getPackage().getName() + "." + command).newInstance();
            baseRequest.setParams(params);

            ctx.fireChannelRead(baseRequest);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            ByteBuf byteBuf = ctx.alloc().directBuffer();
            byteBuf.writeBytes(new Res_502().toString().getBytes(stateContext.getConnectionConfig().getCharsetName()));

            ctx.writeAndFlush(byteBuf);
        }

        // Release the Pooled Bytebuf resources.
        ReferenceCountUtil.release(msg);
    }

    public StateContext getStateContext() {
        return stateContext;
    }

    public void setStateContext(StateContext stateContext) {
        this.stateContext = stateContext;
    }

    /**
     * Parse the message String into command and its sole arg.
     *
     * @param message    The raw command message.
     */
    private void parseCommand(String message) {
        int spaceIndex = message.indexOf(' ');
        if (spaceIndex > 0) {
            command = message.substring(0, spaceIndex);
            List<String> paramsList = new ArrayList<String>();

            message = message.substring(spaceIndex + 1);
            /*while ((spaceIndex = message.indexOf(' ')) > 0) {
                paramsList.add(message.substring(0, spaceIndex));
                message = message.substring(spaceIndex + 1);
            }*/

            paramsList.add(message);
            params = paramsList.toArray(new String[0]);
        } else {
            command = message;
        }
    }
}
