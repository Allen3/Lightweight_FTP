package util.requests;

/**
 * Created by allen on 5/4/16.
 */
public class QUIT extends BaseRequest {
    public QUIT() {
        super();
        cmd = QUIT.class.getSimpleName();
    }
}
