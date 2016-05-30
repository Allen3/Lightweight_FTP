package util.requests;

/**
 * Created by Allen on 4/28/2016.
 */
public class USER extends BaseRequest {
    public USER() {
        super();
        cmd = USER.class.getSimpleName();
    }
}
