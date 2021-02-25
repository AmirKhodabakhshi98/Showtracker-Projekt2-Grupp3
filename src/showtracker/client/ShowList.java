package showtracker.client;

import showtracker.Helper;
import showtracker.Show;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;

/**
 * @author Basir Ramazani
 * Changes made by Filip & Adam
 * <p>
 * A panel for user show list
 * <p>
 * updated 2021-02-09
 * @author Paul Moustakas & Andreas von Uthmann
 * @version 1.0.1
 */

class ShowList extends JPanel {
    private ClientController clientController;
    private JPanel pnlShowList = new JPanel();
    private JScrollPane scrollPane = new JScrollPane();


    /**
     * Constructor that takes a ClientController instance
     *
     * @param clientController
     */
    ShowList(ClientController clientController) {
        this.clientController = clientController;
        pnlShowList.setBackground(Color.decode("#6A86AA"));
        MyDocumentListener myDocumentListener = new MyDocumentListener();
        setLayout(new BorderLayout());
        add(myDocumentListener, BorderLayout.NORTH);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Refereshing the view with a user's every show
     */
    public void draw() {
        draw(clientController.getUser().getShows());
    }

    /**
     * Refereshes the view with a selected amount of shows, from the search list
     *
     * @param shows The shows to show
     * @author Paul Moustakas, Andreas Von Uthmann:  Updated GUI components, size, max, min, fonts etc. Removed HTLM and replaced with pure Java for Labels.
     */
    private void draw(ArrayList<Show> shows) {
        shows.sort(new Helper.NameComparator());
        GridBagConstraints gbc = new GridBagConstraints();
        pnlShowList.setLayout(new GridBagLayout());
        gbc.fill = GridBagConstraints.HORIZONTAL;
        pnlShowList.setBackground(Color.decode("#6A86AA"));

        pnlShowList.removeAll();
        if (shows.size() > 0) {
            for (Show show : shows) {
                JButton btnInfo = new JButton("Info");
                JButton btnRemove = new JButton("Remove");

                btnRemove.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                btnInfo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                JPanel pnlMiddle = new JPanel(new FlowLayout());
                JLabel label = new JLabel("Card Label");
                label.setFont(new Font("Monospaced", Font.BOLD, 18));
                label.setText(show.getName());
                pnlMiddle.add(label);
                pnlMiddle.setBackground(Color.decode("#6A86AA"));

                JPanel pnlSouth = new JPanel(new FlowLayout());
                pnlSouth.add(btnInfo);
                pnlSouth.add(btnRemove);
                pnlSouth.setBackground(Color.decode("#6A86AA"));

                JPanel pnlMain = new JPanel(new BorderLayout());
                pnlMain.setPreferredSize(new Dimension(800, 80));
                Border cardBorder = BorderFactory.createRaisedBevelBorder();
                pnlMain.setBorder(cardBorder); // new LineBorder(Color.DARK_GRAY)
                pnlMain.add(pnlMiddle, BorderLayout.CENTER);
                pnlMain.add(pnlSouth, BorderLayout.SOUTH);
                pnlMain.setBackground(Color.decode("#6A86AA"));

                btnInfo.addActionListener(e -> clientController.setPanel("Info", show));
                btnRemove.addActionListener(e -> {
                    clientController.getUser().removeShow(show);
                    draw();
                });

                gbc.gridx = 0;
                gbc.weightx = 1;

                pnlShowList.add(pnlMain, gbc);

            }
            gbc.anchor = GridBagConstraints.NORTHWEST;
            gbc.weighty = 1;
            pnlShowList.add(new JPanel(), gbc);

        } else {
            pnlShowList.add(new JLabel("   Nothing in your list at the moment!"));
            pnlShowList.add(new JLabel("          "));
            ImageIcon imi = new ImageIcon("images/Showtrack.png");
            Image image = imi.getImage().getScaledInstance(150, 150, Image.SCALE_AREA_AVERAGING);
            JLabel lbLogo = new JLabel(new ImageIcon(image));
            pnlShowList.add(lbLogo);


        }
        scrollPane.setViewportView(pnlShowList);
        scrollPane.setLayout(new ScrollPaneLayout());
        pnlShowList.revalidate();
    }

    /**
     * Inner class for searching amongst shows when a user writes in the search list
     */
    private class MyDocumentListener extends JTextField implements DocumentListener {

        MyDocumentListener() {
            javax.swing.text.Document doc = this.getDocument();
            this.setPreferredSize(new Dimension(700, 30));
            TextPrompt tp7 = new TextPrompt("Search Yor List", this);
            tp7.setForeground(Color.GRAY);
            tp7.changeAlpha(0.5f);
            tp7.changeStyle(Font.BOLD + Font.CENTER_BASELINE);
            doc.addDocumentListener(this);
            setBackground(Color.WHITE);
            setBorder(new LineBorder(Color.BLACK));
        }

        public void changedUpdate(DocumentEvent e) {
            searchShow();
        }

        public void insertUpdate(DocumentEvent e) {
            searchShow();
        }

        public void removeUpdate(DocumentEvent e) {
            searchShow();
        }

        /**
         * Method for getting the shows that matches the search terms
         */
        private void searchShow() {
            ArrayList<Show> searchShows = new ArrayList<>();
            for (Show show : clientController.getUser().getShows()) {
                if (show.getName().toLowerCase().contains(getText().toLowerCase())) {
                    searchShows.add(show);
                }
//                else {
//                    JOptionPane.showMessageDialog(null, "The search gave no result!");
//                }

            }
            pnlShowList.removeAll();
            pnlShowList.setBackground(Color.decode("#6A86AA"));
            draw(searchShows);
        }
    }
}