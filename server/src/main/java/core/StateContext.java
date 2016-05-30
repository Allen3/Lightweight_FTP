package core;

import core.impl.ChannelSyncEvent;
import core.impl.ConnectionConfig;
import core.impl.UserState;
import io.netty.channel.Channel;

/**
 * Created by Allen on 4/28/2016.
 *
 */
public interface StateContext {
    String PI_INITIATE_HANDLER = "PIInitiateHandler";

    UserState getUserState();

    ConnectionConfig getConnectionConfig();

    ChannelSyncEvent getChannelSyncEvent();

    void setChannelSyncEvent(ChannelSyncEvent channelSyncEvent);

    Channel getPiChannel();

    void setPiChannel(Channel piChannel);

    Channel getDtpChannel();

    void setDtpChannel(Channel dtpChannel);
}
