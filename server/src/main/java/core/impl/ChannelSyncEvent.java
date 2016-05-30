package core.impl;

import core.StateContext;
import core.tasks.DTPRunnableTask;

/**
 * Created by allen on 5/3/16.
 *
 * This class is intended for the synchronization of PI and DTP Channel. According to the functions required, a {@code
 * CompletableFuture} is strongly suggested rather than using this. In other words, this class is a provisional approach
 * to solve the problem.
 */
public class ChannelSyncEvent {
    private StateContext stateContext;

    private boolean PIMsgArrived;
    private boolean DTPConnected;
    private boolean consumed;
    private boolean cancel;
    private boolean canceled;
    private DTPRunnableTask task;

    public ChannelSyncEvent() {
        PIMsgArrived = false;
        DTPConnected = false;
        consumed = false;
        cancel = false;
        canceled = false;
        task = null;
    }

    public ChannelSyncEvent(StateContext stateContext) {
        this();
        this.stateContext = stateContext;
    }

    public synchronized boolean isPIMsgArrived() {
        return PIMsgArrived;
    }

    public synchronized void setPIMsgArrived(boolean PIMsgArrived) {
        this.PIMsgArrived = PIMsgArrived;
    }

    public synchronized boolean isDTPConnected() {
        return DTPConnected;
    }

    public synchronized void setDTPConnected(boolean DTPConnected) {
        this.DTPConnected = DTPConnected;
    }

    public synchronized boolean isConsumed() {
        return consumed;
    }

    public synchronized void setConsumed(boolean consumed) {
        this.consumed = consumed;
    }

    public synchronized DTPRunnableTask getTask() {
        return task;
    }

    public synchronized void setTask(DTPRunnableTask task) {
        this.task = task;
        task.setChannelSyncEvent(this);
    }

    public synchronized boolean isCancel() {
        return cancel;
    }

    public synchronized void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public synchronized boolean isCanceled() {
        return canceled;
    }

    public synchronized void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public StateContext getStateContext() {
        return stateContext;
    }

    public void setStateContext(StateContext stateContext) {
        this.stateContext = stateContext;
    }
}
