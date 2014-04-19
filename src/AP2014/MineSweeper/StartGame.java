package AP2014.minesweeper;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Pattern;

public class StartGame extends JDialog {

    private boolean status = false;
    private NumberField fRows, fCols, fMines;
    private JLabel lblRows, lblCols, lblMines;
    private JCheckBox showQuestion;

    private JRadioButton rL1, rL2, rL3, rLc;

    private JButton ok, cancel;

    public StartGame() {

        setLayout(null);
        setLocationRelativeTo(null);
        setSize(360, 280);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(false);
        setTitle("New game");

        initializeComponents();

    }

    private void initializeComponents() {

        enableEvents(AWTEvent.COMPONENT_EVENT_MASK);

        Container c = getContentPane();
        ButtonGroup bg = new ButtonGroup();
        rL1 = new JRadioButton("Beginner");
        rL1.setSize(130, 20);
        rL1.setLocation(10, 20);
        rL1.setActionCommand("1");
        c.add(rL1);
        bg.add(rL1);

        rL2 = new JRadioButton("Intermediate");
        rL2.setSize(rL1.getSize());
        rL2.setLocation(10, 60);
        rL2.setActionCommand("2");
        c.add(rL2);
        bg.add(rL2);

        rL3 = new JRadioButton("Expert");
        rL3.setSize(rL1.getSize());
        rL3.setLocation(10, 100);
        rL3.setActionCommand("3");
        c.add(rL3);
        bg.add(rL3);

        rLc = new JRadioButton("Custom");
        rLc.setSize(rL1.getSize());
        rLc.setLocation(10, 140);
        rLc.setActionCommand("0");
        c.add(rLc);
        bg.add(rLc);

        lblRows = new JLabel("Height :");
        lblRows.setSize(50, 20);
        lblRows.setLocation(210, 30);
        c.add(lblRows);

        lblCols = new JLabel("Width :");
        lblCols.setSize(50, 20);
        lblCols.setLocation(210, 70);
        c.add(lblCols);

        lblMines = new JLabel("Mines :");
        lblMines.setSize(50, 20);
        lblMines.setLocation(210, 110);
        c.add(lblMines);

        fRows = new NumberField(20, 3, 50, lblRows, this);
        fRows.setSize(60, 20);
        fRows.setLocation(260, 30);
        c.add(fRows);
        lblRows.setLabelFor(fRows);

        fCols = new NumberField(30, 3, 50, lblCols, this);
        fCols.setSize(60, 20);
        fCols.setLocation(260, 70);
        c.add(fCols);
        lblRows.setLabelFor(fCols);

        fMines = new NumberField(50, 3, 50, lblMines, this);
        fMines.setSize(60, 20);
        fMines.setLocation(260, 110);
        c.add(fMines);
        lblRows.setLabelFor(fMines);

        showQuestion = new JCheckBox("Enable Question icon");
        showQuestion.setSize(150, 20);
        showQuestion.setLocation(200, 150);
        c.add(showQuestion);

        ok = new JButton("Play");
        ok.setSize(100, 30);
        ok.setLocation(75, 190);
        ok.setActionCommand("ok");
        c.add(ok);

        cancel = new JButton("Cancel");
        cancel.setSize(ok.getSize());
        cancel.setLocation(185, 190);
        cancel.setActionCommand("no");
        c.add(cancel);


        ActionListener rSelect = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int c = Integer.parseInt(e.getActionCommand());
                boolean enabled = false;
                switch (c) {
                    case 1:
                        fRows.setText(9 + "");
                        fCols.setText(9 + "");
                        fMines.setText(10 + "");
                        break;
                    case 2:
                        fRows.setText(16 + "");
                        fCols.setText(16 + "");
                        fMines.setText(40 + "");
                        break;
                    case 3:
                        fRows.setText(16 + "");
                        fCols.setText(30 + "");
                        fMines.setText(99 + "");
                        break;
                    default:
                        enabled = true;
                }

                fCols.setEditable(enabled);
                fRows.setEditable(enabled);
                fMines.setEditable(enabled);


            }
        };
        rL1.addActionListener(rSelect);
        rL2.addActionListener(rSelect);
        rL3.addActionListener(rSelect);
        rLc.addActionListener(rSelect);
        rLc.setSelected(true);

        ActionListener btnClick = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("ok")) {
                    status = true;

                } else {
                    status = false;
                }
                setVisible(false);
            }
        };
        ok.addActionListener(btnClick);
        cancel.addActionListener(btnClick);


    }

    @Override
    protected void processEvent(AWTEvent e) {

        if (e.getID() == NumberField.OnEditEventID) {
            //Validate
            if(rows()>20)
                fRows.setText(20+"");
            if(cols()>30)
                fCols.setText(30+"");
            int m = (int) (.4 * (rows() * cols()));
            if (mines() > m)
                fMines.setText(m + "");

        }


        super.processEvent(e);
    }

    public int rows() {
        return Integer.parseInt(fRows.getText());
    }

    public int cols() {
        return Integer.parseInt(fCols.getText());
    }

    public int mines() {
        return Integer.parseInt(fMines.getText());
    }

    public boolean showQuestionMark() {
        return showQuestion.isSelected();
    }

    public boolean open() {
        setVisible(true);
        return status;
    }

}


class NumberField extends JTextField {

    public final static int OnEditEventID = AWTEvent.RESERVED_ID_MAX + 40;
    final JLabel lbl;
    private int min, max;
    private Component parent;

    public NumberField(int defaultVal, int min, int max, JLabel lbl, final Component parent) {

        this.lbl = lbl;
        this.min = min;
        this.max = max;

        this.parent = parent;

        setText(defaultVal + "");

        addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (isEditable() && isEnabled())
                    NumberField.this.lbl.setForeground(Color.red);
            }

            public void focusLost(FocusEvent e) {
                NumberField.this.lbl.setForeground(Color.black);
                parent.dispatchEvent(new OnEditEvent(NumberField.this));
            }
        });

    }

    @Override
    protected Document createDefaultModel() {
        return new NumericDocument();
    }

    private static class NumericDocument extends PlainDocument {
        private final static Pattern DIGITS = Pattern.compile("\\d*");

        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (str != null && DIGITS.matcher(str).matches())
                super.insertString(offs, str, a);


        }

    }

    class OnEditEvent extends ComponentEvent {
        public OnEditEvent(Component source) {
            super(source, OnEditEventID);
        }
    }
}


