package sample;

import core.FtpServer;
import core.impl.DefaultFtpServer;

/**
 * Created by Allen on 2016/4/25.
 */
public class ServerNode {
    public static void main(String args[]) {
        FtpServer ftpServer = new DefaultFtpServer();

        ftpServer.start();
    }
}
