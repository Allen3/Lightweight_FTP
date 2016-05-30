package util.responses;

/**
 * Created by Allen on 2016/4/25.
 */
public abstract class BaseResponse {
    int code;
    String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return getCode() + " " + getMessage() + System.getProperty("line.separator");
    }
}
