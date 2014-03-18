package AP2014.sql;

import AP2014.sql.command.Command;
import AP2014.sql.storage.Database;

import java.util.Vector;

public class SQL {

    private Database db;

    public SQL() {
        db=new Database("default db");
    }

    public static boolean isValidName(String name) {
        return (name.matches("^[a-zA-Z]\\w*$") &&
              !(name.matches("\\d{3,}")));//TODO : why doesn't works ?!
    }

    public Database getDb() {
        return db;
    }

    public Resource query(String query) {
        Resource r=new Resource();
        Command.query(query,db,r);
        return r;
    }

}
