package util.responses;

/**
 * Created by allen on 5/4/16.
 */
public class Res_250 extends BaseResponse {
    public Res_250() {
        code = 250;
        message = "Directory successfully changed.";
    }

    public Res_250(String message) {
        code = 250;
        this.message = message;
    }
}
