package util.responses;

/**
 * Created by Allen on 4/30/2016.
 */
public class Res_227 extends BaseResponse {
    public Res_227() {
        code = 227;
        message = null;
    }

    public Res_227(String message) {
        code = 227;
        this.message = message;
    }
}
