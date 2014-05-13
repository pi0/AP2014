package AP2014.web;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

public class WebBrowser extends JFrame {

    AddressBar addressBar;
    JToolBar navBar;
    JToolBar navBar2;
    JEditorPane browserPane;
    JMenuBar menuBar;
    JPanel statusBar;
    JLabel statusLabel;
    JPanel toolPanel;
    JButton bback;
    JButton bnext;
    JButton br;


    HistoryManager history;

    public WebBrowser() {
        initializeUI();
        try {
            go(new URL("http://info.cern.ch/hypertext/WWW/TheProject.html"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

//       UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        UIManager.setLookAndFeel(new NimbusLookAndFeel());


        new WebBrowser().setVisible(true);
    }

    private void initializeUI() {
        setTitle("Browser");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        initializeComponents(getContentPane());
    }

    private void initializeComponents(Container c) {

        //History
        history = new HistoryManager();
        history.addListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getID() == HistoryManager.HistoryManager_ITEMSELECTED)
                    try {
                        go(new URL(e.getActionCommand()));
                    } catch (MalformedURLException e1) {
                        e1.printStackTrace();
                    }
            }
        });

        //Address Bar
        addressBar = new AddressBar(history);
        addressBar.addGoListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                go();
            }
        });

        //Nav bar
        navBar = new JToolBar(SwingConstants.HORIZONTAL);
        navBar.setFloatable(false);
        navBar.setBorderPainted(false);

        bback = new JButton("<");
        bback.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBack();
            }
        });
        bnext = new JButton(">");
        bnext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goNext();
            }
        });
        br = new JButton("R");
        br.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                go(history.getLast());
            }
        });
        bback.setEnabled(false);
        bnext.setEnabled(false);

        navBar.add(bback);
        navBar.add(bnext);
        navBar.add(br);

        //Nav bar 2
        navBar2 = new JToolBar(SwingConstants.HORIZONTAL);
        navBar2.setFloatable(false);
        navBar2.setBorderPainted(false);

        JButton btn_go = new JButton("GO");
        btn_go.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                go();
            }
        });
        navBar2.add(btn_go);

        final JToggleButton proxy = new JToggleButton("Proxy", false);
        proxy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (proxy.isSelected()) {
                    System.getProperties().put("proxySet", "true");
                    System.getProperties().put("http.proxyHost", "localhost");
                    System.getProperties().put("http.proxyPort", "2030");
                } else {
                    System.getProperties().put("proxySet", "false");
                    System.getProperties().put("proxyHost", "");
                    System.getProperties().put("proxyPort", "");
                }
            }
        });
        navBar2.add(proxy);

        //Browser Pane
        browserPane = new JEditorPane("text/html", "<h1>Hello!</h1>");
        browserPane.setEditable(false);
        JScrollPane browserContainer = new JScrollPane(browserPane);

        browserPane.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                URL u = e.getURL();
                if (u == null)
                    u = Utils.resolveRelativeURL(history.getLast(), e.getDescription());
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    go(u);
                } else {
                    statusLabel.setText("Go to " + u.toString());
                }
            }
        });

        //Menu bar
        menuBar = new JMenuBar();
        menuBar.add(new JMenu("File"));
        setJMenuBar(menuBar);

        //Status bar
        statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel("Ready!");
        statusBar.add(statusLabel);

        //RightPanel
        toolPanel = new JPanel(new BorderLayout());

        toolPanel.add(history.getGui(), BorderLayout.CENTER);

        //Put components together

        c.setLayout(new BorderLayout(0, 0));

        JPanel a = new JPanel(new BorderLayout(0, 0));
        a.add(navBar, BorderLayout.WEST);
        a.add(addressBar, BorderLayout.CENTER);
        a.add(navBar2, BorderLayout.EAST);
        c.add(a, BorderLayout.NORTH);
        JSplitPane b = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, browserContainer, toolPanel);
        b.setOneTouchExpandable(true);
        b.setDividerLocation(550);

        c.add(b);
        c.add(statusBar, BorderLayout.SOUTH);
    }

    private void go() {
        go(addressBar.getInputURL());
    }

    private void goBack() {
        go(history.goBack());
    }

    private void goNext() {
        go(history.goNext());
    }

    private synchronized void go(final URL addr) {

        if (addr == null)
            return;

        history.go(addr);

        bback.setEnabled(history.hasBack());
        bnext.setEnabled(history.hasNext());


        statusLabel.setText("Loading " + addr);

        new Thread(new Runnable() {
            public void run() {
                String s = Utils.readAllPage(addr);
                if (s == null)
                    s = "<h1>Error!</h1>";
                browserPane.setText(s);
                statusLabel.setText("Ready!");
            }
        }).start();

    }

}
