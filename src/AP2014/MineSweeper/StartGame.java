package AP2014.minesweeper;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;

public class StartGame extends JDialog{

    private boolean status=false;
    private NumberField fRows,fCols,fMines;
    private JLabel lblRows,lblCols,lblMines;

    public StartGame() {
        setLayout(null);
        setLocationRelativeTo(null);
        setSize(200,300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);

        Container c=getContentPane();

        lblRows=new JLabel("Rows :");
        lblRows.setSize(50,20);
        lblRows.setLocation(20,10);
        c.add(lblRows);

        fRows.setSize(100,20);
        fRows.setLocation(70,10);
        c.add(fRows);


        lblCols=new JLabel("Cols :");
        lblCols.setSize(50,20);
        lblCols.setLocation(20,30);
        c.add(lblCols);
        //fCols=NumberField.newInstance(10,lblCols);
        fCols.setSize(100,20);
        fCols.setLocation(70,30);
        c.add(fCols);


        lblMines=new JLabel("Mines :");
        lblMines.setSize(50,20);
        lblMines.setLocation(20,50);
        c.add(lblMines);
        //fMines=NumberField.newInstance(10,lblMines);
        fMines.setSize(100,20);
        fMines.setLocation(70,50);
        c.add(fMines);

    }

    public boolean open() {
        setVisible(true);
        return status;
    }

}

class NumberField extends JFormattedTextField{

    final JLabel lbl;

    public NumberField(int defaultVal,final JLabel lbl,NumberFormatter nf) {
        super(nf);

        this.lbl=lbl;

        setText(defaultVal+"");

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                NumberField.this.lbl.setForeground(Color.red);
            }

            @Override
            public void focusLost(FocusEvent e) {
                NumberField.this.lbl.setForeground(Color.black);
            }
        });

        DocumentFilter f=new DocumentFilter();

        //((AbstractDocument)getDocument()).setDocumentFilter(new );

    }

}