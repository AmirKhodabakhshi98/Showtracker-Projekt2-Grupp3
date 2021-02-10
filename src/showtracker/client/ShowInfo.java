package showtracker.client;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.LineBorder;

import showtracker.Episode;
import showtracker.Helper;
import showtracker.Show;

/**
 * 
 * @author Adam Joulak
 * Changes made by Filip
 * 
 * Show info panel
 *
 */
class ShowInfo extends JPanel {
	private JPanel pnlMain = new JPanel();
	private ArrayList<SeasonListener> listeners = new ArrayList<>();
	private Show show;

	ShowInfo(Show show) {
		this.show = show;
		for (double d: show.getSeasons())
			listeners.add(new SeasonListener(d));

		initiatePanels();
		draw();
	}

    /**
     * Method for setting up the panel
     */
	private void initiatePanels() {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(pnlMain);
		scrollPane.setLayout(new ScrollPaneLayout());
		scrollPane.setBackground(Color.decode("#6A86AA"));

		pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.PAGE_AXIS));
		pnlMain.setBackground(Color.decode("#6A86AA"));
		pnlMain.add(Box.createHorizontalGlue());
		
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
		pnlHeader.add(new JLabel(show.getName()));
		pnlHeader.add(btnInfo, BorderLayout.EAST);
		pnlHeader.setBorder(new LineBorder(Color.black));

		setLayout(new BorderLayout());
		add(pnlHeader, BorderLayout.NORTH);
		add(scrollPane);
	}

    /**
     * Refreshing the view
     */
	private void draw() {
		pnlMain.removeAll();
		for (SeasonListener listener : listeners) {
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
			panel.add(Box.createHorizontalGlue());
			JButton button;
			if (listener.getSeason() == 0)
				button = new JButton("Specials");
			else
				button = new JButton("Season " + Helper.df.format(listener.getSeason()));
			button.setMinimumSize(new Dimension(100, 30));
			button.setMaximumSize(new Dimension(100, 30));
			button.addActionListener(listener);
			button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			panel.add(button);
			if (listener.getOpen())
				for (Episode episode : show.getEpisodes())
					if (episode.getSeasonNumber() == listener.getSeason()) {
						JButton infoButton = new JButton("Info - Episode " + Helper.df.format(episode.getEpisodeNumber()) + " - " + episode.getName());
						infoButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						infoButton.addActionListener(e -> {
								JOptionPane.showMessageDialog(null,
										"<html><body><p style=\"width: 200px;\">" +
												show.getEpisode(episode.getSeasonNumber(),
														episode.getEpisodeNumber()).getDescription() +
												"</p></body></html>", episode.getName(), JOptionPane.INFORMATION_MESSAGE);
						});
						panel.add(infoButton);
						JCheckBox checkBox = new JCheckBox();
						checkBox.setSelected(episode.isWatched());
						checkBox.addActionListener(new EpisodeListener(episode));
						panel.add(checkBox);
			}
			pnlMain.add(panel);
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