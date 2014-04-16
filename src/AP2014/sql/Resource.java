package AP2014.sql;

import AP2014.sql.storage.Table;
import java.util.Vector;

public class Resource {

    private StringBuffer logHolder;
    private Vector<Table> tables;

    public Resource() {
        logHolder = new StringBuffer();
        tables = new Vector<Table>();
    }

    public Vector<Table> getTables() {
        return tables;
    }

    public void log(String message, String tag) {
        logHolder.append("[" + tag + "]\t" + message + "\n");

    }

    public void logError(String message) {
        log(message, "ERR");
    }

    public void logWarning(String message) {
        log(message, "WARN");
    }

    public void logInfo(String message) {
        log(message, "INFO");
    }

    @Override
    public String toString() {
        return logHolder.toString();
    }
}