package AP2014.io;

import javax.swing.*;

public class MessageBox {

    public static boolean AskQuestion (String question,String title) {
        JOptionPane pane=new JOptionPane(
                question,
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION
        );

        pane.createDialog(title).setVisible(true);
        return (pane.getValue().equals(JOptionPane.YES_OPTION));
    }
}
