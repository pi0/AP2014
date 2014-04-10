package AP2014.ContactsBook.Field;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

public class SelectionField<T extends JToggleButton> extends Field {

    private ArrayList<T> optionButtons;
    private JPanel optionsContainer;
    private Class<T> tClass;
    private String options;


    public SelectionField(String title, String options, Class<T> tClass) {
        super(title);

        this.tClass = tClass;
        this.options = options;

        optionButtons = new ArrayList<T>();

        for (String o : options.split("\\|"))
            try {
                T a = tClass.getConstructor(String.class).newInstance(o);
                optionButtons.add((a));
            } catch (Exception e) {
                e.printStackTrace();
            }

        optionsContainer = new JPanel();
        optionsContainer.setLayout(null);

        ButtonGroup bg = new ButtonGroup();
        for (T o : this.optionButtons) {
            optionsContainer.add(o);
            if (o instanceof JRadioButton)
                bg.add(o);

            final Field THIS = this;
            o.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    THIS.focusGained(e);
                }

                @Override
                public void focusLost(FocusEvent e) {
                    THIS.focusLost(e);
                }
            });
        }

        super.init();
    }

    private T getButton(String name) {
        for (T b : optionButtons)
            if (b.getText().equals(name))
                return b;
        return null;
    }


    @Override
    protected JComponent getValueContainer() {
        return optionsContainer;
    }

    @Override
    public String getError() {
        return null;
    }

    @Override
    public int preferredHeight() {
        return 20;
    }

    @Override
    public Object getValue() {
        String r = "";
        for (T o : optionButtons)
            if (o.isSelected())
                r += o.getText() + ",";
        if (r.length() > 0)
            return r.substring(0, r.length() - 1);
        else
            return r;
    }

    @Override
    public void setValue(Object o) {
        for (String opt : o.toString().split(",")) {
            T b = getButton(opt);
            if (b != null)
                b.setSelected(true);
        }
    }

    @Override
    public Object clone() {
        SelectionField<T> s = new SelectionField<T>(getName(), options, tClass);
        s.setValue(getValue());
        return s;
    }

    @Override
    protected void reLayout() {
        super.reLayout();

        int w = optionsContainer.getWidth() / (optionButtons.size());
        int l = 0;
        for (T r : optionButtons) {
            r.setSize(w, preferredHeight());
            r.setLocation(l, 0);
            l += w;
        }
    }


}
