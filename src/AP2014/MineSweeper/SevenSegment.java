package AP2014.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class SevenSegment extends JComponent {

    private int value = 0;

    public SevenSegment() {
        setSize(60, 40);
        setBackground(Color.black);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        updateUI();
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int v = value;
        boolean is_ng=false;
        if(v>999)
            v=999;
        if(v<0) {
            v*=-1;
            is_ng=true;
            if(v>99)
                v=99;
        }
        for (int i = 2, p = 1; i >= 0; i--, v /= 10) {
            BufferedImage r = GameResource.sevenSegmentDigits[v % 10];
            g2.drawImage(r, i * 15 + 5, 5, 15, 30, this);
        }
        if(is_ng){
            BufferedImage r = GameResource.sevenSegmentDigits[10];
            g2.drawImage(r, 0 * 15 + 5, 5, 15, 30, this);
        }
    }
}
