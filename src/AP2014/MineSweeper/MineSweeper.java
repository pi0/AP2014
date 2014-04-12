package AP2014.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class MineSweeper extends JFrame {

    private Block[][] blocks;
    private int width, height;

    public MineSweeper() {
        setLayout(null);
        updateUI();
    }

    public static void main(String[] args) {
        new MineSweeper().open();
    }

    void open() {
        setVisible(true);
        newGame();
    }

    void updateUI() {
        setTitle("MineSweeper");
    }

    void newGame() {

        int width = 25, height = 18, mines = 40;
        boolean showQuestion = false;

        this.width = width;
        this.height = height;

        blocks = new Block[width][height];
        JPanel c = new JPanel(null);

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                blocks[x][y] = new Block(showQuestion);
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

        c.setSize((width + 1) * blocks[0][0].getWidth(),
                (height + 2) * blocks[0][0].getHeight());
        c.setLocation(10, 80);
        setSize(c.getWidth() + c.getX(), c.getHeight() + c.getY());

        getContentPane().add(c);
        getInsets().set(0, 0, 0, 0);
        setLocationRelativeTo(null);
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
