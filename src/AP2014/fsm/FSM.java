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

    public FSM(Object bindTo, File file) throws IOException {
        this(bindTo, Utils.readAllFile(file));
    }

    public FSM(Object bindTo, String data) {
        bindedObject = bindTo;
        //Load from data

        String[] rows = data.split("[\r\n]+");
        String[] headerRow = rows[0].split("\t");

        String[] tmp = headerRow[0].split(",");
        states = new FSMState[Integer.parseInt(tmp[0])];
        events = new FSMEvent[Integer.parseInt(tmp[1])];
        transmitions = new int[events.length][states.length];

        for (int e = 0; e < events.length; e++)
            events[e] = new FSMEvent(headerRow[e + 1], e);

        for (int s = 0; s < states.length; s++) {
            String[] cols = rows[s + 1].split("\t");
            states[s] = new FSMState(cols[0], s);
        }

        for (int s = 0; s < states.length; s++) {
            String[] cols = rows[s + 1].split("\t");
            for (int e = 0; e < events.length; e++) {
                FSMState matchedState = findState(cols[e + 1]);
                int matchedCode = matchedState != null ? matchedState.getId() : -1;
                transmitions[e][s] = matchedCode;
            }
        }
    }

    private FSMState findState(String name) {
        for (FSMState s : states)
            if (s != null && s.getName().equals(name))
                return s;
        return null;
    }

    private FSMEvent findEvent(char input) {
        for (FSMEvent e : events)
            if (e.accepts(input + ""))
                return e;
        return null;
    }

    public void run(String input) {
        FSMState currentState = states[0];
        invokeStateAction(currentState.getName());

        for (char i : input.toCharArray()) {
            FSMEvent e = findEvent(i);
            int trans=-1;
            if (e != null)
                trans=transmitions[e.getCode()][currentState.getId()];

            if(trans>0) {
                currentState = states[trans];
                invokeStateAction(currentState.getName(),String.valueOf(i));
            }
            else {
                invokeStateAction("error_"+currentState.getName(),String.valueOf(i));
                invokeStateAction("error",i);
                currentState = null;
            }

            if(currentState==null || currentState.isFinalState())
                break;
        }

    }

    private void invokeStateAction(String name) {
        invokeStateAction(name,null);
    }

    private void invokeStateAction(String name,Object param) {
        invokeAction("s_"+name,param,false);
    }

    private void invokeAction(String name,Object param,boolean verbose) {
        try {
            Method m = bindedObject.getClass().getMethod(name);
            Parameter[] p=m.getParameters();
            if(p.length==1)
                    m.invoke(bindedObject,p[0].getClass().cast(param));
                else
                    m.invoke(bindedObject);
        } catch (Exception e) {
           // if(verbose)
                System.err.println("Failed to call "+name+"()");
        }
    }



}
