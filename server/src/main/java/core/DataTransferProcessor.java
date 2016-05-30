package core;

import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;

/**
 * Created by Allen on 4/30/2016.
 */
public interface DataTransferProcessor {
    ChannelFuture initiate();
}
