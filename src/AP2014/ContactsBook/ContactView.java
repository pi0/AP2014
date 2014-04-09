package AP2014.ContactsBook;

import AP2014.ContactsBook.Field.*;

import javax.swing.*;

public class ContactView extends JDialog{

    private ContactModel model;
    private JButton btnOk,btnCancel;

    public ContactView(ContactModel model) {

        this.model=model;

        setSize(640,480);
        setLocationRelativeTo(null);
        setTitle("مخاطب");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);

        int span=30;

        FieldContainer container=new FieldContainer();
        container.setSize(300,300);
        container.setLocation(getWidth()-container.getWidth()-span,span);
        for(Field f:model.getFields())
            container.add(f);
        container.layoutComponents();
        getContentPane().add(container);

        btnOk=new JButton("تایید");
        btnCancel=new JButton("انصراف");
        btnOk.setSize(100,30);
        btnCancel.setSize(btnOk.getSize());
        btnOk.setLocation(getWidth()-btnOk.getWidth()-span,getHeight()-btnOk.getHeight()-2*span);
        btnCancel.setLocation(btnOk.getX()-btnOk.getWidth()-span,btnOk.getY());
        getContentPane().add(btnCancel);
        getContentPane().add(btnOk);
    }



    public static void main(String[] args) {
        new ContactView(new ContactModel()).setVisible(true);
    }

}
