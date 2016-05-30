package core;

import io.netty.channel.EventLoopGroup;

/**
 * Created by allen on 4/23/16.
 */
public interface ProtocolInterpreter {

    /**
     * Start the protocol interpreter channel with given eventLoopGroup.*
     */
    void start();
}
