package AP2014.minesweeper;

import javafx.application.Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;

public class MineSweeper extends JFrame {

    private Block[][] blocks;
    private int width, height;
    private SevenSegment score,time;
    private Smilli smilli;

    private JMenuBar menuBar;
    private JMenu menuGame;
    private JMenuItem menuNewGame;
    private JMenuItem menuExit;

    private StartGame startGame=new StartGame();

    private Timer timer=new Timer(1000,new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
          time.setValue(time.getValue()+1);
        }
    });

    public MineSweeper() {
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Mine sweeper!");
    }

    public static void main(String[] args) {
        new MineSweeper().open();
    }

    void open() {
        setVisible(true);
        newGame();
    }

    void newGame() {

        timer.stop();

        if(!startGame.open())
            return;

        this.width = startGame.cols();
        this.height = startGame.rows();
        int mines = startGame.mines();
        boolean showQuestion = false;//TODO

        getContentPane().removeAll();

        setBackground(Color.gray);


        setJMenuBar(menuBar = new JMenuBar());
        menuBar.add(menuGame=new JMenu("Game"));
        menuGame.add(menuNewGame=new JMenuItem("New game"));
        menuGame.add(menuExit=new JMenuItem("Exit"));


        menuNewGame.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newGame();
            }
        });

        menuExit.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
               System.exit(0);
            }
        });

        blocks = new Block[width][height];
        JPanel c = new JPanel(null);

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                blocks[x][y] = new Block(showQuestion,x,y,this);
                blocks[x][y].setLocation(x * blocks[x][y].getWidth(), y * blocks[x][y].getHeight());
                c.add(blocks[x][y]);
            }

        Random r = new Random();
        for (int i = 0; i < mines; i++) {
            int x = r.nextInt(width);
            int y = r.nextInt(height);
            while (blocks[x][y].isBomb()) {
                x = r.nextInt(width);
                y = r.nextInt(height);
            }
            blocks[x][y].setBomb();
        }

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                blocks[x][y].setBombs(getBlockNeighbours(x, y, true).size());

        c.setSize(width * blocks[0][0].getWidth() + 26,
                  height * blocks[0][0].getHeight() + 70);
        c.setLocation(10, 50);
        setSize(c.getWidth() + c.getX(), c.getHeight() + c.getY());

        getContentPane().add(c);


        score=new SevenSegment();
        score.setLocation(10,3);
        score.setValue(mines);
        getContentPane().add(score);

        time=new SevenSegment();
        time.setLocation(getWidth()-time.getWidth()-25,3);
        getContentPane().add(time);

        smilli=new Smilli();
        smilli.setLocation((getWidth()-smilli.getWidth())/2,10);
        getContentPane().add(smilli);

        smilli.setIcon(0);
        getInsets().set(0, 0, 0, 0);
        setLocationRelativeTo(null);
        repaint();
    }

    public void BlockClicked(int x,int y) {

        timer.start();

        if(blocks[x][y].isBomb()) {
            blocks[x][y].setBombState(Block.BOMB_STATE_BOOM);
            gameOver();
            return;
        }
        showBlocks(blocks[x][y]);
    }

    private void showBlocks(Block base) {

        if(base.isBomb() || base.isShown())
            return;

        base.setShown();

        if(base.getBombs()!=0)
            return;


       for(Block block:getBlockNeighbours(base.x,base.y,false))
           showBlocks(block);

    }

    private void gameOver() {
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                if(blocks[x][y].isBomb() &&
                        blocks[x][y].getDisplayState()==Block.DISPLAY_STATE_FLAG)
                    blocks[x][y].setBombState(Block.BOMB_STATE_FOUND);
                blocks[x][y].setShown();
            }
        smilli.setIcon(3);
        timer.stop();
    }

    public void setClicking(boolean isClicking) {
            smilli.setIcon(isClicking?2:0);
    }

    public boolean flagTrriger(boolean value) {
        if(value==false) {
            score.setValue(score.getValue()+1);
        } else {
            if(score.getValue()>0)
                score.setValue(score.getValue()-1);
            else
                return false;
        }
        return true;
    }


    private ArrayList<Block> getBlockNeighbours(int x, int y, boolean shouldBeBomb) {
        ArrayList<Block> r = new ArrayList<Block>();
        for (int dx = -1; dx < 2; dx++)
            for (int dy = -1; dy < 2; dy++) {
                if (dx == 0 && dy == 0) continue;
                if (x + dx < 0 || x + dx >= width || y + dy < 0 || y + dy >= height) continue;
                if (shouldBeBomb && !blocks[x + dx][y + dy].isBomb()) continue;
                r.add(blocks[x + dx][y + dy]);
            }
        return r;
    }
}
