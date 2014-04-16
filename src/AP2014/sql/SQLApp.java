package AP2014.sql;

import AP2014.indexer.v2.MyScanner;
import AP2014.sql.storage.Table;

import java.io.File;
import java.io.IOException;

public class SQLApp {



    public static void main(String[] args) throws IOException {
        SQL sql=new SQL();
        File db=new File("src\\AP2014\\sql\\DB.dump.txt");
        sql.importFromFile(db);

        if(args.length>0)
            for(String c:args)
                System.out.println(sql.query(c));
        MyScanner s=new MyScanner(System.in);

        String c="";
        while (c!="exit") {

            for(Table t:sql.getDb().tables)
                System.out.println(t);

            System.out.print("PooyaDB> ");
            c=s.readString("\r\n");
            if(c.toLowerCase().startsWith("exit"))
                break;
            System.out.println(sql.query(c));
        }

        sql.exportToFile(db);
    }

}
