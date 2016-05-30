package core;

import util.responses.BaseResponse;

/**
 * Created by Allen on 2016/5/30.
 */
public interface ResponseListener {
    void listen(BaseResponse response);
}
