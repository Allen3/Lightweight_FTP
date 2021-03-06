package core.handlers.pc;

import core.FtpClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import util.requests.LIST;

/**
 * Created by Allen on 2016/5/30.
 */
public class ListResHandler extends SimpleChannelInboundHandler<LIST> {
    private FtpClient ftpClient;

    public ListResHandler(FtpClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LIST msg) throws Exception {
        if (ftpClient.getResponse().getCode() == 150) {
            // Transfer Started.
            ftpClient.getClientContext().setPcPrintPriviledge(false);
        } else if (ftpClient.getResponse().getCode() == 226) {
            // Transfer Finished.
            ftpClient.getClientContext().setPcPrintPriviledge(true);
            // Print pending message.
            if (ftpClient.getClientContext().getPendingPCMessage() != null) {
                System.out.print(ftpClient.getClientContext().getPendingPCMessage());
            }
            ftpClient.getClientContext().getDtpChannel().eventLoop().shutdownGracefully();
        }
    }
}
