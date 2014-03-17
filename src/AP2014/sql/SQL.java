package AP2014.sql;

import java.util.Vector;

public class SQL {

    private Vector<Database> databases;

    public SQL() {
        databases=new Vector<Database>();
    }

    public void query(String query) {//TODO : query result ?!

    }

    public int size() {
        return databases.size();
    }

    public Database getDB(int index) {
        return databases.get(index);
    }

    public void dropDatabase(Database database) {
        //TODO : remove database

    }

}
