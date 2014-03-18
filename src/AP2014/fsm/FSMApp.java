package AP2014.fsm;

import AP2014.fsm.testapps.DigitFSM;
import AP2014.io.Utils;
import org.apache.xml.resolver.apps.resolver;

import java.io.File;
import java.io.IOException;

public class FSMApp {
    public static void main(String[] args) {

        String name="Digit";

        System.out.println(("salam".split("(?!^)")).length);

        String basePath="src\\AP2014\\fsm\\testapps\\";

        //new FSMApp().run(basePath+name+".txt",basePath+name);
    }



    private void run(String tablePath,String className,String[] input) {
        File table=new File(tablePath);

        if(!table.exists() || !table.isFile()) {
            System.err.println("File not exists :"+table);
            return;
        }
        String tableData="";
        try {
            tableData=Utils.readAllFile(table);
        } catch (IOException e) {
            System.err.println("Unable to read file :"+table);
            return;
        }

        Object c=null;
        try {
            c=Class.forName(className);
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found :"+className);
            return;
        }

        FSM fsm=new FSM(c,tableData);
        boolean accpted=fsm.run(input);
        if(accpted)
            System.out.println("State machine accepted this input");
        else
            System.out.println("Input not accepted");


    }
}
