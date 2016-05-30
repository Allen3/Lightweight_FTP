package core.handlers.pc;

import core.FtpClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import util.requests.PORT;

/**
 * Created by Allen on 2016/5/30.
 */
public class PortResHandler extends SimpleChannelInboundHandler<PORT> {
    private FtpClient ftpClient;

    public PortResHandler(FtpClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PORT msg) throws Exception {
        if (ftpClient.getResponse().getCode() == 200) {
            // Just OK.
        }
    }
}
