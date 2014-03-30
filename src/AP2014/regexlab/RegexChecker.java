package AP2014.regexlab;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegexChecker extends JFrame{

    private JTextField inpRegex;
    private JTextField inpString;
    private JLabel     lblStatus;

    public RegexChecker() {
        setTitle("Regex Lab");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Add components
        Container pane=getContentPane();
        pane.setLayout(new GridBagLayout());

        GridBagConstraints c=new GridBagConstraints();
        c.fill=GridBagConstraints.HORIZONTAL;
        c.gridx=c.gridy=0;
        c.ipady=10;

        c.weightx=.20;
        pane.add(new JLabel("Regex :"),c);
        c.gridy++;
        pane.add(new JLabel("Input :"),c);

        c.gridx++;

        c.weightx=.80;
        c.gridy=0;
        inpRegex=new JFormattedTextField();
        pane.add(inpRegex,c);
        c.gridy++;
        inpString=new JFormattedTextField();
        pane.add(inpString,c);

        c.gridy++;
        c.gridx=0;
        lblStatus=new JLabel("Status...");
        lblStatus.setFont(lblStatus.getFont().deriveFont(Font.BOLD));
        pane.add(lblStatus,c);

        //Add events
        DocumentListener onEdit= new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }
        };
        inpString.getDocument().addDocumentListener(onEdit);
        inpRegex.getDocument().addDocumentListener(onEdit);

        pack();
        setSize(300,getHeight()+30);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void update() {
        String inp=inpString.getText();
        String rgx=inpRegex.getText().replace("\\\\","\\");

        Color c=lblStatus.getForeground();
        String s=lblStatus.getText();

        try {
            boolean m=inp.matches(rgx);
            if(m) {
                c=Color.green;
                s="Matches!";
            } else {
                c=Color.red;
                s="Not matches!";
            }
        }catch (Exception e) {
            c=Color.orange;
            lblStatus.setText("Invalid regex !");
        }

        lblStatus.setText(s);
        lblStatus.setForeground(c);
    }

    public static void main(String[] args) {
        new RegexChecker();
    }
}
