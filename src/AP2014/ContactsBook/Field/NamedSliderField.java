package AP2014.ContactsBook.Field;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Enumeration;
import java.util.Hashtable;


public class NamedSliderField extends Field {

    Hashtable<Integer, JLabel> lables = new Hashtable<Integer, JLabel>();
    private JPanel container;
    private MyJSlider slider;
    private String options;

    public NamedSliderField(String title, String options) {
        super(title);

        this.options = options;

        String[] o = options.split("\\|");
        lables = new Hashtable<Integer, JLabel>();
        for (int i = 0; i < o.length; i++)
            lables.put(i, new JLabel(o[i]));

        slider = new MyJSlider(JSlider.HORIZONTAL, 0, o.length - 1, o.length / 2);
        slider.setMinorTickSpacing(1);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setLabelTable(lables);
        slider.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Color c = Color.black;
                int crr = slider.getValue();
                for (int i = 0; i < lables.size(); i++) {
                    if (i == crr)
                        c = Color.green;
                    else if (i > crr)
                        c = Color.gray;
                    lables.get(new Integer(i)).setForeground(c);
                }

            }
        });
        slider.getChangeListeners()[0].stateChanged(null);

        container = new JPanel();
        container.setLayout(null);
        container.add(slider);

        super.init();
    }


    @Override
    protected JComponent getValueContainer() {
        return container;
    }

    @Override
    public String getError() {
        return null;
    }

    @Override
    public int preferredHeight() {
        return 80;
    }

    @Override
    public Object getValue() {
        return ((JLabel) slider.getLabelTable().get(new Integer(slider.getValue()))).getText();
    }

    @Override
    public void setValue(Object o) {
        Enumeration<JLabel> e = slider.getLabelTable().elements();
        int i = slider.getMaximum();
        while (e.hasMoreElements()) {
            if (e.nextElement().getText().equals(o.toString())) {
                slider.setValue(i);
                break;
            }
            i--;
        }
    }

    @Override
    public Object clone() {
        NamedSliderField f = new NamedSliderField(getName(), options);
        f.setValue(getValue());
        return f;
    }

    @Override
    protected void reLayout() {
        super.reLayout();

        slider.setSize(container.getWidth() - 5, preferredHeight());
        slider.setLocation(5, 5);
    }
}


class MyJSlider extends JSlider {

    public MyJSlider(int orientation, int min, int max, int value) {
        super(orientation, min, max, value);

    }


}