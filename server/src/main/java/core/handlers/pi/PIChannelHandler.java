package core.handlers.pi;

import core.StateContext;

/**
 * Created by Allen on 4/30/2016.
 *
 * A basic interface for the handlers that implements to work in the PI channel pipeline.
 */
public interface PIChannelHandler {
    StateContext getStateContext();

    void setStateContext(StateContext stateContext);
}
