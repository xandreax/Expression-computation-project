package units.progadv.process.ExpressionResponse;

public class Response {

    private final String result;
    private final String computationTime;
    private final ResponseType respType;

    public Response(String result, String computationTime, ResponseType respType){
        this.result = result;
        this.computationTime = computationTime;
        this.respType = respType;
    }

    @Override
    public String toString (){
        return respType.toString() + ";" + computationTime + ";" + result;
    }

    public enum ResponseType {
        OK,
        ERR
    }
}

