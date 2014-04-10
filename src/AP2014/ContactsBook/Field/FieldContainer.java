package AP2014.ContactsBook.Field;

import javax.swing.*;
import java.awt.*;

public class FieldContainer extends JPanel {

    public FieldContainer() {
        setLayout(null);
    }

    public void layoutComponents() {
        int span = 5;
        int top = span;

        for (Component field : getComponents()) {
            field.setSize(getWidth() - 2 * span, ((Field) field).preferredHeight());
            field.setLocation(getWidth() - field.getWidth() - span, top);
            top += span + field.getHeight();
        }
    }

}
