package AP2014.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Random;

public class MineSweeper extends JFrame {

    private Block[][] blocks;
    private int width, height;
    private SevenSegment score, time;
    private Smilli smilli;
    boolean isGameRunning=false;

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
        isGameRunning=true;
    }

    public void blockClicked(Block block) {

        if(!timer.isRunning())
            timer.start();

        if (block.isBomb()) {
            block.setBombState(Block.BOMB_STATE_BOOM);
            updateStates();
            gameOver();
            return;
        }
        if(!block.isShown())
            showBlocks(block);
        else {
            int flaggedNCount=getBlockNeighbours(block.x,block.y,false,true).size();
            if(flaggedNCount==block.getBombs()) {
                for(Block b:getBlockNeighbours(block.x,block.y,false))
                    if(!b.isFlagged() && !b.isShown())
                        blockClicked(b);
            }
        }
        updateStates();
    }

    private void showBlocks(Block block) {
        block.dispatchEvent(BlockEvent.showBlock
                (block,getBlockNeighbours(block.x,block.y,false)));
    }


    private void gameOver() {
        if(isGameRunning)
            timer.stop();
        else
            return;
        isGameRunning=false;
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                if (blocks[x][y].isBomb() &&
                        blocks[x][y].getDisplayState() == Block.DISPLAY_STATE_FLAG)
                    blocks[x][y].setBombState(Block.BOMB_STATE_FOUND);
                blocks[x][y].setShown(true);
            }
        smilli.setIcon(3);

        JOptionPane.showMessageDialog(this, "Game over! :(");
    }

    private void gameWin() {
        if(isGameRunning)
            timer.stop();
        else
            return;
        isGameRunning=false;
        smilli.setIcon(4);
        JOptionPane.showMessageDialog(this, "You win! :)");
    }


    public void updateStates() {
        int activeBombs = startGame.mines();
        int uncheckedBlocks = 0;
        int flaggedBombs=0;

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {

                Block b = blocks[x][y];

                if (b.isFlagged())
                    activeBombs--;

                if (b.isBomb()) {
                    if(b.isFlagged())
                        flaggedBombs++;
                } else if (!b.isShown())
                    uncheckedBlocks++;
            }

        score.setValue(activeBombs);

        if (flaggedBombs== startGame.mines()|| uncheckedBlocks == 0)
            gameWin();
    }

    @Override
    protected void processEvent(AWTEvent e) {
        super.processEvent(e);

        if(!isGameRunning)
            return;

        if(e instanceof  BlockEvent) {
            BlockEvent b=(BlockEvent)e;
            Block src=(Block)b.getSource();
            switch (b.getID()) {
                case BlockEvent.BLOCK_PRESSED:
                    blockClicked(src);
                    break;
                case BlockEvent.BLOCK_SHOWBLOCK_REQUEST:
                    showBlocks((Block)b.arg0);
                    break;
                case BlockEvent.BLOCK_SHOWBLOCK_MOUSECHANGE:
                    Boolean state=(Boolean)b.arg0;
                    if(state)
                        smilli.setIcon(2);
                    else
                        smilli.setIcon(0);
                    break;
                case BlockEvent.BLOCK_SHOWBLOCK_UPDATE:
                    updateStates();
                    break;
            }
        }

    }

    private ArrayList<Block> getBlockNeighbours(int x, int y, boolean shouldBeBomb) {
        return getBlockNeighbours(x,y,shouldBeBomb,false);

    }

    private ArrayList<Block> getBlockNeighbours(int x, int y, boolean shouldBeBomb,boolean shouldBeFlagged) {
        ArrayList<Block> r = new ArrayList<Block>();
        for (int dx = -1; dx < 2; dx++)
            for (int dy = -1; dy < 2; dy++) {
                if (dx == 0 && dy == 0) continue;
                if (x + dx < 0 || x + dx >= width || y + dy < 0 || y + dy >= height) continue;
                if (shouldBeBomb && !blocks[x + dx][y + dy].isBomb()) continue;
                if (shouldBeFlagged && !blocks[x + dx][y + dy].isFlagged()) continue;
                r.add(blocks[x + dx][y + dy]);
            }
        return r;
    }
}
