package core.handlers.pc;

import core.FtpClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import util.requests.SYST;
import util.responses.BaseResponse;

/**
 * Created by Allen on 2016/5/30.
 */
public class SystResHandler extends SimpleChannelInboundHandler<SYST> {
    private FtpClient ftpClient;

    public SystResHandler(FtpClient ftpClient) {
        this.ftpClient = ftpClient;

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SYST msg) throws Exception {
        if (ftpClient.getResponse().getCode() == 215) {
            ftpClient.getClientContext().setSystemType(ftpClient.getResponse().getMessage());
        }
    }
}
