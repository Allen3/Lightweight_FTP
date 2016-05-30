package core.handlers.pc;

import core.FtpClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import util.requests.BaseRequest;
import util.requests.PASS;
import util.requests.USER;
import util.responses.BaseResponse;

/**
 * Created by Allen on 2016/5/30.
 */
public class UserResHandler extends SimpleChannelInboundHandler<USER> {
    private FtpClient ftpClient;

    public UserResHandler(FtpClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, USER msg) throws Exception {
        if (ftpClient.getResponse().getCode() == 331) {
            // send PASS
            BaseRequest requestPASS = new PASS();
            requestPASS.setParams(ftpClient.getClientContext().getPassword());
            ftpClient.setLastRequest(requestPASS);
            ftpClient.operate(requestPASS);
        }
    }
}
