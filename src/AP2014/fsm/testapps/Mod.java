package AP2014.fsm.testapps;

public class Mod {


    public static String generateTable(int devideBy) {
        StringBuilder b = new StringBuilder();

        b.append(devideBy + ",2 0 1\n");
        int counter = 0;
        for (int state = 0; state < devideBy; state++) {
            b.append("*" + state + " ");
            b.append(counter++ % devideBy + " ");
            b.append(counter++ % devideBy);
            b.append("\n");
        }

        return b.toString();
    }

    public void doEvent(String state, Object... params) {
        if (state.equals("success")) {
            System.out.println("Finished on : " + params[0].toString());
        }
    }
}
