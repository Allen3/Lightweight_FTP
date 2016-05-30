package util.requests;

/**
 * Created by Allen on 4/28/2016.
 */
public abstract class BaseRequest {
    String cmd;
    String[] params;

    public BaseRequest() {
        params = null;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    public String getParamsAt(int index) throws IndexOutOfBoundsException {
        if (index < params.length) {
            return params[index];
        }
        throw new IndexOutOfBoundsException();
    }

    public String getCmd() {
        return cmd;
    }
}
