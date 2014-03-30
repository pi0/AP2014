package AP2014.fsm;

public class FSMEvent {
    private String trigger;//is a regex
    private int code;
    public FSMEvent(String trigger,int code) {
        this.trigger = trigger;
        this.code=code;
    }

    public int getCode() {
        return code;
    }

    public boolean accepts(String input){
        return input.matches(trigger);
    }
}
