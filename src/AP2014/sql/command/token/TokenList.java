package AP2014.sql.command.token;

import AP2014.sql.Resource;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenList {

    private Vector<TokenItem> tokens;
    private int currentToken;
    private Resource resource;
    private boolean isValid;

    public TokenList(Resource resource) {
        this.tokens = new Vector<TokenItem>();
        this.resource = resource;
        this.isValid = true;
        this.currentToken = 0;
    }

    public TokenList(String command, Resource resource) {
        this(resource);

        command=command.toLowerCase();

        Pattern p = Pattern.compile(TokenItem.keywordRegex + "|" +
                TokenItem.symbolRegex + "|" + TokenItem.valueRegex);
        Matcher m = p.matcher(command);
        while (m.find()) {
            String token = command.substring(m.start(), m.end());
            TokenItem t = new TokenItem(token);
            if (t.getType() != TokenItemType.TOKEN_ITEM_TYPE_INVALID)
                tokens.add(t);
            else {
                this.isValid = false;
                resource.logError("Invalid token : " + token);
                break;//Don't continue!
            }
        }


    }

    public boolean isValid() {
        return isValid;
    }

    public boolean checkSequence(String pattern, String... args) {
        return checkSequence(pattern, false, args);
    }

    public boolean checkSequenceAndLog(String pattern, String... args) {
        return checkSequence(pattern, true, args);
    }

    public TokenList getSubsequence() {
        TokenList tl=new TokenList(resource);
        tl.currentToken=0;
        int pCount=0;
        while(true) {
            if(checkSequence("("))
                pCount++;
            else if(checkSequence(")"))
                pCount--;
            else
                tl.tokens.add(getCurrentToken());

            if(pCount<0) {
                resource.logError("Syntax error for parentheses");
                return null;
            } else if(pCount==0) {
                next();
                return tl;
            } //else
                // continue!
            next();
            if(isEndOfList())return tl;
        }
    }


    public boolean checkSequence(String pattern, boolean log, String... args) {
        int tokenindex = currentToken;
        int args_pos = 0;

        for (char c : pattern.toCharArray()) {
            if(tokenindex>=tokens.size())return false;
            TokenItem current = getToken(tokenindex++);
            switch (c) {
                case 'k':
                    if (current.getType() !=
                            TokenItemType.TOKEN_ITEM_TYPE_KEYWORD) {
                        if(log)resource.logError("keyword expected but found " + current);
                        return false;
                    } else if (!current.getText().equals(args[args_pos++])) {
                        if(log)resource.logError("unexpected " + current + " (keyword " + c + "was expected");
                        return false;
                    }
                    break;
                case 'v':
                    if (current.getType() !=
                            TokenItemType.TOKEN_ITEM_TYPE_VALUE) {
                        if(log)resource.logError("value expected but found " + current);
                        return false;
                    }
                    break;
                default:
                    //default goes for symbols
                    if (current.getType() !=
                            TokenItemType.TOKEN_ITEM_TYPE_SYMBOL) {
                        if(log)resource.logError("symbol expected but found " + current);
                        return false;
                    } else if (!current.getText().equals(String.valueOf(c))) {
                        if(log)resource.logError("unexpected " + current + " (symbol " + c + "was expected");
                        return false;
                    }
                    break;
            }
        }
        return true;
    }

    public TokenItem getCurrentToken() {
        if (currentToken >= 0 && currentToken < tokens.size())
            return tokens.get(currentToken);
        else return null;
    }

    public boolean isEndOfList() {
        return getCurrentToken()==null;
    }

    private TokenItem getToken(int index) {
        return tokens.get(index);
    }

    public void next(int count) {
        currentToken += count;
    }

    public void next() {
        next(1);
    }

}


