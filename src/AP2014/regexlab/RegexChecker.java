package AP2014.regexlab;

import sun.security.provider.MD5;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexChecker extends JFrame{

    private JTextField inpRegex;
    private JTextField inpString;
    private JLabel     lblStatus;
    private JList lstMatches;


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
        lblStatus=new JLabel("");
        lblStatus.setFont(lblStatus.getFont().deriveFont(Font.BOLD));
        pane.add(lblStatus,c);

        c.gridy++;
        c.gridx=0;
        pane.add(new JLabel("Pattern matches :"),c);

        c.gridy++;
        c.gridx=0;
        lstMatches=new JList();
        JScrollPane scrollPane=new JScrollPane();
        scrollPane.setViewportView(lstMatches);
        c.fill=GridBagConstraints.HORIZONTAL;
        c.anchor=GridBagConstraints.SOUTH;
        c.ipady=100;
        c.gridwidth=2;
        pane.add(scrollPane,c);


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
        update();
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
                s="Not single matches!";
            }
        }catch (Exception e) {
            c=Color.orange;
            lblStatus.setText("Invalid regex !");
        }
        lblStatus.setText(s);
        lblStatus.setForeground(c);


        try {
            Pattern pattern = Pattern.compile(rgx);
            Matcher matcher = pattern.matcher(inp);
            Dimension size=lstMatches.getSize();
            DefaultListModel listModel=new DefaultListModel();

            while (matcher.find())
                listModel.add(listModel.size(),matcher.group());
            lstMatches.setModel(listModel);
            lstMatches.setSize(size);

        }catch (Exception e){return;}

    }

    String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static void main(String[] args) {
        new RegexChecker();
    }
}
