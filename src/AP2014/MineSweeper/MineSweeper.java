package AP2014.minesweeper;

import javax.swing.*;

public class MineSweeper extends JFrame {

    public MineSweeper() {
        setLayout(null);
        setSize(500,500);
        setLocationRelativeTo(null);
        updateUI();

    }

    void open() {
        setVisible(true);
        newGame();
    }

    void updateUI() {
        setTitle("MineSweeper");
    }

    void newGame() {
        StartGame g=new StartGame();
        if(g.open()) {

        } else {

        }
    }


    public static void main(String[] args) {
        new MineSweeper().open();
    }
}
