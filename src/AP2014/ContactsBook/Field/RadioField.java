package AP2014.ContactsBook.Field;

import javax.swing.*;
import java.awt.*;

public class RadioField extends Field {

    private JRadioButton[] options;

    public RadioField(String title,String options) {
        super(title);

        String[] o=options.split("\\|");
        this.options=new JRadioButton[o.length];

        for(int i=0;i<o.length;i++)
            this.options[i]=new JRadioButton(o[i]);


        super.init();
    }

    @Override
    protected JComponent getValueContainer() {
        JPanel p=new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.RIGHT));
        

        for(JRadioButton o:options)
            p.add(o);
        return p;
    }

    @Override
    public String getError() {
        return null;
    }
}
