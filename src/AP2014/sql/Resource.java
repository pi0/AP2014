package AP2014.sql;

public class Resource {

    private StringBuffer logHolder;

    public Resource() {
        logHolder=new StringBuffer();
    }

    public void logError(String message) {
        logHolder.append("[ERR] "+message+"\n");
    }

    public void logWarning(String message) {
        logHolder.append("[WARN] "+message+"\n");
    }


}
