package AP2014.ContactsBook;


import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;

public class ContactsView extends JFrame {

    JMenuBar topMenu;
    JMenu contactsMenu;
    JMenu helpMenu;
    JMenuItem newContactMenu;

    JTable contactsTable;
    ContactsModel contacts;

    JPopupMenu contactPopup;
    JMenuItem contactPopupEdit;
    JMenuItem contactPopupRemove;

    public ContactsView() {

        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        setTitle("مخاطب ها");

        topMenu = new JMenuBar();
        topMenu.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        setJMenuBar(topMenu);
        topMenu.add(contactsMenu = new JMenu("مخاطب ها"));
        topMenu.add(helpMenu = new JMenu("راهنما"));
        contactsMenu.add(newContactMenu = new JMenuItem("مخاطب جدید"));
        newContactMenu.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ContactModel m = ContactView.newContact();
                if (m != null)
                    contacts.add(m);
            }
        });


        contacts = ContactsModel.generateRandomeContacts(500);

        contactsTable = new JTable(contacts);
        contactsTable.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        for (int i = 0; i < contacts.getColumnCount(); i++)
            contactsTable.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
        contactPopup = new JPopupMenu("مخاطب");
        contactPopupEdit = new JMenuItem("ویرایش");
        contactPopupRemove = new JMenuItem("حذف");
        contactPopup.add(contactPopupEdit);
        contactPopup.add(contactPopupRemove);
        contactPopupEdit.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editRow();
            }
        });
        contactPopupRemove.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeRow();
            }
        });
        contactsTable.setComponentPopupMenu(contactPopup);
        contactsTable.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_DELETE:
                        removeRow();
                        break;
                    case KeyEvent.VK_ENTER:
                        editRow();
                        break;
                }
            }
        });
        contactsTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2)
                    editRow();
            }

            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    int r = contactsTable.rowAtPoint(e.getPoint());
                    int c = contactsTable.columnAtPoint(e.getPoint());
                    contactsTable.clearSelection();
                    contactsTable.changeSelection(r, c, false, false);
                }
            }
        });


        JScrollPane contactsTablePanel = new JScrollPane(contactsTable);
        int span = 20;
        contactsTablePanel.setSize(getWidth() - 2 * span, getHeight() - span * 3);
        contactsTablePanel.setLocation(span, 0);
        getContentPane().add(contactsTablePanel);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // UIManager.setLookAndFeel(new MotifLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new ContactsView().setVisible(true);

    }

    private void editRow() {
        int row = contactsTable.getSelectedRow();
        if (row < 0)
            return;
        ContactModel m = (ContactModel) contacts.getRow(row).clone();
        boolean s = (new ContactView(m)).open();
        if (s) {
            contacts.getRow(row).cloneFrom(m);
            contacts.updateRow(row);
        }
    }

    private void removeRow() {
        int row = contactsTable.getSelectedRow();
        if (row < 0)
            return;
        contacts.removeRow(row);
        contactsTable.repaint();
    }

}
