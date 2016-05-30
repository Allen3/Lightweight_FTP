package sample;

import core.DataTransferClient;
import core.DataTransferServer;
import core.FtpClient;
import core.ResponseListener;
import core.impl.DefaultFtpClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import util.NetworkPair;
import util.NetworkPairv4;
import util.requests.*;
import util.responses.BaseResponse;
import util.responses.Res_331;

import java.net.InetSocketAddress;

/**
 * Created by Allen on 2016/5/30.
 */
public class ClientNode implements ResponseListener {
    private FtpClient ftpClient;

    public ClientNode() {
        ftpClient = new DefaultFtpClient(ClientNode.this);
    }

    public void logIn(String username, String password) {
        ftpClient.getClientContext().setUsername(username);
        ftpClient.getClientContext().setPassword(password);

        if (ftpClient.isConnectionEstablished()) {

            // send USER
            BaseRequest requestUSER = new USER();
            requestUSER.setParams(username);
            ftpClient.setLastRequest(requestUSER);
            ftpClient.operate(requestUSER);
        }
    }

    public void list() {
        BaseRequest requestLIST = new LIST();
        ftpClient.getClientContext().setDtpRequeset(requestLIST);

        if (ftpClient.isConnectionEstablished()) {
            if (ftpClient.getClientContext().isPassiveMode()) {
                // send PASV
                BaseRequest requestPASV = new PASV();
                ftpClient.setLastRequest(requestPASV);
                ftpClient.operate(requestPASV);
            } else {
                // send PORT
                BaseRequest requestPORT = new PORT();

                // Build DataTransferServer.
                DataTransferServer dataTransferServer = new DataTransferServer(ftpClient);
                InetSocketAddress dtpSocketAddress = (InetSocketAddress) dataTransferServer.start().localAddress();
                InetSocketAddress pcSocketAddress = (InetSocketAddress) ftpClient.getChannel().localAddress();
                NetworkPairv4 networkPair = new NetworkPairv4(pcSocketAddress.getAddress().toString().substring(1), dtpSocketAddress.getPort());

                requestPORT.setParams(NetworkPairv4.compress(networkPair));
                ftpClient.setLastRequest(requestPORT);
                ftpClient.operate(requestPORT);

            }
        }
    }

    public void listen(BaseResponse response) {
        //TODO
    }

    public static void main(String args[]) {
        ClientNode clientNode = new ClientNode();
        clientNode.ftpClient.connect("192.168.1.112", "2121");

        clientNode.logIn("a", "bbb");
        clientNode.ftpClient.getClientContext().setPassiveMode(true);
        clientNode.list();
    }
}
