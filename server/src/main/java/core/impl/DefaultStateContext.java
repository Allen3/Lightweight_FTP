package core.impl;

import core.StateContext;
import io.netty.channel.Channel;

/**
 * Created by Allen on 4/30/2016.
 */
public class DefaultStateContext implements StateContext {
    private ConnectionConfig connectionConfig;
    private UserState userState;
    private Channel piChannel;
    private Channel dtpChannel;

    private ChannelSyncEvent channelSyncEvent;

    public DefaultStateContext() {
        connectionConfig = new ConnectionConfig(this, true, "UTF-8", false);
        userState = new UserState(this);
        channelSyncEvent = null;
    }

    public ConnectionConfig getConnectionConfig() {
        return connectionConfig;
    }


    public UserState getUserState() {
        return userState;
    }

    public ChannelSyncEvent getChannelSyncEvent() {
        return channelSyncEvent;
    }

    public void setChannelSyncEvent(ChannelSyncEvent channelSyncEvent) {
        this.channelSyncEvent = channelSyncEvent;
        channelSyncEvent.setStateContext(this);
    }

    public Channel getPiChannel() {
        return piChannel;
    }

    public void setPiChannel(Channel piChannel) {
        this.piChannel = piChannel;
    }

    public Channel getDtpChannel() {
        return dtpChannel;
    }

    public void setDtpChannel(Channel dtpChannel) {
        this.dtpChannel = dtpChannel;
    }
}
