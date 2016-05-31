package core.handlers.dtp;

import core.FtpClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import util.requests.LIST;
import util.requests.RETR;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Allen on 2016/5/30.
 */
public class GeneralDataHandler extends ChannelInboundHandlerAdapter {
    private FtpClient ftpClient;
    private FileOutputStream fileOutputStream;

    public GeneralDataHandler(FtpClient ftpClient) {
        this.ftpClient = ftpClient;
        fileOutputStream = null;
    }
    //TODO
    // Close the channel after data transfer done.

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // Save the reference to the established DTP connection.
        ftpClient.getClientContext().setDtpChannel(ctx.channel());

        ftpClient.setLastRequest(ftpClient.getClientContext().getDtpRequeset());
        ftpClient.operate(ftpClient.getClientContext().getDtpRequeset());

        if (ftpClient.getClientContext().getDtpRequeset() instanceof RETR) {
            fileOutputStream = new FileOutputStream(new File(ftpClient.getClientContext().getLocalPath(), ftpClient.getClientContext().getFileName()), true);
        }

        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // LIST
        if (ftpClient.getClientContext().getDtpRequeset() instanceof LIST) {
            String message = ((ByteBuf) msg).toString(ftpClient.getClientContext().getCharset());
            System.out.print(message);
        } else if (ftpClient.getClientContext().getDtpRequeset() instanceof RETR) {
            byte data[] = new byte[((ByteBuf) msg).readableBytes()];
            ((ByteBuf) msg).readBytes(data);

            // Write file.
            fileOutputStream.write(data);
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (fileOutputStream != null) {
            fileOutputStream.close();
        }
        super.channelInactive(ctx);
    }
}
