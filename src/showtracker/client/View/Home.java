package showtracker.client.View;

import showtracker.Episode;
import showtracker.Helper;
import showtracker.Show;
import showtracker.client.ClientController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * @author Filip Spånberg
 * Changes made by Moustafa
 * 
 * Panel showing the next episode to watch in a show
 */
public class Home extends JPanel {
    private ClientController clientController;
    private JScrollPane scrollPane = new JScrollPane();

    public Home(ClientController clientController) {
        this.clientController = clientController;
        add(scrollPane);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(960, 400));
    }

    /**
     * Refereshing the view
     * @Changes made by Paul Moustakas - Updated the components to match the new GUI design, removed "Dead" unnecessary fill out code.
     * @date 2021-02-09
     */
    public void draw() {

        scrollPane.getViewport().removeAll();
        Box box = Box.createVerticalBox();
        clientController.getUser().getShows().sort(new Helper.LastWatchedComparator());

        int episodeCounter = 0;
        for (Show show : clientController.getUser().getShows()) {

            Episode currentEpisode = show.getFirstUnwatched();

            if (currentEpisode != null) {

                episodeCounter++;
                JPanel panel = new JPanel(new BorderLayout());
                panel.setBorder(BorderFactory.createBevelBorder(1));

                JButton button = new JButton("I've seen it!");
                button.setFont(new Font("Monospaced", Font.PLAIN, 14));
                button.addActionListener(new EpisodeListener(currentEpisode));

                JLabel label = new JLabel(String.format("<html><div style=\"width:150px;\">%s<br>Season %s, episode %s%s</div></html>",
                        show.getName(),
                        Helper.df.format(currentEpisode.getSeasonNumber()),
                        Helper.df.format(currentEpisode.getEpisodeNumber()),
                        currentEpisode.getName() != null && !currentEpisode.getName().equals("") ? ":<br>" + currentEpisode.getName() : ""));

                label.setFont(new Font("Monospaced", Font.PLAIN, 14));
                panel.add(button, BorderLayout.EAST);
                panel.add(label, BorderLayout.CENTER);
                panel.setMaximumSize(new Dimension(960, 80));
                box.add(panel);
            }
        }

        if (episodeCounter == 0)
            box.add(new JLabel("<html><p style=\"width:200px; align:center;\">\nNo new episodes to display. Either search for new shows, or go to your list and set some episodes to \"not watched\".</p></html>"));
        scrollPane.setViewportView(box);
        scrollPane.revalidate();
        scrollPane.repaint();
    }


    /**
     * Inner class to handle the episode buttons (setting an episode to "watched"
     */
    private class EpisodeListener implements ActionListener {

        private Episode episode;
        EpisodeListener(Episode episode) {
            this.episode = episode;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(episode.getName() + ", " + episode);
            episode.setWatched(true);
            draw();
        }
    }
}