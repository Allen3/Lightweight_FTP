package sample;

import core.DataTransferServer;
import core.FtpClient;
import core.ResponseListener;
import core.impl.DefaultFtpClient;
import util.NetworkPairv4;
import util.requests.*;
import util.responses.BaseResponse;

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

    public void dtpOperate(BaseRequest request) {
        boolean isDtpRequest = false;
        for (String dtpReq : BaseRequest.DTP_REQUEST) {
            if (request.getCmd().equals(dtpReq)) {
                isDtpRequest = true;
                break;
            }
        }

        if (isDtpRequest) {
            ftpClient.getClientContext().setDtpRequeset(request);

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

    }

    public void listen(BaseResponse response) {
        //TODO
    }

    public static void main(String args[]) {
        ClientNode clientNode = new ClientNode();
        clientNode.ftpClient.connect("192.168.1.104", "21");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        clientNode.logIn("allen", "osullivan");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        clientNode.ftpClient.getClientContext().setPassiveMode(true);

//        BaseRequest requestLIST = new LIST();
//        clientNode.dtpOperate(requestLIST);



//        BaseRequest requestRETR = new RETR();
//        requestRETR.setParams("cu.txt");
//        clientNode.ftpClient.getClientContext().setFileName("cu-backup.txt");
//        clientNode.dtpOperate(requestRETR);

        BaseRequest requestCWD = new CWD();
        requestCWD.setParams("Desktop");
        clientNode.ftpClient.operate(requestCWD);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        BaseRequest requestSTOR = new STOR();
        requestSTOR.setParams("IMG.jpg");
        clientNode.ftpClient.getClientContext().setFileName("IMG_TEST.jpg");
        clientNode.dtpOperate(requestSTOR);
    }
}
