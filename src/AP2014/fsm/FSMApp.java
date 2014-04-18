package AP2014.fsm;

import AP2014.io.Utils;

import java.io.File;
import java.io.IOException;

public class FSMApp {
    public static void main(String[] args) {

        String name="Calc";
        String basePath="src\\AP2014\\fsm\\testapps\\";
        String baseClass="AP2014.fsm.testapps.";

        new FSMApp().run(basePath+name+".txt",
                baseClass+name, strToStrArr("4*2+3"));
    }

    private static String[] strToStrArr(String s) {
        return charArrToStrArr(s.toCharArray());
    }
    private static String[] charArrToStrArr(char[] ca) {
        String[] sa=new String[ca.length];
        for(int i=0;i<ca.length;i++)
            sa[i]=ca[i]+"";
        return sa;
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

        Class c=null;
        try {
            c=Class.forName(className);
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found :"+className);
            return;
        }

        Object cInstance=null;
        try {
           cInstance=c.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        FSM fsm=new FSM(cInstance,tableData);
        boolean accpted=fsm.run(input);
        if(accpted)
            System.out.println("State machine accepted this input");
        else
            System.out.println("Input not accepted");


    }
}
