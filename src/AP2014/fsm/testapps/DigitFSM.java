package AP2014.fsm.testapps;

public class DigitFSM {

    public void s_init(){
        System.out.println("Initialized ....");
    }

    public void s_integer(){
        System.out.println("Integer number");
    }

    public void s_float(){
        System.out.println("Float number");
    }

    public void s_error(Object a){
        System.out.println("Input error ! Unexpected :"+a);
    }

}
