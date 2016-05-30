package util.responses;

/**
 * Created by allen on 5/4/16.
 */
public class Res_550 extends BaseResponse {
    public Res_550() {
        code = 550;
        message = "Requested action not taken.";
    }

    public Res_550(String message) {
        code = 550;
        this.message = message;
    }
}
