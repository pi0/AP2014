package AP2014.fsm;

public class FSMState {
    private String name;
    private int    id;
    private boolean isFinalState;

    public boolean isFinalState() {
        return isFinalState;
    }

    public FSMState(String name, int id) {
        this.name = name;
        this.id = id;
        if(name.startsWith("*")) {
            isFinalState=true;
            this.name=name.substring(1);
        }
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        return (isFinalState?"*":"")+name;
    }
}
