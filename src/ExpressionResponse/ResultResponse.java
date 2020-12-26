package ExpressionResponse;

public class ResultResponse extends Response{

    public static String create(String result, float time) {
        return "OK;" + time + ";" + result;
    }
}
