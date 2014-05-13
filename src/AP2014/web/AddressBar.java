package AP2014.web;

import javax.swing.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

public class AddressBar extends JComboBox {

    HistoryManager historyManager;
    boolean newFocus = true;
    Vector<AbstractAction> goListeners = new Vector<AbstractAction>();

    public AddressBar(final HistoryManager h) {
        setEditable(true);
        this.historyManager = h;

        h.addListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getID() == HistoryManager.HistoryManager_GO)
                    getEditor().setItem(historyManager.getLast().toString());
            }
        });

        getEditor().getEditorComponent().addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                showPopup();
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                newFocus = true;
                hidePopup();
            }
        });

        getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    for (AbstractAction a : goListeners)
                        a.actionPerformed(new ActionEvent(this, 0, null));
                    hidePopup();
                }

            }

            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                updateSuggestions();
                if (newFocus) {
                    newFocus = false;
                    requestFocus();
                } else
                    showPopup();

            }
        });

        requestFocus();
    }

    public void go() {

    }

    public void addGoListener(AbstractAction a) {
        goListeners.add(a);
    }

    void updateSuggestions() {
        String current = getInput();
        removeAllItems();
        for (URL u : historyManager.getSuggestion(current, 4))
            addItem(u);
        getEditor().setItem(current);
    }

    public String getInput() {

        return (String) ((getEditor().getItem()).toString());
    }

    public URL getInputURL() {
        try {
            return new URL(getInput());
        } catch (MalformedURLException e) {
            return null;
        }
    }

}
