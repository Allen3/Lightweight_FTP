package core.handlers.pc;

import core.FtpClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by Allen on 2016/5/31.
 */
@Deprecated
public class ResPrintHandler extends ChannelInboundHandlerAdapter {
    private FtpClient ftpClient;

    public ResPrintHandler(FtpClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (ftpClient.getClientContext().isPcPrintPriviledge()) {
            System.out.print(ftpClient.getResponse());
        } else {
            ftpClient.getClientContext().setPendingPCMessage(ftpClient.getClientContext().getPendingPCMessage() + ftpClient.getResponse());
        }

        super.channelRead(ctx, msg);
    }
}
