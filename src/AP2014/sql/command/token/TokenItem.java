package AP2014.sql.command.token;

enum TokenItemType {
    TOKEN_ITEM_TYPE_VALUE,
    TOKEN_ITEM_TYPE_KEYWORD,
    TOKEN_ITEM_TYPE_SYMBOL,
    TOKEN_ITEM_TYPE_INVALID
}

public class TokenItem {

	static final String symbolRegex="[\\*()<>#=;,]";
    static final String valueRegex="'.*?'|\".*?\"|[a-zA-Z0-9_\\.]+";
    static final String keywordRegex;
    static final String[] validKeywords={
        "create","table","drop","insert","update","into",
        "parameter","values","from","set","where",
        "and","or","select","delete","int","float","string"
    };
    static {
    	StringBuffer b=new StringBuffer();
    //	b.append("[^'\"\\s]");
    	for(String keyword:validKeywords) {
    		b.append(keyword);
    		b.append("|");
    	}
    	keywordRegex=b.substring(0,b.length()-1);
    }
    
    private String text;
    private TokenItemType type;

    public TokenItem(String text) {
        this.text=text;
        if(text.matches(symbolRegex))
            this.type=TokenItemType.TOKEN_ITEM_TYPE_SYMBOL;
        else if(text.matches(keywordRegex))// && isValidKeyword(text))
            this.type=TokenItemType.TOKEN_ITEM_TYPE_KEYWORD;
        else if(text.matches(valueRegex)) {
            this.type=TokenItemType.TOKEN_ITEM_TYPE_VALUE;
            this.text=text.replaceAll("^['\"]|['\"]$", "");
        }
        else this.type=TokenItemType.TOKEN_ITEM_TYPE_INVALID;

    }

    public String getText() {
        return text;
    }

    public TokenItemType getType() {
        return type;
    }

    /*
    private static boolean isValidKeyword(String text) {
        for(String k:validKeywords)
            if(k.equals(text))
                return true;
        return false;
    }
    */

    public String toString() {
    	//return text;
    	
        String typeStr="";
        switch (type) {
            case TOKEN_ITEM_TYPE_KEYWORD:typeStr="keyword";break;
            case TOKEN_ITEM_TYPE_SYMBOL:typeStr="symbol";break;
            case TOKEN_ITEM_TYPE_VALUE:typeStr="value";break;
            default: typeStr="??";
        }
        return typeStr