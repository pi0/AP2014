package AP2014.infixpostfix;

import java.util.regex.Pattern;

public class Postfix {

    private final static String matcherRegex="[\\+\\*/\\-]{1}|[a-z]{1}|[\\(\\)]{1}|[\\d]+|\\d*\\.\\d+";

    public Postfix(String eq){
        Pattern pattern=Pattern.compile(matcherRegex);
        String[] matches=pattern.split(eq);
        for(String m:matches)
           System.out.println(m);
    }

}
