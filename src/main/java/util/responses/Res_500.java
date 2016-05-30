package util.responses;

/**
 * Created by Allen on 4/30/2016.
 */
public class Res_500 extends BaseResponse {
    public Res_500() {
        code = 500;
        //TODO
        // Message could be more detailed for specific action.
        message = "Syntax error";
    }

    public Res_500(String message) {
        code = 500;
        //TODO
        // Message could be more detailed for specific action.
        this.message = message;
    }
}
