package core.handlers.pi;

import core.StateContext;
import util.requests.CWD;
import util.responses.Res_250;
import util.responses.Res_550;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;

/**
 * Created by allen on 5/4/16.
 */
public class CwdCmdHandler extends SimpleChannelInboundHandler<CWD> implements PIChannelHandler {
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
    protected void channelRead0(ChannelHandlerContext ctx, CWD msg) throws Exception {
        String requestFolder = msg.getParamsAt(0);
        String targetDir = null;
        File currentDir = new File(stateContext.getUserState().getCurrentPath());

        if (requestFolder.endsWith("/")) {
            requestFolder = requestFolder.substring(0, requestFolder.length() - 1);
        }

        if (requestFolder.equals("..")) {
            targetDir = currentDir.getParent();
            System.out.println(targetDir);
            if (targetDir == null) {
                // No Parent directory, return the current one.
                targetDir = currentDir.getPath();
            }
        } else if (requestFolder.equals(".")) {
            targetDir = currentDir.getPath();
        } else {
            if (requestFolder.contains(System.getProperty("file.separator"))) {
                System.out.println(System.getProperty("file.separator"));
                // Absolute Path
                File absoluteFile = new File(requestFolder);
                targetDir = absoluteFile.getPath();
            } else {
                File[] files = currentDir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isDirectory() && file.getName().equals(requestFolder)) {
                            targetDir = file.getPath();
                            break;
                        }
                    }
                }
            }
        }

        if (targetDir != null) {
            stateContext.getUserState().setCurrentPath(targetDir);

            // 250 Response.
            ByteBuf byteBuf = ctx.alloc().directBuffer();
            byteBuf.writeBytes(new Res_250().toString().getBytes(stateContext.getConnectionConfig().getCharsetName()));
            ctx.writeAndFlush(byteBuf);
        } else {
            //550 Response.
            ByteBuf byteBuf = ctx.alloc().directBuffer();
            byteBuf.writeBytes(new Res_550("Failed to change directory.").toString().getBytes(stateContext.getConnectionConfig().getCharsetName()));
            ctx.writeAndFlush(byteBuf);
        }

    }
}
