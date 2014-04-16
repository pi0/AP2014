package AP2014.sql;

import AP2014.io.Utils;
import java.io.File;
import java.io.IOException;

public class SQLApp {

    public static void main(String[] args) throws IOException {
        SQL sql=new SQL();
        Resource r=sql.query(Utils.readAllFile
                (new File("src\\AP2014\\sql\\testQuery.txt")));

        System.out.println(r);
        
    }

}
