package showtracker.client;

import showtracker.Helper;
import showtracker.Rating;
import showtracker.Show;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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

    private static final int colorCard1      = 0xF8F8F8;
    private static final int colorCard2      = 0xF2F2EF;
    private static final int colorBackground = 0xDCDBD8;
    private static final int colorTitle      = 0x6A86AA;

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
        pnlShowList.setBackground(new Color(colorBackground));
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
     * @author Paul Moustakas, Andreas Von Uthmann:
     * Updated GUI components, size, max, min, fonts etc. Removed HTLML and replaced with pure Java for Labels.
     */
    private void draw(ArrayList<Show> shows) {
        shows.sort(new Helper.NameComparator());
        GridBagConstraints gbc = new GridBagConstraints();
        pnlShowList.setLayout(new GridBagLayout());
        pnlShowList.setBackground(new Color(colorBackground));

        pnlShowList.removeAll();
        if (shows.size() > 0) {
            Show show;
            Color colorMiddle;

            for(int i = 0; i < shows.size(); i++) {
                show = shows.get(i);

                if (i % 2 == 0)
                    colorMiddle = new Color(colorCard1);
                else
                    colorMiddle = new Color(colorCard2);

                JButton btnInfo = new JButton("Info");
                JButton btnRemove = new JButton("Remove");
                String[] rating = {"No rating","★","★★","★★★","★★★★","★★★★★"};
                JComboBox cb = new JComboBox(rating);
                if(show.getPersonalRating() != null)
                    cb.setSelectedItem(show.getPersonalRating().getStrValue());
                else
                    cb.setSelectedItem(rating);

                btnRemove.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                btnInfo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                cb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                JPanel pnlMiddle = new JPanel(new FlowLayout());

                JLabel label = new JLabel("Card Label");
                label.setForeground(new Color(colorTitle));
                label.setFont(new Font("Roboto", Font.BOLD, 18));
                label.setText(show.getName());

                pnlMiddle.add(label);
                pnlMiddle.setBackground(colorMiddle);

                JPanel pnlSouth = new JPanel(new FlowLayout());
                pnlSouth.setBackground(colorMiddle);
                pnlSouth.add(btnInfo);
                pnlSouth.add(btnRemove);
                pnlSouth.add(cb);

                JPanel pnlMain = new JPanel(new BorderLayout());
                pnlMain.setPreferredSize(new Dimension(800, 162));
                Border cardBorder = BorderFactory.createLineBorder(Color.decode("#E3E2DD"));
                pnlMain.setBorder(cardBorder);
                pnlMain.add(pnlMiddle, BorderLayout.CENTER);
                pnlMain.add(pnlSouth, BorderLayout.EAST);
                pnlMain.setBackground(Color.decode("#6A86AA"));

                //Poster container
                Border posterBorder = BorderFactory.createLineBorder(colorMiddle, 10, false);
                JLabel lblImage = new JLabel();
                lblImage.setBorder(posterBorder);
                JPanel pnlPoster = new JPanel(new BorderLayout());

                // Add poster if available
                if (show.getPoster() != null &&
                    !show.getPoster().isEmpty()
                ) {
                    BufferedImage image;
                    try {
                        URL url = new URL(show.getPoster());
                        image = ImageIO.read(url);
                        Image dImg = image.getScaledInstance(96, 142, Image.SCALE_AREA_AVERAGING);
                        ImageIcon imageIcon = new ImageIcon(dImg);
                        lblImage.setIcon(imageIcon);
                        pnlPoster.add(lblImage, BorderLayout.WEST);
                    } catch (IOException e) {
                        System.err.println("Poster exception in class ShowList");
                    }
                }

                final Show tmpShow = show;
                btnInfo.addActionListener(e -> {
                    clientController.setPanel("Info", tmpShow);
                });

                cb.addActionListener(e ->{
                    String personalRating = (String) cb.getSelectedItem();
                    tmpShow.setPersonalRating(Rating.get(personalRating));
                    cb.setSelectedItem(personalRating);
                });

                btnRemove.addActionListener(e -> {
                    clientController.getUser().removeShow(tmpShow);
                    draw();
                });

                gbc.gridx = 0;
                gbc.weightx = 1;

                pnlMain.add(pnlPoster, BorderLayout.WEST);
                pnlShowList.add(pnlMain, gbc);
            }

            gbc.anchor = GridBagConstraints.NORTHWEST;
            gbc.weighty = 1;
            pnlShowList.add(new JPanel(), gbc);
        }
        else {
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
            TextPrompt tp7 = new TextPrompt("Search Your List", this);
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