package AP2014.web;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Stack;
import java.util.Vector;

public class HistoryManager {

    public static final int HistoryManager_GO = 0;
    public static final int HistoryManager_ITEMSELECTED = 1;
    private final static String[] defaultSites = {"http://google.com", "http://facebook.com", "http://youtube.com",
            "http://yahoo.com", "http://baidu/com", "http://yahoo.com", "http://wikipedia.org", "http://qq.com", "http://taobao.com",
            "http://twitter.com", "  http://live.com", "http://linkedin.com", "http://ebay.com",
            "http://aut.ac.ir", "http://ceit.aut.ac.ir", "http://pi0.ir",
            "http://info.cern.ch/hypertext/WWW/TheProject.html", "http://titangames.ir", "http://cmyip.com"};
    Vector<URL> history;
    Vector<AbstractAction> listeners;
    Stack<URL> urlStack;
    Stack<URL> nextStack;
    URL last;
    JList<String> gui = null;

    public HistoryManager() {
        try {
            last = new URL("http://google.com");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        listeners = new Vector<AbstractAction>();
        urlStack = new Stack<URL>();
        nextStack = new Stack<URL>();
        history = new Vector<URL>();

        for (String s : defaultSites)
            try {
                addItem(new URL(s));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


    }


    public void addListener(AbstractAction a) {
        listeners.add(a);
    }

    private void action(int id, String command) {
        for (AbstractAction l : listeners)
            l.actionPerformed(new ActionEvent(this, id, command));
    }


    public void addItem(URL url) {
        boolean found = false;
        for (int i = 0; i < history.size(); i++)
            if (history.get(i).toString().equals(url.toString())) {
                found = true;
                break;
            }
        if (!found)
            history.insertElementAt(url, 0);

    }

    public void go(URL u) {
        addItem(u);
        if (urlStack.isEmpty() || (!urlStack.peek().toString().equals(u.toString()))) {
            urlStack.push(u);
        }
        last = u;
        //   nextStack.clear();
        updateGui();
        action(HistoryManager_GO, u.toString());
    }

    public URL getLast() {
        return last;
    }

    public URL goBack() {
        if (!hasBack())
            return null;
        nextStack.push(urlStack.pop());//current
        URL u = urlStack.pop();
        updateGui();
        return u;
    }

    public boolean hasBack() {
        return urlStack.size() > 1;
    }

    public URL goNext() {
        if (!hasNext())
            return null;

        urlStack.push(nextStack.peek());
        URL u = nextStack.pop();
        updateGui();
        return u;
    }

    public boolean hasNext() {
        return !nextStack.isEmpty();
    }


    public Vector<URL> getSuggestion(String input, int num) {

        Vector<URL> v = new Vector<URL>();

        String rgx = ".*(" + input.toLowerCase().
                replaceAll("http|com|ir|www", "").
                replaceAll("^[^\\w]+", "").
                replaceAll("[^\\w]+$", "").
                replaceAll("[^\\w]+", "|") + ").*";

        //      System.out.println(rgx);

        for (URL url : history) {
            if (v.size() >= num)
                return v;
            if (url.toString().matches(rgx))
                v.add(url);
        }


        for (int r = num - v.size(); r > 0; r--) {

            for (URL u : history) {
                if (!v.contains(u))
                    v.add(u);
                break;
            }
        }

        return v;

    }

    private void updateGui() {
        if (gui == null)
            return;

        ((DefaultListModel<String>) gui.getModel()).removeAllElements();

        for (URL u : history)
            addToGUI(u.toString());

    }

    private void addToGUI(String s) {
        ((DefaultListModel<String>) gui.getModel()).addElement(s);
    }

    public JList<String> getGui() {
        if (gui == null) {
            gui = new JList<String>(new DefaultListModel<String>());
            gui.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

            gui.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {


                    action(HistoryManager_ITEMSELECTED, gui.getSelectedValue());

                }
            });
        }
        return gui;
    }
}
