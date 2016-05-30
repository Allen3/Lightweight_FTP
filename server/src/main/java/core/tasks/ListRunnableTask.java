package core.tasks;

import core.StateContext;
import util.responses.Res_150;
import util.responses.Res_226;
import core.impl.ChannelSyncEvent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.io.File;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;

/**
 * Created by allen on 5/4/16.
 */
public class ListRunnableTask extends DTPRunnableTask {
    private StateContext stateContext;
    private Channel piChannel;
    private Channel dtpChannel;
    private Charset charset;
    private String pathName;

    public ListRunnableTask() {
        super();
    }

    public ListRunnableTask(ChannelSyncEvent channelSyncEvent) {
        super(channelSyncEvent);
    }

    private void init() {
        stateContext = getChannelSyncEvent().getStateContext();

        piChannel = stateContext.getPiChannel();
        dtpChannel = stateContext.getDtpChannel();
        charset = Charset.forName(stateContext.getConnectionConfig().getCharsetName());
        pathName = stateContext.getUserState().getCurrentPath();
    }

    public void run() {
        init();

        if (piChannel != null && dtpChannel != null) {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) dtpChannel.remoteAddress();
            System.out.println("Remote address: " + inetSocketAddress.getAddress() + ":" + inetSocketAddress.getPort());

            // 150 Response.
            ByteBuf piByteBuf = piChannel.alloc().directBuffer();
            piByteBuf.writeBytes(new Res_150().toString().getBytes(charset));
            piChannel.writeAndFlush(piByteBuf);

            ByteBuf dtpByteBuf = dtpChannel.alloc().directBuffer();
            dtpByteBuf.writeBytes(listDataCollect(pathName).getBytes(charset));

            //System.out.println(DTPByteBuf.toString(charset));
            dtpChannel.writeAndFlush(dtpByteBuf).addListener(new DTPChannelTerminator(piChannel, new Res_226().toString().getBytes(charset)));
        }
    }

    private String listDataCollect(String pathName) {
        StringBuilder result = new StringBuilder();

        File currentDir = new File(pathName);
        final File[] files = currentDir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (!file.isHidden()) {
                    if (file.isDirectory()) {
                        result.append('d');
                    } else {
                        result.append('-');
                    }

                    if (file.canRead()) {
                        result.append('r');
                    } else {
                        result.append('-');
                    }

                    if (file.canWrite()) {
                        result.append('w');
                    } else {
                        result.append('-');
                    }

                    if (file.canExecute()) {
                        result.append('x');
                    } else {
                        result.append('-');
                    }

                    result.append("    ");

                    String fileLength = String.format("%1$10d", file.length());
                    result.append(fileLength);

                    result.append("    ");

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd HH:mm");
                    result.append(simpleDateFormat.format(file.lastModified()));

                    result.append("    ");

                    result.append(file.getName());

                    result.append(System.getProperty("line.separator"));
                }
            }
        }

        System.out.println(result);
        return result.toString();
    }
}
