package units.progadv.process.ExpressionResponse;

public class ErrorResponse extends Response{

    public static String create(String errorDescription) {
        return "ERR;"+errorDescription;
    }
}
