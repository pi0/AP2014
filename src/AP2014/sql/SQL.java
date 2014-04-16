package AP2014.sql;

import AP2014.io.Utils;
import AP2014.sql.command.Command;
import AP2014.sql.storage.Database;

import java.io.File;
import java.io.IOException;

public class SQL {

    private Database db;

    public SQL() {
        db=new Database("default db");
    }

    public static boolean isValidName(String name) {
        return (name.matches("[a-zA-Z]\\w*") &&
                (!(name.matches("\\d{3,}"))) );
    }

    public Database getDb() {
        return db;
    }

    public Resource query(String query) {
        Resource r=new Resource();
        Command.query(query,db,r);
        return r;
    }

    public void importFromFile(File f) {
        try {
            query(Utils.readAllFile(f));
        } catch (IOException e) {
           // e.printStackTrace();
        }
    }

    public void exportToFile(File f) {
        try {
            Utils.writeAllFile(f,db.dump());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
