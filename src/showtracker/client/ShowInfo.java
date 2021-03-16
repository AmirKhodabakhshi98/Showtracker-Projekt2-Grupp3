package showtracker.client;

import showtracker.Episode;
import showtracker.Helper;
import showtracker.Show;
import showtracker.client.View.FontsAndColors;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @author Adam Joulak
 * Changes made by Filip, Albin Ahlbeck
 * <p>
 * Show info panel
 */
class ShowInfo extends JPanel {
    private JPanel pnlShowInfo = new JPanel();
    private ArrayList<SeasonListener> listeners = new ArrayList<>();
    private Show show;
    private ClientController clientController;

    private Image imgListOpen;
    private Image imgListClosed;

    ShowInfo(Show show, ClientController clientController) {
        this.show = show;
        this.clientController = clientController;

        for (int i = 0; i < show.getSeasons(); i++)
            listeners.add(new SeasonListener(i+1));

        imgListOpen = new ImageIcon("images/list-open.png").getImage()
                .getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        imgListClosed = new ImageIcon("images/list-closed.png").getImage()
                .getScaledInstance(32, 32, Image.SCALE_SMOOTH);

        initiatePanels();
        draw();
    }

    /**
     * Method for setting up the panel
     */
    private void initiatePanels() {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(pnlShowInfo);
        scrollPane.setLayout(new ScrollPaneLayout());
        scrollPane.setBackground(Color.decode("#6A86AA"));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        pnlShowInfo.setLayout(new BorderLayout());
        pnlShowInfo.setBackground(Color.decode("#6A86AA"));
        pnlShowInfo.add(Box.createHorizontalGlue());

        JLabel lblHeader = new JLabel(show.getName());
        lblHeader.setBorder(new EmptyBorder(5, 15, 5, 5));
        lblHeader.setFont(new Font("Roboto", Font.BOLD, 20));

        ImageIcon imiInfo = new ImageIcon("images/info.png");
        Image imgInfo = imiInfo.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        imiInfo = new ImageIcon(imgInfo);

        JButton btnInfo = new JButton(imiInfo);
        btnInfo.setPreferredSize(new Dimension(50, 40));
        btnInfo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnInfo.addActionListener(e -> JOptionPane.showMessageDialog(null,
                "<html><body><p style=\"width: 300px;\">" +
                        show.getDescription() + "</p><br>" + "Imdb Rating: " + show.getImdbRating() +
                        "</p><br>" + "Released: " + show.getYear()+
                        "</p><br>" + "Actors: " + show.getActors()+
                        "</body></html>", "Show info", JOptionPane.PLAIN_MESSAGE));

        JPanel pnlHeader = new JPanel();
        pnlHeader.setBounds(0, 0, 500, 50);
        pnlHeader.setLayout(new BorderLayout());
        pnlHeader.setPreferredSize(new Dimension(500, 50));
        pnlHeader.add(lblHeader, BorderLayout.WEST);
        pnlHeader.add(btnInfo, BorderLayout.EAST);
        pnlHeader.setBorder(new LineBorder(Color.DARK_GRAY));

        setLayout(new BorderLayout());
        add(pnlHeader, BorderLayout.NORTH);
        add(scrollPane);
    }

    /**
     * Refreshing the view
     */
    private void draw() {
        pnlShowInfo.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        pnlShowInfo.setLayout(new GridBagLayout());
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (SeasonListener listener : listeners) {

            JToggleButton btnSeason = new JToggleButton();
            if (listener.blnOpen)
                btnSeason.setIcon(new ImageIcon(imgListClosed));
            else
                btnSeason.setIcon(new ImageIcon(imgListOpen));

            btnSeason.setSize(0, 80);
            btnSeason.setBorderPainted(false);
            btnSeason.setBackground(FontsAndColors.getProjectBlue());

            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(FontsAndColors.getProjectBlue());

            JLabel seasonlbl;

            JPanel pnlCompleted = new JPanel(new BorderLayout(5, 0));
            JLabel lblCompleted = new JLabel("Completed: ");
            JCheckBox cbxCompleted = new JCheckBox();
            pnlCompleted.setBorder(new EmptyBorder(10, 10, 10, 10));
            pnlCompleted.add(lblCompleted, BorderLayout.LINE_START);
            pnlCompleted.add(cbxCompleted, BorderLayout.CENTER);



            cbxCompleted.addActionListener(e ->
                setSeasonCompleted((int)listener.getSeason(), cbxCompleted.isSelected()));

            JPanel pnlMain = new JPanel(new BorderLayout());
            pnlMain.setPreferredSize(new Dimension(800,80));
            pnlMain.setBackground(FontsAndColors.getProjectBlue());
            Border cardBorder = BorderFactory.createRaisedBevelBorder();
            pnlMain.setBorder(cardBorder); // new LineBorder(Color.DARK_GRAY)

            pnlMain.setBackground(FontsAndColors.getProjectBlue());
            seasonlbl = new JLabel("Season " + Helper.df.format(listener.getSeason()));
            seasonlbl.setSize(new Dimension(100, 80));
            seasonlbl.setFont(new Font("Roboto", Font.PLAIN, 17));
            btnSeason.addActionListener(listener);
            btnSeason.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            JPanel pnlWest = new JPanel(new BorderLayout());
            pnlWest.setBackground(FontsAndColors.getProjectBlue());
            pnlWest.add(btnSeason, BorderLayout.WEST);
            pnlWest.add(seasonlbl, BorderLayout.CENTER);

            panel.add(pnlWest, BorderLayout.WEST);
            panel.add(pnlCompleted, BorderLayout.EAST);

            pnlMain.add(panel, BorderLayout.CENTER);

            gbc.gridx = 0;
            gbc.weightx = 1;
            pnlShowInfo.add(pnlMain, gbc);

            int watchedCnt = 0;
            ArrayList<Episode> seasonEpisodes = show.getEpisodes()
                    .get(listener.getSeason()-1);

            if (listener.getOpen()) {
                for (Episode episode : seasonEpisodes) {
                    JPanel episodePanel = new JPanel(new BorderLayout());
                    Border Border = BorderFactory.createRaisedBevelBorder();
                    episodePanel.setBorder(Border); // new LineBorder(Color.DARK_GRAY)
                    episodePanel.setBackground(Color.decode("#6A86AA"));
                    JToggleButton infoButton;
                    if (show.isCustom()) {
                        infoButton = new JToggleButton("Episode " +
                                Helper.df.format(episode.getEpisodeNumber()));
                    } else {
                        infoButton = new JToggleButton("Episode " +
                                Helper.df.format(episode.getEpisodeNumber()) +
                                " - " + episode.getName());
                    }

                    infoButton.setBackground(new Color(238, 238, 238));
                    infoButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    infoButton.addActionListener(e -> {
                        clientController.getDetailEpisode(episode);
                        if (episode.getPoster() != null) {
                            JOptionPane.showMessageDialog(null,
                                    "<html><body><p style=\"width: 200px;\">" +
                                            episode.getPlot() + "<p>" + "<br>" + episode.getRuntime() + "</p>" +
                                            "<img src =" + episode.getPoster(), episode.getName(),
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "<html><body><p style=\"width: 200px;\">" +
                                            episode.getPlot() + "<p>" + "<br>" + episode.getRuntime() + "</p>", episode.getName(),
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    });

                    episodePanel.add(infoButton, BorderLayout.CENTER);
                    JCheckBox checkBox = new JCheckBox();
                    checkBox.setSelected(episode.isWatched());
                    checkBox.addActionListener(new EpisodeListener(episode));
                    episodePanel.add(checkBox, BorderLayout.EAST);
                    episodePanel.setBackground(FontsAndColors.getProjectBlue());
                    pnlShowInfo.add(episodePanel, gbc);

                    if (episode.isWatched())
                        watchedCnt++;
                }

                gbc.anchor = GridBagConstraints.NORTHWEST;
                gbc.weighty = 1;
                pnlShowInfo.add(new JPanel(), gbc);
                pnlShowInfo.setBackground(FontsAndColors.getProjectBlue());
            }
            else {

                for (Episode episode : seasonEpisodes)
                    if (episode.isWatched())
                        watchedCnt++;
            }

            if (watchedCnt == seasonEpisodes.size())
                cbxCompleted.setSelected(true);

        }

        revalidate();
        repaint();
    }

    private void setSeasonCompleted(int season, boolean completed) {
        int seasons = show.getEpisodes().size();
        if (season <= 0 || season > seasons)
            return;

        ArrayList<Episode> eps = show.getEpisodes().get(season-1);
        for (int i = 0; i < eps.size(); i++) {
            eps.get(i).setWatched(completed);
        }

        clientController.getUser().updateShow(show);
        draw();
    }

    /**
     * Inner class for handling the opening and closing of each season
     */
    private class SeasonListener implements ActionListener {
        private int season;
        private boolean blnOpen = false;

        SeasonListener(int season) {
            this.season = season;
        }

        int getSeason() {
            return season;
        }

        boolean getOpen() {
            return blnOpen;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            blnOpen = !blnOpen;
            draw();
        }
    }

    /**
     * Inner class for handling setting an episode watched or not
     */
    private class EpisodeListener implements ActionListener {
        private Episode episode;

        EpisodeListener(Episode episode) {
            this.episode = episode;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean isWatched = ((JCheckBox) e.getSource()).isSelected();
            episode.setWatched(isWatched);
            draw();
        }
    }
}