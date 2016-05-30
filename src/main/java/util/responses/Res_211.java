package util.responses;

/**
 * Created by Allen on 2016/5/13.
 */
public class Res_211 extends BaseResponse {
    public Res_211() {
        code = 211;
        message = null;
    }

    public Res_211(String... features) {
        code = 211;
        StringBuilder stringBuilder = new StringBuilder();
        for (String feature : features) {
            stringBuilder.append(" ").append(feature).append(System.getProperty("line.separator"));
        }
        message = stringBuilder.toString();
    }

    @Override
    public String toString() {
        return getCode() + "-" + "Features:" + System.getProperty("line.separator") +
                getMessage() +
                getCode() + " " + "End" + System.getProperty("line.separator");
    }
}
