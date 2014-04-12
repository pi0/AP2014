package AP2014.minesweeper;

import javax.swing.*;


public class Block extends JButton {

    private static final int DISPLAY_STATE_INVISIBLE = 1;
    private int displayState = DISPLAY_STATE_INVISIBLE;
    private static final int DISPLAY_STATE_FLAG = 2;
    private static final int DISPLAY_STATE_QUESTION = 3;
    private int displayStateCount = 3;
    private int bombs = 0;
    private boolean bomb = false;

    public Block(boolean showQuestion) {
        if (!showQuestion)
            displayStateCount--;

        setSize(25, 25);
    }


    public int getBombs() {
        return bombs;
    }

    public void setBombs(int bombs) {
        this.bombs = bombs;
        if (!isBomb())
            setText(bombs + "");
        else
            setText("B");
    }

    public boolean isBomb() {
        return bomb;
    }

    public void setBomb() {
        bomb = true;
    }


}
