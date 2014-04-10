package AP2014.ContactsBook.Field;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public abstract class Field extends JPanel implements FocusListener, ComponentListener {

    protected JLabel lblTitle;
    private JComponent value;
    private boolean isFocused = true;

    public Field(String title) {
        setLayout(null);
        lblTitle = new JLabel(title, SwingConstants.RIGHT);
    }

    protected abstract JComponent getValueContainer();

    public abstract String getError();

    public abstract int preferredHeight();

    public abstract Object getValue();

    public abstract void setValue(Object o);

    public abstract Object clone();

    protected void init() {

        value = getValueContainer();

        value.addFocusListener(this);

        addComponentListener(this);

        lblTitle.setLabelFor(value);
        lblTitle.setForeground(Color.gray);

        add(lblTitle);
        add(value);

        reLayout();
    }

    protected void reLayout() {
        lblTitle.setSize(100, getHeight());
        lblTitle.setLocation(getWidth() - lblTitle.getWidth(), 0);
        value.setSize(getWidth() - lblTitle.getWidth(), getHeight());
        value.setLocation(0, 0);
    }

    public void check() {
        Color c = Color.blue;
        if (!isFocused) {
            if (isValid())
                c = Color.decode("#007317");
            else
                c = Color.red;
        }
        lblTitle.setForeground(c);
    }

    public boolean isValid() {
        return (getError() == null);
    }

    public String getName() {
        return lblTitle.getText();
    }

    @Override
    public void focusGained(FocusEvent e) {
        isFocused = true;
        check();
    }

    @Override
    public void focusLost(FocusEvent e) {
        isFocused = false;
        check();
    }

    @Override
    public void componentResized(ComponentEvent e) {
        reLayout();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        reLayout();
    }

    @Override
    public void componentShown(ComponentEvent e) {
        reLayout();
    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }


}

