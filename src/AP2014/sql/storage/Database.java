package AP2014.sql.storage;

import java.util.Vector;

import AP2014.sql.storage.cell.AbstractCell;
import AP2014.sql.storage.cell.MetaCell;

public class Database {

    private String name;
    private Vector<Table> tables;

    public Database(String name) {
        tables=new Vector<Table>();
        this.name=name;
    }

    public void addTable(Table table) {
        tables.add(table);
    }

    public Table getTable(String name) {
        for(Table table:tables)
            if(table.getName().equals(name))
                return table;
        return null;
    }
    
    public String dump() {
    	StringBuilder sb=new StringBuilder();
 
    	for(Table table:tables) {
    		sb.append(table.dump());
    		sb.append("\n");
    	}
    	return sb.toString();
    }

	public void drop(Table table) {
		tables.remove(table);
	}
	
	public String getName() {
		return name;
	}


}
