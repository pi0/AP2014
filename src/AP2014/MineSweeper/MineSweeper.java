package AP2014.minesweeper;

import AP2014.io.MessageBox;
import javafx.application.Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.ImageObserver;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;

public class MineSweeper extends JFrame {

    private Block[][] blocks;
    private int width, height;
    private SevenSegment score, time;
    private Smilli smilli;

    private JMenuBar menuBar;
    private JMenu menuGame;
    private JMenuItem menuNewGame;
    private JMenuItem menuExit;

    private StartGame startGame = new StartGame();

    private Timer timer = new Timer(1000, new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            time.setValue(time.getValue() + 1);
        }
    });

    public MineSweeper() {
        setLayout(null);
        setResizable(false);
        getInsets().set(0, 0, 0, 0);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Mine sweeper!");
        initializeComponents();

    }

    public static void main(String[] args) throws Exception {

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        new MineSweeper().open();
    }

    void open() {
        setVisible(true);
        newGame(false);
    }

    void initializeComponents() {
        setJMenuBar(menuBar = new JMenuBar());
        menuBar.add(menuGame = new JMenu("Game"));
        menuGame.add(menuNewGame = new JMenuItem("New game"));
        menuGame.add(menuExit = new JMenuItem("Exit"));

        menuNewGame.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newGame(true);
            }
        });
        menuExit.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        setBackground(Color.gray);

        score = new SevenSegment();
        score.setLocation(20, 3);

        smilli = new Smilli();
        smilli.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newGame(false);
            }
        });

    }

    void newGame(boolean showConfigDialog) {

        //Game config
        if (showConfigDialog && !startGame.open())
            return;
        this.width = startGame.cols();
        this.height = startGame.rows();
        int mines = startGame.mines();
        boolean showQuestion = startGame.showQuestionMark();

        //It's a NEW game dude!
        getContentPane().removeAll();

        //Stop timer until first move
        timer.stop();

        //Set sizes
        JPanel c = new JPanel(null);

        c.setSize(width * 16, height * 16);

        setSize(Math.max(c.getWidth() + c.getX() + 100, 300), Math.max(c.getHeight() + c.getY() + 130, 300));
        c.setLocation((getWidth() - c.getWidth()) / 2, 50);

        getContentPane().add(c);

        //Make blocks
        blocks = new Block[width][height];

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                blocks[x][y] = new Block(showQuestion, x, y, this);
                blocks[x][y].setLocation(x * 16, y * 16);
                c.add(blocks[x][y]);
            }

        //Place bombs
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

        //Count bombs
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                blocks[x][y].setBombs(getBlockNeighbours(x, y, true).size());


        score.setValue(mines);
        getContentPane().add(score);

        time = new SevenSegment();
        time.setLocation(getWidth() - time.getWidth() - 25, 3);
        getContentPane().add(time);

        smilli.setLocation((getWidth() - smilli.getWidth()) / 2, 10);
        getContentPane().add(smilli);
        smilli.setIcon(0);
        setLocationRelativeTo(null);
        repaint();
    }

    public void BlockClicked(int x, int y) {

        timer.start();

        if (blocks[x][y].isBomb()) {
            blocks[x][y].setBombState(Block.BOMB_STATE_BOOM);
            gameOver();
            return;
        }

        showBlocks(blocks[x][y]);
    }

    private void showBlocks(Block base) {

        if (base.isBomb() || base.isShown())
            return;

        base.setShown();

        if (base.getBombs() != 0)
            return;


        for (Block block : getBlockNeighbours(base.x, base.y, false))
            showBlocks(block);

    }

    private void gameOver() {
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                if (blocks[x][y].isBomb() &&
                        blocks[x][y].getDisplayState() == Block.DISPLAY_STATE_FLAG)
                    blocks[x][y].setBombState(Block.BOMB_STATE_FOUND);
                blocks[x][y].setShown();
            }
        smilli.setIcon(3);
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game over! :(");
    }

    private void gameWin() {
        smilli.setIcon(4);
        timer.stop();
        JOptionPane.showMessageDialog(this, "You win! :)");
    }

    public void setBlockPressed(boolean isClicking) {
        smilli.setIcon(isClicking ? 2 : 0);
    }

    public void updateStates() {
        int activeBombs = 0;
        int uncheckedBlocks = 0;

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {

                Block b = blocks[x][y];
                if (b.isBomb())
                    if (b.getDisplayState() != Block.DISPLAY_STATE_FLAG)
                        activeBombs++;
                    else ;
                else if (!b.isShown())
                    uncheckedBlocks++;
            }

        score.setValue(activeBombs);

        if (activeBombs == 0 || uncheckedBlocks == 0)
            gameWin();
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
