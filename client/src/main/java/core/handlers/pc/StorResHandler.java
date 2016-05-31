package core.handlers.pc;

import core.FtpClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.stream.ChunkedFile;
import util.requests.STOR;

import java.io.File;

/**
 * Created by Allen on 2016/5/31.
 */
public class StorResHandler extends SimpleChannelInboundHandler<STOR> {
    private FtpClient ftpClient;

    public StorResHandler(FtpClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, STOR msg) throws Exception {
        if (ftpClient.getResponse().getCode() == 150) {
            // Transfer Started.
            ftpClient.getClientContext().setPcPrintPriviledge(false);

            // Start to transfer file.
            File file = new File(ftpClient.getClientContext().getLocalPath(), ftpClient.getClientContext().getFileName());
            ftpClient.getClientContext().getDtpChannel().writeAndFlush(new ChunkedFile(file)).sync();

            ftpClient.getClientContext().getDtpChannel().eventLoop().shutdownGracefully();

        } else if (ftpClient.getResponse().getCode() == 226) {
            // Transfer Finished.
            ftpClient.getClientContext().setPcPrintPriviledge(true);
            // Print pending message.
            if (ftpClient.getClientContext().getPendingPCMessage() != null) {
                System.out.print(ftpClient.getClientContext().getPendingPCMessage());
            }
        }
    }
}
