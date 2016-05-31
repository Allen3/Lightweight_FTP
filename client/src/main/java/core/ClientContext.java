package core;

import io.netty.channel.Channel;
import util.NetworkPair;
import util.requests.BaseRequest;

import java.nio.charset.Charset;

/**
 * Created by Allen on 2016/5/30.
 */
public class ClientContext {
    private String username;
    private String password;
    private Charset charset;
    private String systemType;
    private boolean passiveMode;
    private NetworkPair passiveDTPNetworkPair;
    private Channel dtpChannel;
    private BaseRequest dtpRequeset;
    private String fileName;
    private boolean pcPrintPriviledge;
    private String pendingPCMessage;

    public ClientContext() {
        charset = Charset.forName("UTF-8");
        passiveMode = false;
        dtpChannel = null;
        dtpRequeset = null;
        fileName = null;
        pcPrintPriviledge = true;
        pendingPCMessage = null;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Charset getCharset() {
        return charset;
    }

    public String getSystemType() {
        return systemType;
    }

    public boolean isPassiveMode() {
        return passiveMode;
    }

    public NetworkPair getPassiveDTPNetworkPair() {
        return passiveDTPNetworkPair;
    }

    public Channel getDtpChannel() {
        return dtpChannel;
    }

    public BaseRequest getDtpRequeset() {
        return dtpRequeset;
    }

    public String getFileName() {
        return fileName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public void setPassiveMode(boolean passiveMode) {
        this.passiveMode = passiveMode;
    }

    public void setPassiveDTPNetworkPair(NetworkPair passiveDTPNetworkPair) {
        this.passiveDTPNetworkPair = passiveDTPNetworkPair;
    }

    public void setDtpChannel(Channel dtpChannel) {
        this.dtpChannel = dtpChannel;
    }

    public void setDtpRequeset(BaseRequest dtpRequeset) {
        this.dtpRequeset = dtpRequeset;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public synchronized boolean isPcPrintPriviledge() {
        return pcPrintPriviledge;
    }

    public synchronized void setPcPrintPriviledge(boolean pcPrintPriviledge) {
        this.pcPrintPriviledge = pcPrintPriviledge;
    }

    public String getPendingPCMessage() {
        return pendingPCMessage;
    }

    public void setPendingPCMessage(String pendingPCMessage) {
        this.pendingPCMessage = pendingPCMessage;
    }
}
