package AP2014.testgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TestGame extends JFrame{

    private GraphicsDevice graphicsDevice;
    private boolean isFullScreen = false;

    private GamePanel gamePanel;

    public TestGame() {
        graphicsDevice=GraphicsEnvironment.getLocalGraphicsEnvironment().
                getDefaultScreenDevice();

        initFrame();
        initComponents(getContentPane());
    }

    private void initFrame() {

        //Basic setup
        setTitle("Best game!");
        setFullScreen(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Toggle fullscreen with Ctrl+Enter
        getRootPane().registerKeyboardAction(new AbstractAction() {
                                                 @Override
                                                 public void actionPerformed(ActionEvent e) {
                                                     setFullScreen(!isFullScreen);
                                                 }
                                             },
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER , KeyEvent.CTRL_DOWN_MASK),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        //Close on ESC
        getRootPane().registerKeyboardAction(new AbstractAction() {
                                                 @Override
                                                 public void actionPerformed(ActionEvent e) {
                                                     setVisible(false);
                                                     dispose();
                                                 }
                                             },
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE ,0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private void initComponents(Container c) {
        c.setLayout(new BorderLayout(50,50));
        //gamePanel
        gamePanel=new GamePanel();
        c.add(gamePanel,BorderLayout.CENTER);
    }


    private void setFullScreen(boolean fullScreen) {
        this.isFullScreen = fullScreen;
        isFullScreen &= graphicsDevice.isFullScreenSupported();
        setResizable(!isFullScreen);

        if (isFullScreen) {
            // Full-screen mode
            graphicsDevice.setFullScreenWindow(this);
            validate();
        } else {
            // Windowed mode
            setSize(600,400);
            graphicsDevice.setFullScreenWindow(null);
            setLocationRelativeTo(null);//center
        }
    }

    public static void main(String[] args) {
        new TestGame().setVisible(true);
    }

}
