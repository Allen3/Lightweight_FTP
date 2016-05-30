package util.responses;

/**
 * Created by Allen on 2016/5/10.
 */
public class Res_229 extends BaseResponse {
    public Res_229() {
        code = 229;
        message = null;
    }

    public Res_229(String message) {
        code = 229;
        this.message = message;
    }
}
