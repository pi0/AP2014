package AP2014.minesweeper;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class Smilli extends JButton {

    public Smilli() {
        setSize(24, 24);

        setBackground(null);
        setBorder(null);
        setFocusable(false);

        setPressedIcon(GameResource.smilliIcons[1]);

        setIcon(0);
    }

    public void setIcon(int i) {
        setIcon(GameResource.smilliIcons[i]);
        update();
    }

    public void update() {
        updateUI();
    }

}
