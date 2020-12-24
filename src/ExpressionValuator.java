import ExpressionParser.Constant;
import ExpressionParser.Node;
import ExpressionParser.Operator;
import ExpressionParser.Variable;

public class ExpressionValuator {

    public double evaluate (Node expressionParsed){
        if(expressionParsed instanceof Operator){
            double a1, a2;
            if(expressionParsed.getChildren().get(0) instanceof Operator){
                a1 = evaluate(expressionParsed.getChildren().get(0));
            }
            else if(expressionParsed.getChildren().get(0) instanceof Variable){
                a1 = 0;
            }
            else{
                a1= 0;
            }
            if(expressionParsed.getChildren().get(1) instanceof Operator){
                a2 = evaluate(expressionParsed.getChildren().get(1));
            }
            else if(expressionParsed.getChildren().get(1) instanceof Variable){
                a2 = 0;
            }
            else{
                a2 = ((Constant) expressionParsed).getValue();
            }
            double[] a = new double[2];
            a[0] = a1;
            a[1] = a2;
            var function = ((Operator) expressionParsed).getType().getFunction();
            return function.apply(a);
        }
        else if(expressionParsed instanceof Variable){

        }
        else{
            return ((Constant) expressionParsed).getValue();
        }
        return 0;
    }
}
