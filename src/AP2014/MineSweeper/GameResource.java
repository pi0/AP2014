package AP2014.minesweeper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class GameResource {

    public static final BufferedImage[] sevenSegmentDigits;
    public static final BufferedImage[] mineCounters;
    public static final BufferedImage[] buttonIcons;
    public static final ImageIcon[] smilliIcons;
    private static BufferedImage sprit;
    private static String sprit_path = "src\\AP2014\\minesweeper\\sprite100.gif";

    static {

        //Load main sprite
        try {
            sprit = ImageIO.read(new File(sprit_path));
        } catch (Exception e) {
            System.err.println("Unable to load game images ");
            System.exit(-1);
        }

        //Load sevenSegmentDigits
        sevenSegmentDigits = new BufferedImage[11];
        for (int i = 0; i < 11; i++)
            sevenSegmentDigits[i] =
                    sprit.getSubimage(13 * i, 0, 12, 23);

        //Load mines
        mineCounters = new BufferedImage[9];
        for (int i = 0; i < 9; i++)
            mineCounters[i] =sprit.getSubimage(16 * i, 23, 16, 16);

        //Load buttonIcons
        buttonIcons = new BufferedImage[8];
        for (int i = 0; i < 7; i++)
            buttonIcons[i] =sprit.getSubimage(16 * i, 40, 16, 16);
        buttonIcons[7] = mineCounters[0];

        //Load smillies
        smilliIcons = new ImageIcon[5];
        for (int i = 0; i < 5; i++)
            smilliIcons[i] = new ImageIcon(sprit.getSubimage(1 + 26 * i, 56, 25, 25));

    }


}
