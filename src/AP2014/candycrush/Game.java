package AP2014.candycrush;

import javax.swing.*;

public class Game {

    private JFrame gameWindow;
    private GameBoard board;
    private int level;

    public Game(int level) {

        this.level=level;

        gameWindow=new JFrame("CandyCrush!");
        gameWindow.setSize(547,567);
        gameWindow.setLocationRelativeTo(null);
        gameWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        board=new GameBoard(15,15);
        gameWindow.add(board);
    }

    public void Start() {
        board.setLevel(1);
        board.newGame();

        gameWindow.setVisible(true);

    }


}


