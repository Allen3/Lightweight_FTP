package util.responses;

/**
 * Created by Allen on 2016/5/31.
 */
public class Res_421 extends BaseResponse {
    public Res_421() {
        code = 421;
        message = "Service not available";
    }

    public Res_421(String message) {
        code = 421;
        this.message = message;
    }
}
