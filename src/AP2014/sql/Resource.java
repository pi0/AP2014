package AP2014.sql;

import java.io.PrintStream;

public class Resource {

    private StringBuffer logHolder;

    public Resource() {
        logHolder=new StringBuffer();
    }

    public void log(String message,String tag) {
        logHolder.append("["+tag+"]\t"+message+"\n");
    }

    public void logError(String message) {
        log(message,"ERR");
    }

    public void logWarning(String message) {
        log(message, "WARN");
    }

    public void logInfo(String message) {
        log(message, "INFO");
    }


    public void dump(PrintStream stream) {
        stream.print(logHolder);
    }


}
