package AP2014.fsm;

import java.lang.reflect.Method;

public class FSM {

    private int[][] transmitions;
    private FSMEvent[] events;
    private FSMState[] states;
    private Object bindedObject;

    public FSM(Object bindTo, String data) {
        bindedObject = bindTo;
        String splitterRegex="\\s+";

        //Load from data
        String[] rows = data.split("[\r\n]+");
        String[] headerRow = rows[0].split(splitterRegex);

        String[] tmp = headerRow[0].split(",");
        states = new FSMState[Integer.parseInt(tmp[0])];
        events = new FSMEvent[Integer.parseInt(tmp[1])];
        transmitions = new int[events.length][states.length];

        for (int e = 0; e < events.length; e++)
            events[e] = new FSMEvent(headerRow[e + 1], e);

        for (int s = 0; s < states.length; s++) {
            String[] cols = rows[s + 1].split(splitterRegex);
            states[s] = new FSMState(cols[0], s);
        }

        int err_num=-1;
        for (int s = 0; s < states.length; s++) {
            String[] cols = rows[s + 1].split(splitterRegex);
            for (int e = 0; e < events.length; e++) {
                FSMState matchedState = findState(cols[e + 1]);
                int matchedCode = matchedState != null ? matchedState.getId() : err_num--;
                transmitions[e][s] = matchedCode;
            }

        }
    }

    public boolean run(String[] inputs) {

        FSMState currentState = states[0];
        FSMState lastState=currentState;
        String lastInput="";

        for(String input:inputs) {
            FSMEvent e = findEvent(input);
            int trans=transmitions[e.getCode()][currentState.getId()];

            if(trans>=0) {
                lastState=currentState;
                currentState = states[trans];
                invokeAction(currentState.getName(), lastState.getName(),input);
            } else {
                //It's end of the line , bro!
                lastInput=input;
                break;
            }
        }

        boolean accepted=false;
        if(currentState.isFinalState()) {
            invokeAction("success", currentState.getName());
            accepted=true;
        } else {
            invokeAction("error", currentState.getName(),lastInput);
            accepted=false;
        }
        return accepted;
    }

    private FSMState findState(String name) {
        for (FSMState s : states)
            if (s.getName().equals(name))
                return s;
        return null;
    }

    private FSMEvent findEvent(String input) {
        for (FSMEvent e : events)
            if (e.accepts(input))
                return e;
        return null;
    }


    private void invokeAction(String name,Object... params) {
        try {
            Class[] classes=new Class[params.length];
            for(int i=0;i<params.length;i++)
                classes[i]=params[i].getClass();
            Method m = bindedObject.getClass().getMethod(name,classes);
            m.invoke(bindedObject,params);
        } catch (Exception e) {
            System.err.println("Error on invoking "+name);}
    }


}
