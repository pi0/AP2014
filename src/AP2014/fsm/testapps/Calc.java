package AP2014.fsm.testapps;

import java.util.ArrayList;
import java.util.Stack;

public class Calc {

    double currentValue;
    int currentFloatDigitCounter;
    boolean currentIsFloat;
    boolean isCurrentNegative;

    ArrayList<Object> postfix=new ArrayList<Object>();
    Stack<Character>  operatorsStack=new Stack<Character>();
    int prCounter=0;

    public void start(String src,String input) {
        if(src.equals("digit"))
            addNumber();
        resetNumber();
    }

    public void neg(String src,String input) {
        isCurrentNegative=true;
    }

    public void dot(String src,String input) {
        currentIsFloat =true;
    }

    public void digit(String src,String input) {
        int d=stringToDigit(input);
        if(d>0) { // So it is a digit !
            currentValue = 10 * currentValue + d;
            if (currentIsFloat)
                currentFloatDigitCounter++;
        }
    }

    public void binOp(String src,String input) {
        if(src.equals("digit"))
            addNumber();
        char o=input.charAt(0);
        if(operatorsStack.isEmpty())
            operatorsStack.push(o);
        else
            addOperator(o);
    }

    public void prO(String src,String input) {
        operatorsStack.push('(');
        prCounter++;
    }

    public void prC(String src,String input) {
        if(src.equals("digit"))
            addNumber();
        addOperator(')');
        prCounter--;
    }

    public void success(String on) {
        if(on.equals("digit"))
            addNumber();

        boolean err=false;
        double res= 0;

        if(prCounter==0) {
            try {
                res = evaluatePostfix();
            } catch (Exception e) {
                err=true;
            }
        } else
            err=true;


        if(!err)
            System.out.println("Result : " + res);
        else
            System.out.println("Syntax error!");
    }

    public void error(String on,String inp) {
        System.out.println("Error on : "+on+" , unexpected : "+inp);
    }

    private double evaluatePostfix() throws Exception{
        while(!operatorsStack.isEmpty())
            postfix.add(operatorsStack.pop());

        Stack<Double> result=new Stack<Double>();

        for(Object o:postfix) {
            if(o instanceof Double) {
                result.push((Double)o);
            } else {
                if(result.size()<2)
                    throw new Exception();
                result.push(applyOperator((Character)o,result.pop(),result.pop()));
            }
        }
        if(result.size()!=1)
            throw new Exception();
        return result.pop();
    }

    private void addOperator(char currentOperator) {
       char topOp=operatorsStack.peek();
       int currentP=getOperatorPrecedence(currentOperator);

        while (!operatorsStack.isEmpty() &&
                currentP<=getOperatorPrecedence(topOp)) {
            operatorsStack.pop();
            if(topOp==')')
                break;
            if(topOp!='(')
                postfix.add(topOp);
            if(!operatorsStack.isEmpty())
                topOp=operatorsStack.peek();

        }

        if(currentOperator!=')')
            operatorsStack.push(currentOperator);
    }

    private int getOperatorPrecedence(char opr){
        switch (opr) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return -1;
        }
    }

    private void addNumber() {
        postfix.add(getCurrentValue());
        resetNumber();
    }

    private void resetNumber() {
        currentValue=0;
        currentFloatDigitCounter =0;
        currentIsFloat =false;
        isCurrentNegative=false;
    }

    private double getCurrentValue() {
        double v=currentValue;
        if(isCurrentNegative)
            v*=-1;
        if(currentIsFloat)
            v/=Math.pow(10, currentFloatDigitCounter);
        return v;
    }

    private static int stringToDigit(String a) {
        if(a.matches("[0-9]"))
            return a.charAt(0)-'0';
        else
            return -1;
    }

    private static double applyOperator(char opr,double a,double b) {
        switch (opr) {
            case '+':
                return a+b;
            case '-':
                return a-b;
            case '*':
                return a*b;
            case '/':
                return a/b;
            default:
                return 0;
        }
    }
}
