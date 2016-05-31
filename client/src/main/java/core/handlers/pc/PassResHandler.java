package core.handlers.pc;

import core.FtpClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import util.requests.BaseRequest;
import util.requests.PASS;
import util.requests.SYST;
import util.responses.BaseResponse;

/**
 * Created by Allen on 2016/5/30.
 */
public class PassResHandler extends SimpleChannelInboundHandler<PASS> {
    private FtpClient ftpClient;

    public PassResHandler(FtpClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PASS msg) throws Exception {
        if (ftpClient.getResponse().getCode() == 230) {
            // send SYST
            BaseRequest requestSYST = new SYST();
            ftpClient.setLastRequest(requestSYST);
            ftpClient.operate(requestSYST);
        } else if (ftpClient.getResponse().getCode() == 530) {
            //TODO
            // Not logged in.
        }
    }
}
