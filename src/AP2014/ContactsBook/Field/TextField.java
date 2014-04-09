package AP2014.ContactsBook.Field;

import javax.swing.*;
import java.awt.*;

public class TextField extends Field {

    private JTextField txt;
    private String validateRegex;

    public TextField(String title,boolean isRTL,String validateRegex) {
        super(title);

        txt=new JTextField();
        if(isRTL)
            txt.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        this.validateRegex=validateRegex;

        super.init();
    }

    public TextField(String title) {
        this(title,true,".+");
    }

    @Override
    protected JComponent getValueContainer() {
        return txt;
    }

    @Override
    public String getError() {
        if(txt==null  || txt.getText().matches(validateRegex))
            return null;
        else return "Invalid input";
    }


}

