package AP2014.infixpostfix;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Postfix {

    private final static String _matcherRegex="[\\+\\*\\/-]|[\\(\\)]";
    private final static String valueMatcherRegex="\\d*\\.?\\d+|[a-zA-Z]";
    private final static String matcherRegex;
    static {matcherRegex=_matcherRegex+"|"+valueMatcherRegex;}

    private final static String operatorsPrecedence="+-*/";
    private int getOperatorPrecedence(String opr){
        return operatorsPrecedence.indexOf(opr);
    }

    public Postfix(String eq){
        Pattern pattern=Pattern.compile(matcherRegex);
        Matcher matcher=pattern.matcher(eq);

        StringBuffer outputString=new StringBuffer();
        Stack<String> operatorStack=new Stack<String>();

        while(matcher.find()) {
            String matchedItem=matcher.group();
            if(!matchedItem.matches(valueMatcherRegex)) {

                //Operator
                String currOpr=matchedItem;

                if(operatorStack.isEmpty() || matchedItem.equals("(")) {
                    operatorStack.push(currOpr);
                    continue;
                }

                int CurrPrc=getOperatorPrecedence(currOpr);
                String topOpr=operatorStack.peek();
                while(!operatorStack.isEmpty() &&
                        getOperatorPrecedence(topOpr)>=CurrPrc) {
                    if(topOpr.equals("(")) {//matching ')' as current op
                        operatorStack.pop();
                        break;//effectively discarding the '('
                    }
                    outputString.append(operatorStack.pop());
                    if(!operatorStack.isEmpty())
                        topOpr=operatorStack.peek();
                }

                if(!matchedItem.equals(")"))
                    operatorStack.push(matchedItem);

            } else
                //Operand
                outputString.append(matchedItem);
        }

        while(!operatorStack.isEmpty())
            outputString.append(operatorStack.pop());

        System.out.println(outputString.toString());
    }

}
