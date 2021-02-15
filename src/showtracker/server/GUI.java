package showtracker.server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;

/**
 * @author Filip Spånberg
 * Handles the graphical interface
 */
class GUI {
    private Controller controller;
    private JPanel pnlMain = new JPanel();
    private JLabel lblActiveThreads = new JLabel("Active threads: 0");
    private JComboBox cmbThreadNumber;
    private JButton btnStart = new JButton("Start server");
    private JButton btnStop = new JButton("Stop server");
    private JFrame frame = new JFrame("ShowTracker server");

    GUI(Controller controller) {
        this.controller = controller;
        String[] arThreadNumber = {"1","2","3","4","5","6","7","8","9","10"};
        cmbThreadNumber = new JComboBox(arThreadNumber);

        pnlMain.setLayout(new GridLayout(3,2));
    }

    /**
     * Starts a connection with [threads] number of threads
     * @param threads
     */
    private void startConnection(int threads) {
        controller.startConnection(threads);
        btnStart.setEnabled(false);
        btnStop.setEnabled(true);
    }

    /**
     * Stops the connection
     */
    private void stopConnection() {
        controller.stopConnection();
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
    }

    /**
     * Set the label indicating active threads
     * @param i
     */
    void setActiveThreads(int i) {
        lblActiveThreads.setText("Active threads: " + i);
    }

    /**
     * Opens a panel that lets the user get an authentication token from TheTVDB
     */
//    private void authenticateTheTVDB() {
//        String token = controller.authenticateTheTVDB();
//        JTextArea textArea = new JTextArea(8, 50);
//        textArea.setText(token);
//        textArea.setLineWrap(true);
//        JLabel label = new JLabel("Go to https://api.thetvdb.com/swagger#/ and enter the following token:");
//        label.setPreferredSize(new Dimension(100, 50));
//        JButton button = new JButton("Open browser");
//        button.addActionListener(e -> openBrowser());
//        JPanel panel = new JPanel();
//        panel.setLayout(new BorderLayout());
//        panel.add(label, BorderLayout.NORTH);
//        panel.add(button, BorderLayout.SOUTH);
//        panel.add(textArea, BorderLayout.CENTER);
//        JOptionPane.showMessageDialog(null, panel);
//    }

    /**
     * Method for opening a browser to submit a token
     */
    private static void openBrowser() {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI("https://api.thetvdb.com/swagger#/"));
            } catch (Exception e) {
                System.out.println("GUI: " + e);
            }
        }
    }

    /**
     * Initiates the graphical interface
     */
    void start() {
        btnStart.addActionListener(e -> startConnection(Integer.parseInt((String) cmbThreadNumber.getSelectedItem())));
        pnlMain.add(btnStart);
        pnlMain.add(cmbThreadNumber);
        btnStop.addActionListener(e -> stopConnection());
        pnlMain.add(btnStop);
        pnlMain.add(lblActiveThreads);
        btnStop.setEnabled(false);
        JButton bnAuthenticate = new JButton("Authenticate");
//        bnAuthenticate.addActionListener(e -> authenticateTheTVDB());
        pnlMain.add(bnAuthenticate);
        frame.add(pnlMain);
        frame.setVisible(true);
        frame.pack();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("GUI exiting...");
                controller.stopConnection();
                System.out.println("GUI exited.");
                System.exit(0);
            }
        });
    }
}
