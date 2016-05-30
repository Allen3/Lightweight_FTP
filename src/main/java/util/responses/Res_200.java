package util.responses;

/**
 * Created by Allen on 4/30/2016.
 */
public class Res_200 extends BaseResponse {
    public Res_200() {
        code = 200;
        message = "The requested action has been successfully completed.";
    }

    public Res_200(String message) {
        code = 200;
        this.message = message;
    }
}
