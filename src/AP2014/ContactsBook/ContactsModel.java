package AP2014.ContactsBook;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ContactsModel implements TableModel {

    HashMap<Point, Object> cache;
    private ArrayList<ContactModel> contacts;
    private ContactModel contactModel;
    private ArrayList<TableModelListener> listeners;

    public ContactsModel() {
        contacts = new ArrayList<ContactModel>();
        listeners = new ArrayList<TableModelListener>();
        contactModel = new ContactModel();
        cache = new HashMap<Point, Object>();
    }

    public static ContactsModel generateRandomeContacts(int count) {
        ContactsModel c = new ContactsModel();
        for (int i = 0; i < count; i++)
            c.contacts.add(ContactModel.generateRandomModel());
        return c;
    }

    public void add(ContactModel contact) {
        contacts.add(contact);
        updateRow(contacts.size() - 1);
    }

    public void updateRow(int num) {
        for (TableModelListener listener : listeners)
            listener.tableChanged(new TableModelEvent(this, num, num));
        invalidateCache(num);
    }

    private void invalidateCache(int row) {
        for (int i = 0; i < getColumnCount(); i++)
            cache.remove(new Point(i, row));
    }

    public void updateAllRows() {
        for (int i = 0; i < contacts.size(); i++)
            updateRow(i);
    }

    public ContactModel getRow(int index) {
        return contacts.get(index);

    }

    public void removeRow(int index) {
        contacts.remove(index);
        updateAllRows();
    }

    @Override
    public int getRowCount() {
        return contacts.size();
    }

    @Override
    public int getColumnCount() {
        return contactModel.getFields().size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return contactModel.getFields().get(columnIndex).getName();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return contactModel.getFields().get(columnIndex).getClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Point p = new Point(columnIndex, rowIndex);
        Object c = cache.get(p);
        if (c == null) {
            c = contacts.get(rowIndex).fields.get(columnIndex).getValue();
            cache.put(p, c);
        }
        return c;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        //TODO !
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        listeners.add(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        listeners.remove(l);
    }
}
