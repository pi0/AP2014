package AP2014.sql.storage;

import java.util.Vector;

public class Database {

    private String name;
    private Vector<Table> tables;

    public Database(String name) {
        tables=new Vector<Table>();
    }

    public void addTable(Table table) {
        tables.add(table);
    }


}
