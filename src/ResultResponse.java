public class ResultResponse extends Response{

    public static String create(String result, long time) {
        return "OK;" + time + ";" + result;
    }
}
