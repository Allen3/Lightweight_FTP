package util.responses;

/**
 * Created by Allen on 5/1/2016.
 */
public class Res_150 extends BaseResponse {
    public Res_150() {
        code = 150;
        message = "Data transport ready.";
    }

    public Res_150(String message) {
        code = 150;
        this.message = message;
    }
}
