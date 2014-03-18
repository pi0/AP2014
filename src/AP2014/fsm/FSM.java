package AP2014.fsm;

import AP2014.io.Utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

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

        for (int s = 0; s < states.length; s++) {
            String[] cols = rows[s + 1].split(splitterRegex);
            for (int e = 0; e < events.length; e++) {
                FSMState matchedState = findState(cols[e + 1]);
                int matchedCode = matchedState != null ? matchedState.getId() : -1;
                transmitions[e][s] = matchedCode;
            }

        }
    }



    public boolean run(String[] input) {
        FSMState currentState = states[0];

        for (int c=0;c<=input.length;c++) {
            String i=input[c<input.length?c:input.length-1];
            int trans=-1;
            if(c<input.length) {
                FSMEvent e = findEvent(i);
            if (e != null )
                trans=transmitions[e.getCode()][currentState.getId()];
            }

            if(trans>0) {
                currentState = states[trans];
                invokeAction("s_" + currentState.getName(), i);
            }
            else {
                //It's end of the line bro!
                if(currentState.isFinalState()) {
                    invokeAction("finish_" + currentState.getName(), i);
                    invokeAction("finish", currentState.getName());
                    return true;
                } else {
                    invokeAction("error_" + currentState.getName(), i);
                    invokeAction("error", currentState.getName());
                    return false;
                }
            }
        }
        return false;//Wont happen !!
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


    private void invokeAction(String name,String param) {
        boolean s=false;
        try {
            Method m = bindedObject.getClass().getMethod(name,String.class);
            s=true;
            m.invoke(bindedObject,param);
        } catch (Exception e) {}
        if(!s) try {
            Method m = bindedObject.getClass().getMethod(name);
            m.invoke(bindedObject);
        } catch (Exception e) {}
    }



}
