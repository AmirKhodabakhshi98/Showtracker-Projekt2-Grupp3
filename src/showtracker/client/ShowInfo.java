package showtracker.client;

import showtracker.Episode;
import showtracker.Helper;
import showtracker.Show;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @author Adam Joulak
 * Changes made by Filip
 * <p>
 * Show info panel
 */
class ShowInfo extends JPanel {
    private JPanel pnlShowInfo = new JPanel();
    private ArrayList<SeasonListener> listeners = new ArrayList<>();
    private Show show;

    ShowInfo(Show show) {
        this.show = show;
        for (double d : show.getSeasons())
            listeners.add(new SeasonListener(d));

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
//        scrollPane.getViewport().removeAll();
//        Box box = Box.createVerticalBox();
//        scrollPane.getViewport().setBackground(Color.decode("#6A86AA"));

        pnlShowInfo.setLayout(new BorderLayout());
        pnlShowInfo.setBackground(Color.decode("#6A86AA"));
        pnlShowInfo.add(Box.createHorizontalGlue());
//		pnlMain.add(box);

        JLabel lblHeader = new JLabel(show.getName());

        ImageIcon imiInfo = new ImageIcon("images/info.png");
        Image imgInfo = imiInfo.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        imiInfo = new ImageIcon(imgInfo);

        JButton btnInfo = new JButton(imiInfo);
        btnInfo.setPreferredSize(new Dimension(30, 50));
        btnInfo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnInfo.addActionListener(e -> JOptionPane.showMessageDialog(null,
                "<html><body><p style=\"width: 200px;\">" +
                        show.getDescription() +
                        "</p></body></html>", "Show info", JOptionPane.PLAIN_MESSAGE));

        JPanel pnlHeader = new JPanel();
        pnlHeader.setBounds(0, 0, 500, 50);
        pnlHeader.setLayout(new BorderLayout());
        pnlHeader.setPreferredSize(new Dimension(500, 50));
        pnlHeader.setBackground(Color.decode("#6A86AA"));
        pnlHeader.add(lblHeader);
        lblHeader.setFont(new Font("Monospaced", Font.BOLD, 20));
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

                JButton btnSeason = new JButton("Show Season");

                JPanel panel = new JPanel(new FlowLayout());
//            JLabel label = new JLabel("Card Label");
			    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));


                JPanel pnlSouth = new JPanel(new FlowLayout());
			    pnlSouth.add(btnSeason);
                JButton button;
                JLabel seasonlbl;

                JPanel pnlMain = new JPanel(new BorderLayout());
                pnlMain.setPreferredSize(new Dimension(800,80));
                Border cardBorder = BorderFactory.createRaisedBevelBorder();
                pnlMain.setBorder(cardBorder); // new LineBorder(Color.DARK_GRAY)
                pnlMain.add(panel, BorderLayout.CENTER);
                pnlMain.add(pnlSouth, BorderLayout.SOUTH);
                pnlMain.setBackground(Color.decode("#6A86AA"));
                if (listener.getSeason() == 0)
                    button = new JButton("Specials");
                else
                    button = new JButton("Season " + Helper.df.format(listener.getSeason()));
                seasonlbl = new JLabel("Season " + Helper.df.format(listener.getSeason()));

                seasonlbl.setMinimumSize(new Dimension(100, 30));
                seasonlbl.setMaximumSize(new Dimension(100, 30));
                btnSeason.addActionListener(listener);
                btnSeason.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                panel.add(seasonlbl);
                panel.add(btnSeason);
                gbc.gridx = 0;
                gbc.weightx = 1;
                pnlShowInfo.add(pnlMain, gbc);


                if (listener.getOpen())
                    for (Episode episode : show.getEpisodes())
                        if (episode.getSeasonNumber() == listener.getSeason()) {
                            JPanel episodePanel = new JPanel(new BorderLayout());
                            Border Border = BorderFactory.createRaisedBevelBorder();
                            episodePanel.setBorder(Border); // new LineBorder(Color.DARK_GRAY)
                            episodePanel.setBackground(Color.decode("#6A86AA"));
                            JButton infoButton = new JButton("Info - Episode " + Helper.df.format(episode.getEpisodeNumber()) + " - " + episode.getName());
                            infoButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                            infoButton.addActionListener(e -> {
                                JOptionPane.showMessageDialog(null,
                                        "<html><body><p style=\"width: 200px;\">" +
                                                show.getEpisode(episode.getSeasonNumber(),
                                                        episode.getEpisodeNumber()).getDescription() +
                                                "</p></body></html>", episode.getName(), JOptionPane.INFORMATION_MESSAGE);
                            });
                            episodePanel.add(infoButton, BorderLayout.CENTER);
                            JCheckBox checkBox = new JCheckBox();
                            checkBox.setSelected(episode.isWatched());
                            checkBox.addActionListener(new EpisodeListener(episode));
                            episodePanel.add(checkBox, BorderLayout.EAST);
                            pnlShowInfo.add(episodePanel,gbc);
                        }
                gbc.anchor = GridBagConstraints.NORTHWEST;
                gbc.weighty = 1;
                pnlShowInfo.add(new JPanel(), gbc);
//            pnlMain.add(panel);
            }

        revalidate();
        repaint();
    }

    /**
     * Inner class for handling the opening and closing of each season
     */
    private class SeasonListener implements ActionListener {
        private double dblSeason;
        private boolean blnOpen = false;

        SeasonListener(double dblSeason) {
            this.dblSeason = dblSeason;
        }

        double getSeason() {
            return dblSeason;
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
        }
    }
}