package AP2014.sql.command.token;

enum TokenItemType {
    TOKEN_ITEM_TYPE_VALUE,
    TOKEN_ITEM_TYPE_KEYWORD,
    TOKEN_ITEM_TYPE_SYMBOL,
    TOKEN_ITEM_TYPE_INVALID
}

public class TokenItem {

    static final String keywordRegex="[^'\"\\s]\\S+";
    static final String valueRegex="'.*?'|\".*?\"";
    static final String symbolRegex="[=();,]";
    static final String[] validKeywords={
            "create","table","drop","insert","into",
            "parameter","values","from","set","where",
            "and","or","select","delete","int","float","string"
    };

    private String text;
    private TokenItemType type;

    public TokenItem(String text) {
        this.text=text;
        if(text.matches(keywordRegex) && isValidKeyword(text))
            this.type=TokenItemType.TOKEN_ITEM_TYPE_KEYWORD;
        else if(text.matches(valueRegex)) {
            this.type=TokenItemType.TOKEN_ITEM_TYPE_VALUE;
            text=text.replace("^['\"]|['\"]$","");
        }
        if(text.matches(symbolRegex))
            this.type=TokenItemType.TOKEN_ITEM_TYPE_SYMBOL;
        else
            this.type=TokenItemType.TOKEN_ITEM_TYPE_INVALID;

    }

    public String getText() {
        return text;
    }

    public TokenItemType getType() {
        return type;
    }

    private static boolean isValidKeyword(String text) {
        for(String k:validKeywords)
            if(k.equals(text))
                return true;
        return false;
    }



}
