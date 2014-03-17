package AP2014.fsm;

import AP2014.fsm.testapps.DigitFSM;

import java.io.File;

public class FSMApp {
    public static void main(String[] args) {
        try {
            new FSMApp().run();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error: "+e.getMessage());
        }
    }

    private void run() throws Exception{
        String fsmPath="src\\AP2014\\fsm\\testapps\\Digit.txt";
        FSM fsm=new FSM(new DigitFSM(),new File(fsmPath));
        fsm.run("12.dfdf\n");

    }
}
