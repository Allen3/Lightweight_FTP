package core.impl;

import core.FtpServer;
import core.ProtocolInterpreter;

/**
 * Created by allen on 4/23/16.
 */
public class DefaultFtpServer implements FtpServer {

    private ProtocolInterpreter protocolInterpreter;

    public DefaultFtpServer() {
        protocolInterpreter = new DefaultProtocolInterpreter();
    }

    /**
     * Start the service of FTP Server by starting protocolInterpreter.
     */
    public void start() {
        ProtocolInterpreter protocolInterpreter = new DefaultProtocolInterpreter();
        protocolInterpreter.start();
    }

    public void stop() {

    }
}
