package AP2014.minesweeper;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Block extends JButton {

    public static final int DISPLAY_STATE_NORMAL = 0;
    private int displayState =
            DISPLAY_STATE_NORMAL;
    public static final int DISPLAY_STATE_FLAG = 1;
    public static final int DISPLAY_STATE_QUESTION = 2;
    public static final int BOMB_STATE_NOTFOUND = 4;
    private int bombState =
            BOMB_STATE_NOTFOUND;
    public static final int BOMB_STATE_FOUND = 3;
    public static final int BOMB_STATE_BOOM = 2;
    public final int x, y;
    private int displayStateCount;
    private int bombs = 0;
    private boolean bomb = false;
    private boolean isShown = false;

    public Block(boolean showQuestion, final int x, final int y, final MineSweeper parent) {

        if (showQuestion)
            displayStateCount = 3;
        else
            displayStateCount = 2;

        this.x = x;
        this.y = y;

        setSize(16, 16);
        setBackground(null);
        setBorder(null);
        setFocusable(false);

        setPressedIcon(GameResource.buttonIcons[7]);
        update();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isShown()) {
                    e.consume();
                    return;
                }

                if (e.getButton() == MouseEvent.BUTTON3) {
                    changeState();//Right click
                    parent.updateStates();
                } else if (displayState == DISPLAY_STATE_NORMAL)
                    parent.BlockClicked(x, y); //Left click

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (!isShown())
                    parent.setBlockPressed(true);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                parent.setBlockPressed(false);
            }
        });


    }

    @Override
    public void setIcon(Icon defaultIcon) {
        super.setIcon(defaultIcon);
        setDisabledIcon(defaultIcon);
    }

    private void changeState() {
        displayState++;
        displayState %= displayStateCount;

        update();
    }


    private void update() {

        if (!isShown) {
            int displayIcon = 0;
            switch (displayState) {
                case DISPLAY_STATE_NORMAL:
                    displayIcon = 0;
                    break;
                case DISPLAY_STATE_FLAG:
                    displayIcon = 1;
                    break;
                case DISPLAY_STATE_QUESTION:
                    displayIcon = 5;
                    break;
            }
            setIcon(GameResource.buttonIcons[displayIcon]);
        } else {
            if (isBomb()) {
                setIcon(GameResource.buttonIcons[bombState]);
            } else {
                setIcon(GameResource.mineCounters[bombs]);
            }
        }

    }


    public int getBombs() {
        return bombs;
    }

    public void setBombs(int bombs) {
        this.bombs = bombs;
        update();
    }

    public boolean isBomb() {
        return bomb;
    }

    public void setBomb() {
        bomb = true;
        update();
    }

    public void setBombState(int bombState) {
        this.bombState = bombState;
    }

    public void setShown() {
        isShown = true;
        setEnabled(false);
        update();
    }

    public boolean isShown() {
        return isShown;
    }

    public int getDisplayState() {
        return displayState;
    }
}
