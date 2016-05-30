package core.tasks;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * Created by allen on 5/6/16.
 *
 * This class is intended for termination of DTP Channel and flushes a response message in PI channel back to client
 * side before the DTP's termination.
 */
public class DTPChannelTerminator implements ChannelFutureListener {
    private Channel piChannel;
    private byte[] response;

    public DTPChannelTerminator(Channel piChannel, byte[] response) {
        this.piChannel = piChannel;
        this.response = response;
    }

    public void operationComplete(ChannelFuture future) throws Exception {
        // Guarantee that channel won't close for repetitively.
        if (future.channel().isActive()) {
            future.channel().close();
        }

        // Clear the resources that DTP Channel create.
        if (future.channel().parent() != null) {
            future.channel().parent().close();
        }

        future.channel().eventLoop().shutdownGracefully();

        if (piChannel!= null && response != null) {
            ByteBuf piByteBuf = piChannel.alloc().directBuffer();
            piByteBuf.writeBytes(response);
            piChannel.writeAndFlush(piByteBuf);
        }
    }
}
