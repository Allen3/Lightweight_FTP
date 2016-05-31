package core.handlers.pc;

import core.FtpClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import util.responses.BaseResponse;

/**
 * Created by Allen on 2016/5/30.
 */
public class ResAnalyzeHandler extends ChannelInboundHandlerAdapter {
    private FtpClient ftpClient;

    public ResAnalyzeHandler(FtpClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    /**
     * Obtain the response message and parse into a specific {@code BaseResponse}. Then inject the inbound pipeline with
     * a {@code BaseRequest} instance for particular handler to handle with help of the response by adverting to
     * {@code ftpClient.getResponse()}.
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String message = ((ByteBuf) msg).toString(ftpClient.getClientContext().getCharset());

        if (ftpClient.getClientContext().isPcPrintPriviledge() && message!= null) {
            System.out.print(message);
        } else {
            if (ftpClient.getClientContext().getPendingPCMessage() != null) {
                ftpClient.getClientContext().setPendingPCMessage(ftpClient.getClientContext().getPendingPCMessage() + message);
            } else {
                ftpClient.getClientContext().setPendingPCMessage(message);
            }
        }

        // Remove the line separator.
        message = message.replaceAll("(\\r|\\n)", "");
        ftpClient.setResponse(parseResponse(message));

        // Release the Pooled Bytebuf resources.
        ReferenceCountUtil.release(msg);

        // Check whether last request doesn't exist.
        if (ftpClient.getLastRequest() != null) {
            super.channelRead(ctx, ftpClient.getLastRequest());
        }
    }

    private BaseResponse parseResponse(String message) {
        int spaceIndex = message.indexOf(' ');
        String resCodeString = message.substring(0, spaceIndex);
        String resMessage = message.substring(spaceIndex + 2);
        BaseResponse baseResponse = null;

        try {
            baseResponse = (BaseResponse) Class.forName(BaseResponse.class.getPackage().getName() + '.' + "Res_" + resCodeString).newInstance();
            baseResponse.setCode(Integer.parseInt(resCodeString));
            baseResponse.setMessage(resMessage);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return baseResponse;
    }
}
