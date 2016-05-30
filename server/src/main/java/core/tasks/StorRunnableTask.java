package core.tasks;

import core.StateContext;
import util.responses.Res_150;
import core.handlers.dtp.StorDataHandler;
import core.impl.ChannelSyncEvent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.charset.Charset;

/**
 * Created by allen on 5/11/16.
 */
public class StorRunnableTask extends DTPRunnableTask {
    private StateContext stateContext;
    private Channel piChannel;
    private Channel dtpChannel;
    private Charset charset;
    private String fileName;

    public StorRunnableTask(String fileName) {
        super();
        this.fileName = fileName;
    }

    public StorRunnableTask(ChannelSyncEvent channelSyncEvent) {
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
            piByteBuf.writeBytes(new Res_150("OK to send data.").toString().getBytes(charset));
            piChannel.writeAndFlush(piByteBuf);

            // accept the file.
            try {
                File file = new File(stateContext.getUserState().getCurrentPath() + System.getProperty("file.separator") + fileName);
                FileOutputStream fileOutputStream = new FileOutputStream(file, true);

                // Add a data inbound handler to read the dtp data.
                dtpChannel.pipeline().addLast(StorDataHandler.NAME, new StorDataHandler(piChannel, fileOutputStream, charset));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
