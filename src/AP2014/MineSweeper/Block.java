package AP2014.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


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
    private Component parent;

    private boolean isPressed=false;
    private boolean doubleClicked =false;


    enum NextActionType {
        NEXT_ACTION_TYPE_NONE,
        NEXT_ACTION_TYPE_OPEN,
        NEXT_ACTION_TYPE_FLAG
    }
    private NextActionType nextActionType = NextActionType.NEXT_ACTION_TYPE_NONE;



    public Block(boolean showQuestion, final int x, final int y, final Component parent) {

        if (showQuestion)
            displayStateCount = 3;
        else
            displayStateCount = 2;

        this.x = x;
        this.y = y;

        setSize(16, 16);
        setBackground(null);
        setBorder(null);

        this.parent=parent;

        enableEvents(AWTEvent.MOUSE_EVENT_MASK);

    }

    @Override
    protected void processEvent(AWTEvent e) {
        super.processEvent(e);

        if(e instanceof MouseEvent)
            onMouseEvent((MouseEvent)e);
        else if(e instanceof KeyEvent)
            onKeyEvent((KeyEvent)e);
        else if(e instanceof BlockEvent)
            onBlockEvent((BlockEvent)e);
    }

    private void onBlockEvent(BlockEvent e) {
        switch (e.getID()) {
            case BlockEvent.BLOCK_SHOWBLOCK:
                if(isShown || isFlagged())
                    return;
                setShown(true);
                if (getBombs() != 0)
                    return;
                for(Block b:(ArrayList<Block>)e.arg0)
                    parent.dispatchEvent(BlockEvent.showBlockRequest(b));
                break;
        }
    }


    private void onKeyEvent(KeyEvent e) {
       if (e.getID() != KeyEvent.KEY_RELEASED)
            return;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                nextActionType = NextActionType.NEXT_ACTION_TYPE_FLAG;
                break;
            case KeyEvent.VK_ENTER:
                nextActionType = NextActionType.NEXT_ACTION_TYPE_OPEN;
                break;
            default:
                nextActionType = NextActionType.NEXT_ACTION_TYPE_NONE;
        }

        doAction();
        updateUI();
    }

    private void onMouseEvent(MouseEvent e) {

        boolean btn1= (e.getModifiersEx() &
                MouseEvent.BUTTON1_DOWN_MASK)!=0;
        boolean btn3= (e.getModifiersEx() &
                MouseEvent.BUTTON3_DOWN_MASK)!=0;

        isPressed=false;

        if(!contains(e.getPoint())) {
            nextActionType = NextActionType.NEXT_ACTION_TYPE_NONE;
            parent.dispatchEvent(BlockEvent.mouseChange(this, false));
        } else if(btn1) {
            if((isBomb() || bombs!=0)&&!isFlagged())
                parent.dispatchEvent(BlockEvent.mouseChange(this,true));
            isPressed = true;
            doubleClicked =e.getClickCount()==2;
            nextActionType = NextActionType.NEXT_ACTION_TYPE_OPEN;
        }else if(btn3) {
            nextActionType = NextActionType.NEXT_ACTION_TYPE_FLAG;
        } else {
            //Mouse is up
            doAction();
            parent.dispatchEvent(BlockEvent.mouseChange(this,false));
        }
        updateUI();
    }

    private void doAction() {
        switch (nextActionType) {
            case NEXT_ACTION_TYPE_FLAG:
                //change state
                if(!isShown()) {
                    displayState++;
                    displayState %= displayStateCount;
                    parent.dispatchEvent(BlockEvent.update(this));
                }
                break;
            case NEXT_ACTION_TYPE_OPEN:
                //show
                if(!isFlagged())
                    if(!isShown() || doubleClicked) {
                        parent.dispatchEvent(BlockEvent.blockClicked(this));
                    }
                break;
        }
        nextActionType = NextActionType.NEXT_ACTION_TYPE_NONE;
        requestFocus();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        BufferedImage i=null;

        if(!isShown) {
            if (!isPressed) {
                //Normal state
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
                i=GameResource.buttonIcons[displayIcon];
            }else //Pressed state
                i=GameResource.buttonIcons[7];

        } else {
            //Shown state
            if(!bomb) {
                //Empty
                i=GameResource.mineCounters[bombs];
            } else {
                //Bomb
                i=GameResource.buttonIcons[bombState];
            }

        }


        if(i!=null)
            g.drawImage(i,0,0,this);
    }


    public int getBombs() {
        return bombs;
    }

    public void setBombs(int bombs) {
        this.bombs = bombs;
        repaint();
    }

    public boolean isBomb() {
        return bomb;
    }

    public void setBomb() {
        bomb = true;
        repaint();
    }

    public void setBombState(int bombState) {
        this.bombState = bombState;
    }

    public void setShown(boolean shown) {
        isShown = shown;
       repaint();
    }

    public boolean isShown() {
        return isShown;
    }

    public boolean isFlagged() {
        return (displayState==DISPLAY_STATE_FLAG);
    }

    public int getDisplayState() {
        return displayState;
    }

}
