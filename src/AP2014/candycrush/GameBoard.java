package AP2014.candycrush;

import AP2014.io.MessageBox;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


public class GameBoard extends JPanel implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 8369084705122293774L;
	
	private final static int spacer = 5;
    private final static int blockWidth = 30;
    private final static int blockHeight = 30;
    private Block[][] blocks;
    private Block selectedBlock1, selectedBlock2;
    private int removeBlocks;


    private int level=0;

    private Timer timer;
    private int totalTime;
    private int elapsedTime;


    public GameBoard(int rows, int cols) {
        this.blocks = new Block[cols][rows];
        this.level=1;
        addMouseListener(this);
        addMouseMotionListener(this);
        timer=new Timer(100,new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                elapsedTime+=timer.getDelay();
                if(elapsedTime>totalTime){

                    if(MessageBox.AskQuestion("Try again ?","Game over!"))
                        newGame();
                    else
                        System.exit(0);

                }
                repaint();
            }
        });
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        //newGame();
    }

    public void newGame() {

        selectedBlock1 = selectedBlock2 = null;
        this.blocks = new Block[getCols()][getRows()];
        this.removeBlocks=0;
        fillBoard();
        checkForConnectedBlocks();

        totalTime=20000-level*2000;
        timer.restart();
        elapsedTime=0;
    }




    private void fillBoard() {
        Random random = new Random();
        for (int c = 0; c < getCols(); c++)
            for (int r = 0; r < getRows(); r++) {
                if (blocks[c][r] == null) {
                    do{
                    blocks[c][r] = new Block((char) ('a' + random.nextInt('j' - 'a')), c, r);
                    } while(getConnectedBlocks(blocks[c][r]).length>=3);
                }
            }
        repaint();
    }

    private void checkForConnectedBlocks() {
        //Find and remove connected blocks
        boolean changed = false;
        for (int c = 0; c < getCols(); c++)
            for (int r = 0; r < getRows(); r++) {
                for (Block b : getConnectedBlocks(blocks[c][r])) {
                    blocks[b.getX()][b.getY()] = null;
                    changed = true;
                    this.removeBlocks++;
                    if(removeBlocks>30+20*level) {
                        removeBlocks=0;
                        level++;
                    }

                }
            }
        if (!changed) return;
        //Fall down
        while (changed) {
            changed = false;
            for (int r = getRows() - 2; r >= 0; r--) {
                for (int c = 0; c < getCols(); c++) {
                    if (blocks[c][r] == null) continue;
                    if (blocks[c][r + 1] == null) {
                        //Move down
                        blocks[c][r + 1] = blocks[c][r];
                        Block b=blocks[c][r + 1];
                        b.setY(b.getY()+1);
                        blocks[c][r] = null;
                        changed = true;

                    }
                }
            }
            elapsedTime=0;
            repaint();
        }
        fillBoard();
        checkForConnectedBlocks();
    }

    private Block[] getConnectedBlocks(Block b) {

        if (b == null) return new Block[0];

        final int[][][] directions = {{{-1, 0}, {+1, 0}}, {{0, -1}, {0, +1}}};

        Vector<Block> connectedBlocks = new Vector<Block>();

        for (int[][] direction : directions) {
            Vector<Block> dirConnectedBlocks = new Vector<Block>();
            for (int[] delta : direction) {
                int c = b.getX() + delta[0];
                int r = b.getY() + delta[1];
                while (isInRange(c, r) && blocks[c][r] != null && blocks[c][r].isJoinableWith(b)) {
                    dirConnectedBlocks.add(blocks[c][r]);
                    c += delta[0];
                    r += delta[1];
                }
                if (dirConnectedBlocks.size() >= 2)
                    connectedBlocks.addAll(dirConnectedBlocks);
            }
        }

        if (connectedBlocks.size() > 0) {
            connectedBlocks.add(b);
            return connectedBlocks.toArray(new Block[connectedBlocks.size()]);
        } else
            return new Block[0];
    }

    private boolean isInRange(int x, int y) {
        return (x >= 0 && y >= 0 && x < getCols() && y < getRows());
    }

    public int getCols() {
        return blocks[0].length;
    }

    private int getRows() {
        return blocks.length;
    }

    private void swapBlocks(Block a, Block b) {
        a.swapWith(b);
        if (getConnectedBlocks(a).length + getConnectedBlocks(b).length == 0) {
            //Revert it!!
            a.swapWith(b);
        } else
            checkForConnectedBlocks();
    }

    ///==========================
    // GUI
    ///==========================

    @Override
    public void paint(Graphics sg) {
        //super.paint(g);
        Graphics2D g = (Graphics2D) sg;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(Color.gray);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setFont(g.getFont().deriveFont(15));

        for (int r = 0; r < getRows(); r++)
            for (int c = 0; c < getCols(); c++) {

                if (blocks[c][r] == null) continue;

                int sx = (c + 1) * spacer + c * blockWidth;
                int sy = (r + 1) * spacer + r * blockHeight;

                if (blocks[c][r].equals(selectedBlock1) ||
                        blocks[c][r].equals(selectedBlock2))
                    g.setColor(blocks[c][r].getColor().darker().darker().darker());
                else
                    g.setColor(blocks[c][r].getColor());
                g.fillRoundRect(sx, sy, blockWidth, blockHeight, 10, 10);

                g.setColor(Color.BLACK);
                g.drawString(("" + blocks[c][r].getValue()).toUpperCase(), sx + blockWidth / 2 - 3, sy + blockHeight / 2 + 3);
            }

            g.setColor(new Color(0,0,0,120));
            g.fillRect(0, getHeight() - 20, getWidth(), getHeight() - 20);

            g.setColor(new Color(250,0,0,200));
            g.fillRect(0, getHeight() - 5, (int)(getWidth()*((double)elapsedTime/totalTime)), getHeight() - 5);
            g.setColor(Color.GREEN);
            g.setFont(g.getFont().deriveFont(Font.BOLD,15));
            g.drawString("Level: "+level+"   Score: " + this.removeBlocks + "/" + (30+20*level), 10, getHeight()-8);

    }

    public void mouseClicked(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {
        selectedBlock1 = getBlock(e.getX(), e.getY());
        repaint();
    }

    public void mouseReleased(MouseEvent e) {
        if (selectedBlock2 != null && selectedBlock1 != null)
            swapBlocks(selectedBlock1, selectedBlock2);

        selectedBlock1 = selectedBlock2 = null;
        repaint();
    }

    public void mouseEntered(MouseEvent e) {
        timer.start();
    }

    public void mouseExited(MouseEvent e) {
        timer.stop();
    }

    private Block getBlock(int x, int y) {
        int c = getIndex(x, blockWidth, spacer);
        int r = getIndex(y, blockHeight, spacer);
        if (c < getCols() && c >= 0 && r < getRows() && r >= 0)
            return blocks[c][r];
        else return null;
    }

    private int getIndex(int location, int length, int spacer) {
        int region = (location) / (length + spacer);
        return region;
    }


    public void mouseDragged(MouseEvent e) {
        if (selectedBlock1 != null) {
            selectedBlock2 = getBlock(e.getX(), e.getY());
            validateSelectedBlocks();
            repaint();
        }
    }

    private void validateSelectedBlocks() {
        if (selectedBlock1 != null && selectedBlock2 != null) {
            int totalDiff = Math.abs(selectedBlock1.getX() - selectedBlock2.getX()) +
                    Math.abs(selectedBlock1.getY() - selectedBlock2.getY());
            if (totalDiff != 1)
                selectedBlock2 = null;
        }
    }

    public void mouseMoved(MouseEvent e) {}
}


