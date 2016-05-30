package util.requests;

/**
 * Created by Allen on 4/30/2016.
 */
public class AUTH extends BaseRequest {
    public AUTH() {
        super();
        cmd = AUTH.class.getSimpleName();
    }
}
