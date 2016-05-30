package core.handlers.dtp;

import util.responses.Res_226;
import core.tasks.DTPChannelTerminator;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.FileOutputStream;
import java.nio.charset.Charset;

/**
 * Created by allen on 5/11/16.
 */
public class StorDataHandler extends ChannelInboundHandlerAdapter {
    public static final String NAME = "StoreDataHandler";
    private Channel piChannel;
    private FileOutputStream fileOutputStream;
    private Charset charset;

    public StorDataHandler(Channel piChannel, FileOutputStream fileOutputStream, Charset charset) {
        this.piChannel = piChannel;
        this.fileOutputStream = fileOutputStream;
        this.charset = charset;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        byte data[] = new byte[((ByteBuf) msg).readableBytes()];
        ((ByteBuf) msg).readBytes(data);

        fileOutputStream.write(data);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("DTP Channel Inactive!");
        fileOutputStream.close();
        ctx.channel().close().addListener(new DTPChannelTerminator(piChannel, new Res_226().toString().getBytes(charset)));
        super.channelInactive(ctx);
    }
}
