package AP2014.sql;

import java.util.Vector;

public class Database {

    private String name;
    private Vector<Table> tables;

    public Database(String name) {
        this.tables = new Vector<Table>();
        this.name = name;
    }

    public int size() {
        return tables.size();
    }

    public Table getTable(int index) {
        return tables.get(index);
    }

    public Table getTable(String name) {
        for(Table table:tables)
            if(table.getName().equals(name))
                return table;
        return null;
    }



    public void dropTable(Table table) {
        //TODO : drop table
    }


}
