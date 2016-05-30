package util.responses;

/**
 * Created by Allen on 4/30/2016.
 */
public class Res_215 extends BaseResponse {
    public Res_215() {
        code = 215;

        //TODO
        // Standardize the message.
        message = System.getProperty("os.name");
    }
}
