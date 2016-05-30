package core;

import io.netty.channel.Channel;
import util.requests.BaseRequest;
import util.responses.BaseResponse;

/**
 * Created by Allen on 2016/5/30.
 */
public interface FtpClient {
    /**
     * Connect to the remote server specified by args.
     *
     * @param args    Remote server's <i>Hostname/IP<i/> and the <i>port<i/>.
     */
    void connect(String... args);

    /**
     * Disconnect from the remote server, which may do nothing if no connection established.
     */
    void disconnect();

    /**
     * Check whether connection is established with remote server.
     *
     * @return
     */
    boolean isConnectionEstablished();

    /**
     * A general method which sends a request to the connected server in order to execute a specific operations. The
     * instance who invokes this method will get listened when response messages reach, by
     * {@code ResponseListener.listen()}, which may do nothing if no connection established.
     *
     * @param request
     */
    void operate(BaseRequest request);

    Channel getChannel();

    void setResponseListener(ResponseListener responseListener);

    ResponseListener getResponseListener();

    void setClientContext(ClientContext clientContext);

    ClientContext getClientContext();

    void setLastRequest(BaseRequest lastRequest);

    BaseRequest getLastRequest();

    void setResponse(BaseResponse response);

    BaseResponse getResponse();
}
