package core.tasks;

import core.StateContext;
import util.responses.Res_150;
import util.responses.Res_226;
import core.impl.ChannelSyncEvent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.stream.ChunkedFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by allen on 5/6/16.
 */
public class RetrRunnableTask extends DTPRunnableTask {
    private StateContext stateContext;
    private Channel piChannel;
    private Channel dtpChannel;
    private Charset charset;
    private File file;

    public RetrRunnableTask(File file) {
        super();
        this.file = file;
    }

    public RetrRunnableTask(ChannelSyncEvent channelSyncEvent) {
        super(channelSyncEvent);
    }

    private void init() {
        stateContext = getChannelSyncEvent().getStateContext();

        piChannel = stateContext.getPiChannel();
        dtpChannel = stateContext.getDtpChannel();
        charset = Charset.forName(stateContext.getConnectionConfig().getCharsetName());
    }

    public void run() {
        init();

        if (piChannel != null && dtpChannel != null) {
            // 150 Response.
            ByteBuf piByteBuf = piChannel.alloc().directBuffer();
            String resMsg = null;
            if (stateContext.getConnectionConfig().isBinaryMode()) {
                resMsg = "Opening BINARY mode data connection for ";
            } else {
                resMsg = "Opening ASCII mode data connection for ";
            }
            piByteBuf.writeBytes(new Res_150(resMsg + file.getName() + " (" + file.length() + " bytes).").toString().getBytes(charset));
            piChannel.writeAndFlush(piByteBuf);

            // Transfer the file content data.
            try {
                dtpChannel.writeAndFlush(new ChunkedFile(file)).addListener(new DTPChannelTerminator(piChannel, new Res_226().toString().getBytes(charset)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
