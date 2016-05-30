package util.responses;

/**
 * Created by Allen on 2016/5/10.
 */
public class Res_501 extends BaseResponse {
    public Res_501() {
        code = 501;
        message = null;
    }

    public Res_501(String message) {
        code = 501;
        this.message = message;
    }
}
