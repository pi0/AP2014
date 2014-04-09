package AP2014.ContactsBook.Field;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;

public class FieldContainer extends JPanel{

    public FieldContainer() {
        //setBackground(Color.red);
        setLayout(null);
    }

    public void layoutComponents() {

        int span=5;
        int height=20;
        int top=span;

        for(Component field:getComponents()) {
            field.setSize(getWidth()-2*span,height);
            field.setLocation(getWidth() - field.getWidth() - span, top);
            top+=span+field.getHeight();
        }
    }




}
