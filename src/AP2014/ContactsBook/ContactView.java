package AP2014.ContactsBook;

import AP2014.ContactsBook.Field.Field;
import AP2014.ContactsBook.Field.FieldContainer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

public class ContactView extends JDialog {

    private ContactModel model;
    private JButton btnOk,btnCancel;
    private boolean status;

    public ContactView(ContactModel model) {

        this.model=model;

        setSize(400, 450);
        setLocationRelativeTo(null);
        setTitle("مخاطب");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setModal(true);
        setLayout(null);

        getRootPane().registerKeyboardAction(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                status = false;
                setVisible(false);
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);


        int span=30;

        FieldContainer container=new FieldContainer();
        container.setSize(350, 300);
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

        AbstractAction clicked = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("تایید"))
                    status = true;
                setVisible(false);
            }
        };
        btnOk.addActionListener(clicked);
        btnCancel.addActionListener(clicked);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                repaint();
            }
        });
    }

    public ContactView() {
        this(new ContactModel());
    }

    public static ContactModel newContact() {
        ContactView v = new ContactView(new ContactModel());
        if (v.open())
            return v.model;
        else
            return null;
    }

    public boolean open() {
        setVisible(true);
        return status;
    }


}
