package util.responses;

/**
 * Created by Allen on 4/30/2016.
 */
public class Res_502 extends BaseResponse {
    public Res_502() {
        code = 502;
        message = "Command not implemented.";
    }
}
