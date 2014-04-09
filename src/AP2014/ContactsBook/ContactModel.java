package AP2014.ContactsBook;

import AP2014.ContactsBook.Field.Field;
import AP2014.ContactsBook.Field.RadioField;
import AP2014.ContactsBook.Field.TextField;

import java.util.ArrayList;

public class ContactModel {

    public ArrayList<Field> getFields() {
        ArrayList<Field> fields=new ArrayList<Field>();

        fields.add(new TextField("نام"));
        fields.add(new TextField("نام خانوادگی"));
        fields.add(new TextField("شرکت"));
        fields.add(new TextField("شماره تماس",false,"\\d+"));
        fields.add(new TextField("فکس",false,"\\d+"));
        fields.add(new RadioField("وضعیت","دانش آموز|استاد"));



        return fields;
    }


}
