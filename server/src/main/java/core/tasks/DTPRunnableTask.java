package core.tasks;

import core.impl.ChannelSyncEvent;

/**
 * Created by Allen on 5/1/2016.
 */
public abstract class DTPRunnableTask implements Runnable {
    private ChannelSyncEvent channelSyncEvent;

    public DTPRunnableTask() {
    }

    public DTPRunnableTask(ChannelSyncEvent channelSyncEvent) {
        this.channelSyncEvent = channelSyncEvent;
    }

    public ChannelSyncEvent getChannelSyncEvent() {
        return channelSyncEvent;
    }

    public void setChannelSyncEvent(ChannelSyncEvent channelSyncEvent) {
        this.channelSyncEvent = channelSyncEvent;
    }
}
