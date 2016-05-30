package core.handlers.pc;

import core.DataTransferClient;
import core.FtpClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import util.NetworkPair;
import util.NetworkPairv4;
import util.requests.PASV;

/**
 * Created by Allen on 2016/5/30.
 */
public class PasvResHandler extends SimpleChannelInboundHandler<PASV> {
    private FtpClient ftpClient;

    public PasvResHandler(FtpClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PASV msg) throws Exception {
        if (ftpClient.getResponse().getCode() == 227) {
            String compressedAddress = ftpClient.getResponse().getMessage();
            compressedAddress = compressedAddress.substring(compressedAddress.indexOf('(') + 1, compressedAddress.indexOf(')'));
            NetworkPair networkPair = NetworkPairv4.parse(compressedAddress);
            ftpClient.getClientContext().setPassiveDTPNetworkPair(networkPair);
            // Build DataTransferClient.
            DataTransferClient dataTransferClient = new DataTransferClient(ftpClient, networkPair.getIpAddress(), networkPair.getPort());

            dataTransferClient.start();
        }
    }
}
