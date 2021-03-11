package showtracker.client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

import showtracker.Episode;
import showtracker.Movie;
import showtracker.Show;
import showtracker.client.View.FontsAndColors;

/**
 * 
 * @author Moustafa Al-louhaibi
 * Changes made by Filip, Basir & Adam
 * 
 * Represents the search panel
 *
 * Changes made by Paul Moustakas
 * - GUI fixes.
 * @date 2021-02-09
 * @version 1.0.1
 *
 */
class SearchShows extends JPanel {

	private ClientController clientController;

	private JTextField txfSearchBar = new JTextField();
    private JTextField txfShowName = new JTextField();
	private JTextField txfShowYear = new JTextField();
	private	JTextField txfShowPlot = new JTextField();
	private	JTextField txfShowActors = new JTextField();
	private	JTextField txfShowIMDBRating = new JTextField();
	private JTextField txfShowPoster = new JTextField();

	private JPanel pnlSearchResult = new JPanel();
	private JPanel pnlMyOwnShowPanel = new JPanel();
	private JPanel pnlMyShow = new JPanel();
	private JPanel pnlMyOwnMoviePanel = new JPanel();

	private JScrollPane spnSearchResult = new JScrollPane();

	/**
	 * Constructor that takes a ClientController instance
	 * @param clientController
	 */
	SearchShows(ClientController clientController) {
		this.clientController = clientController;
		setLayout(new BorderLayout());

		//Search Bar
		JPanel pnlSearchBar = new JPanel();
		pnlSearchBar.setBackground(FontsAndColors.getProjectBlue());
		pnlSearchBar.setSize(350, 200);
		pnlSearchBar.setLayout(new GridLayout(1,4));

		spnSearchResult.getVerticalScrollBar().setUnitIncrement(16);
		pnlSearchResult.setBackground(FontsAndColors.getProjectBlue());
		add(pnlSearchBar, BorderLayout.NORTH);
		add(spnSearchResult, BorderLayout.CENTER);


		txfSearchBar.setPreferredSize(new Dimension(200,60));
		txfSearchBar.setFont(FontsAndColors.getFontBold(16));
		JButton btnSearchBar = new JButton("Search");
		btnSearchBar.setFont(FontsAndColors.getFontPlain(16));
		btnSearchBar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		JButton btnCreateShow = new JButton("Create Show");
		btnCreateShow.setFont(FontsAndColors.getFontPlain(16));
		btnCreateShow.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnCreateShow.addActionListener(e -> drawNoSearchResultPanel());

		btnSearchBar.addActionListener(e -> drawSearchResultPanel(txfSearchBar.getText()));
		txfSearchBar.addKeyListener(new EnterListener());

		JButton btnCreateMovie = new JButton("Create Movie");
		btnCreateMovie.setFont(FontsAndColors.getFontPlain(16));
		btnCreateMovie.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnCreateMovie.addActionListener(e -> createOwnMoviePanel());

		pnlSearchBar.add(txfSearchBar);
		pnlSearchBar.add(btnSearchBar);
		pnlSearchBar.add(btnCreateShow);
		pnlSearchBar.add(btnCreateMovie);
	}



	/**
	 * Method for refreshing the view
	 */
	void draw() {
		pnlSearchResult.removeAll();
		pnlMyOwnMoviePanel.removeAll();
		pnlMyOwnMoviePanel.removeAll();
		pnlMyShow.removeAll();
		txfSearchBar.setText("");
		TextPrompt textPrompt = new TextPrompt("Search for a show here!", txfSearchBar);
		textPrompt.setFont(FontsAndColors.getFontItalic(16));
		textPrompt.changeAlpha(0.5f);
	}


	/**
	 * Method for either displaying the results from a show search, or showing a "no results" warning
	 * @param strSearchRequest The search terms
	 */
	private void drawSearchResultPanel(String strSearchRequest) {
		pnlSearchResult.removeAll();
		pnlMyOwnMoviePanel.removeAll();
		pnlMyOwnMoviePanel.removeAll();
		pnlMyShow.removeAll();
		String[][] arrStrSearchResults = clientController.searchShows(strSearchRequest);
		if (arrStrSearchResults != null) {
			pnlSearchResult.setLayout(new GridLayout(arrStrSearchResults.length, 2));
			System.out.println("Show found");
			updateSearchResults(arrStrSearchResults);

		} else {
			pnlSearchResult.setLayout(new BorderLayout());
			System.out.println("Show not found");
			strSearchRequest = "<html>" + "Your Search '" + strSearchRequest + "' was not found <br>" + "tips:<br>"
					+ "- Make sure all word are spelled correctly<br>" + "- Try different keywords<br>"
					+ "- or click the button below to create your own tracker =)" + "</html>";

			JLabel label = new JLabel("<html><font size = '3', padding-left: 50px>" + strSearchRequest + "</font></html>");

			label.setPreferredSize(new Dimension(pnlSearchResult.getWidth() - 5, pnlSearchResult.getHeight() / 2));

			ImageIcon imiAdd = new ImageIcon("images/notes-add.png");
			Image imgAdd = imiAdd.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			imiAdd = new ImageIcon(imgAdd);

			JButton btnCreateOwnShow = new JButton(imiAdd);

			btnCreateOwnShow.addActionListener(e -> drawNoSearchResultPanel());
			pnlSearchResult.add(label, BorderLayout.CENTER);
			pnlSearchResult.add(btnCreateOwnShow, BorderLayout.SOUTH);

		}

		spnSearchResult.setViewportView(pnlSearchResult);
		pnlSearchResult.revalidate();
	}

	/**
	 * Printing out the results from the search
	 * @param arrStrSearchResults The search terms
	 * Modified by Paul
	 * @version 1.0.3
	 */
	private void updateSearchResults(String[][] arrStrSearchResults) {
		pnlSearchResult.removeAll();
		pnlMyOwnMoviePanel.removeAll();
		pnlMyOwnMoviePanel.removeAll();
		pnlMyShow.removeAll();
		GridBagConstraints gbc = new GridBagConstraints();
		pnlSearchResult.setLayout(new GridBagLayout());
		gbc.fill = GridBagConstraints.HORIZONTAL;

		for (String[] arrStr : arrStrSearchResults) {
			if (arrStr[0] == null) { drawNoSearchResultPanel(); }

			JPanel pnlMainCard = new JPanel();

			pnlMainCard.setPreferredSize(new Dimension(800, 80));
			pnlMainCard.setBorder(BorderFactory.createRaisedBevelBorder());
			pnlMainCard.setLayout(new BorderLayout());
			pnlMainCard.setBackground(FontsAndColors.getProjectBlue());

			//Poster container
			JPanel pnlPoster = new JPanel(new BorderLayout());
			JLabel lblImage = new JLabel();

			//Poster
			BufferedImage image;
			try {
				URL url = new URL(arrStr[2]);
				image = ImageIO.read(url);
				Image dImg = image.getScaledInstance(50, 80, Image.SCALE_AREA_AVERAGING);
				ImageIcon imageIcon = new ImageIcon(dImg);
				lblImage.setIcon(imageIcon);
				pnlPoster.add(lblImage, BorderLayout.CENTER);
			} catch (Exception e){
				System.err.println("SearchShow | Row 166 | Poster Exception ");
			}

			// Add button
			ImageIcon icon = new ImageIcon("images/plus-2.png");
			Image imageAdd = icon.getImage();
			Image newAddImage = imageAdd.getScaledInstance(40,40, Image.SCALE_SMOOTH);
			icon = new ImageIcon(newAddImage);
			JButton btnAdd = new JButton(icon);
			btnAdd.setBorderPainted(false);
			btnAdd.addActionListener(new AddListener(arrStr[0], arrStr[1], arrStrSearchResults[0][4], btnAdd));
			btnAdd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

			//Result Label
			JLabel lblSearchResult = new JLabel("\t" + arrStr[0] + "\t" + "    IMDB Rating: " + arrStr[3]);
			lblSearchResult.setFont(FontsAndColors.getFontTitle(16));

			pnlMainCard.add(pnlPoster, BorderLayout.WEST);
			pnlMainCard.add(btnAdd, BorderLayout.EAST);
			pnlMainCard.add(lblSearchResult, BorderLayout.CENTER);

			gbc.gridx = 0;
			gbc.weightx = 1;

			pnlSearchResult.add(pnlMainCard, gbc);
			pnlSearchResult.setBackground(FontsAndColors.getProjectBlue());

		}

		JPanel panel = new JPanel();
		panel.setBackground(FontsAndColors.getProjectBlue());
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.weighty = 1;
		pnlSearchResult.add(panel, gbc);

	}




	// TODO: 2021-02-12 Denna metod kanske ska fixa om vi har tid, känns klumpig att visa om man inte hittar i OMDB  - PM
	/**
	 * Draws a panel for creating a show when no results are found
	 */
	private void drawNoSearchResultPanel() {
		pnlSearchResult.removeAll();
		pnlMyOwnShowPanel.removeAll();
		pnlSearchResult.setLayout(new BorderLayout());
		pnlMyOwnShowPanel.setLayout(new BoxLayout(pnlMyOwnShowPanel, BoxLayout.Y_AXIS));
		txfShowName.setText(txfSearchBar.getText());

		JButton btnSubmit = new JButton("Submit");

		JTextField txfNbrOfSeasons = new JTextField();



		pnlMyOwnShowPanel.add(new JLabel("Title: "));
		pnlMyOwnShowPanel.add(txfShowName);
		pnlMyOwnShowPanel.add(new JLabel("Released:"));
		pnlMyOwnShowPanel.add(txfShowYear);
		pnlMyOwnShowPanel.add(new JLabel("IMDB Rating: "));
		pnlMyOwnShowPanel.add(txfShowIMDBRating);
		pnlMyOwnShowPanel.add(new JLabel("Actors: "));
		pnlMyOwnShowPanel.add(txfShowActors);
		pnlMyOwnShowPanel.add(new JLabel("Plot:"));
		pnlMyOwnShowPanel.add(txfShowPlot);
		pnlMyOwnShowPanel.add(new JLabel("Poster (web url): "));
		pnlMyOwnShowPanel.add(txfShowPoster);



		pnlMyOwnShowPanel.add(new JLabel("Number of Seasons"));
		pnlMyOwnShowPanel.add(txfNbrOfSeasons);
		pnlMyOwnShowPanel.add(btnSubmit, BorderLayout.SOUTH);
		btnSubmit.addActionListener(e -> createMyOwnShowPanel(txfNbrOfSeasons.getText()));
		pnlSearchResult.add(pnlMyOwnShowPanel, BorderLayout.NORTH);
		spnSearchResult.setViewportView(pnlSearchResult);
	}


	/**
	 * Method for user to input their own movie via GUI
	 */
	private void createOwnMoviePanel(){
		pnlSearchResult.removeAll();
		pnlMyOwnShowPanel.removeAll();
		pnlMyOwnMoviePanel.removeAll();
		pnlSearchResult.setLayout(new BorderLayout());
		pnlMyOwnMoviePanel.setLayout(new BoxLayout(pnlMyOwnMoviePanel, BoxLayout.Y_AXIS));

		JTextField txfMovieTitle = new JTextField();
		txfMovieTitle.setText(txfSearchBar.getText());

		JTextField txfMovieYear = new JTextField();
		JTextField txfMovieIMDBRating = new JTextField();
		JTextField txfMoviePlot = new JTextField();
		JTextField txfMovieActors = new JTextField();
		JTextField txfMoviePosterURL = new JTextField();

		JButton btnCreate = new JButton("Create");

		pnlMyOwnMoviePanel.add(new JLabel("Title: "));
		pnlMyOwnMoviePanel.add(txfMovieTitle);
		pnlMyOwnMoviePanel.add(new JLabel("Released: "));
		pnlMyOwnMoviePanel.add(txfMovieYear);
		pnlMyOwnMoviePanel.add(new JLabel("IMDB Rating: "));
		pnlMyOwnMoviePanel.add(txfMovieIMDBRating);
		pnlMyOwnMoviePanel.add(new JLabel("Actors: "));
		pnlMyOwnMoviePanel.add(txfMovieActors);
		pnlMyOwnMoviePanel.add(new JLabel("Plot: "));
		pnlMyOwnMoviePanel.add(txfMoviePlot);
		pnlMyOwnMoviePanel.add(new JLabel("Poster (web url):"));
		pnlMyOwnMoviePanel.add(txfMoviePosterURL);

		pnlMyOwnMoviePanel.add(btnCreate, BorderLayout.SOUTH);



		btnCreate.addActionListener(e -> {
			saveOwnMovie(
					txfMovieTitle.getText(),
					txfMovieYear.getText(),
					txfMovieIMDBRating.getText(),
					txfMovieActors.getText(),
					txfMoviePlot.getText(),
					txfMoviePosterURL.getText()
			);
			txfMovieActors.setText("");
			txfMovieTitle.setText("");
			txfMovieYear.setText("");
			txfMovieIMDBRating.setText("");
			txfMoviePlot.setText("");
			txfMoviePosterURL.setText("");
			JOptionPane.showMessageDialog(null,"Movie successfully created!");

				}
		);

		pnlSearchResult.add(pnlMyOwnMoviePanel, BorderLayout.NORTH);
		spnSearchResult.setViewportView(pnlSearchResult);

	}

	/**
	 * Method  to save the users custom movie.
	 */
	private void saveOwnMovie(String title, String year, String imdbRating, String actors, String plot, String posterURL){

		Movie movie = new Movie(title);
		movie.setYear(year);
		movie.setImdbRating(imdbRating);
		movie.setActors(actors);
		movie.setPlot(plot);
		movie.setPoster(posterURL);
		clientController.getUser().addMovie(movie);

		clientController.updateUser(clientController.getUser());

		System.out.println("Custom movie saved");
	}


	/**
	 * Switches a show between adding it to the user's library and removing it
	 * @param strShowName The name of the show
	 * @param btnAdd The tracked button
	 * @param blnAdd Is show watched or not
	 */
	private void addRemoveShow(String strShowName, JButton btnAdd, boolean blnAdd) {
		if (!blnAdd) {

			ImageIcon icon = new ImageIcon("images/error.png");
			Image imageAdd = icon.getImage();
			Image newAddImage = imageAdd.getScaledInstance(40,40, Image.SCALE_SMOOTH);
			icon = new ImageIcon(newAddImage);
			btnAdd.setIcon(icon);
		} else {

			ImageIcon icon = new ImageIcon("images/plus-2.png");
			Image imageAdd = icon.getImage();
			Image newAddImage = imageAdd.getScaledInstance(40,40, Image.SCALE_SMOOTH);
			icon = new ImageIcon(newAddImage);
			btnAdd.setIcon(icon);

			clientController.getUser().removeShow(new Show(strShowName));
		}
	}

	/**
	 * Draws a panel where a user can set number of episodes per season in a show
	 * @param strInput The name of the show
	 */
	private void createMyOwnShowPanel(String strInput) {
		try {
			int intNbrOfSeasons = Integer.parseInt(strInput);
			GridBagConstraints gbc = new GridBagConstraints();
			pnlMyShow.setLayout(new GridBagLayout());
			gbc.fill = GridBagConstraints.HORIZONTAL;
			pnlMyShow.removeAll();

			JTextField[] arrTxfSeasons = new JTextField[intNbrOfSeasons];
			JButton btnCreate = new JButton("Create");

			for (int i = 0; i < intNbrOfSeasons; i++) {
				JPanel panel = new JPanel();
				panel.setPreferredSize(new Dimension(300, 40));
				panel.setLayout(new GridLayout(2, 1));
				JTextField tfNbrOfEpisodes = new JTextField();
				panel.add(new JLabel("Season" + (i + 1) + " :"));
				panel.add(tfNbrOfEpisodes);
				arrTxfSeasons[i] = tfNbrOfEpisodes; // sätter in varje textfield i en array
				gbc.gridx = 0;
				gbc.weightx = 1;
				pnlMyShow.add(panel, gbc);
			}

			btnCreate.addActionListener(e -> createMyShow(arrTxfSeasons));
			pnlMyShow.add(btnCreate, gbc);
			pnlSearchResult.add(pnlMyShow);
			spnSearchResult.setViewportView(pnlSearchResult);

			gbc.anchor = GridBagConstraints.NORTHWEST;
			gbc.weighty = 1;
			pnlMyShow.add(new JPanel(), gbc);

		} catch (Exception e) {
			System.out.println(strInput + " is not a valid integer number");
		}
	}

	/**
	 * Creates a show from the user's input
	 * @param arrTxfSeasons An array of JTextField containing the number of episodes per season
	 */
	private void createMyShow(JTextField[] arrTxfSeasons) {
		boolean blnParseIntSuccess = false;
		int[] arrIntEpisodes = new int[arrTxfSeasons.length];
		String strSeasons = "";
		try {
			for (int i = 0; i < arrTxfSeasons.length; i++) {
				strSeasons = arrTxfSeasons[i].getText();
				arrIntEpisodes[i] = Integer.parseInt(strSeasons);
			}
			blnParseIntSuccess = true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, strSeasons + " is not valid, please enter a number.");
		}
		if (blnParseIntSuccess) {
			Show show = new Show(txfShowName.getText(), true);
			show.setYear(txfShowYear.getText());
			show.setImdbRating(txfShowIMDBRating.getText());
			show.setActors(txfShowActors.getText());
			show.setDescription(txfShowPlot.getText());
			show.setPoster(txfShowPoster.getText());

			txfShowName.setText(null);
			txfShowYear.setText(null);
			txfShowIMDBRating.setText(null);
			txfShowActors.setText(null);
			txfShowPoster.setText(null);
			txfShowPlot.setText(null);

			for (int s = 0; s < arrTxfSeasons.length; s++)
				for (int e = 0; e < arrIntEpisodes[s]; e++)
					show.addEpisode(new Episode(show, e + 1, s + 1));

			show.sortEpisodes();
			clientController.getUser().addShow(show);
			clientController.updateUser(clientController.getUser());
			JOptionPane.showMessageDialog(null, "Show created successfully!");
		}
	}



	/**
	 * An inner class for toggling a to add or remove a show.
	 */
	private class AddListener implements ActionListener {
		private boolean blnAdd = true;
		private String strShowName;
		private String strShowId;
		private JButton btnAdd;
		private String type;

		AddListener(String strShowName, String strShowId, String type, JButton btnAdd) {
			this.strShowName = strShowName;
			this.strShowId = strShowId;
			this.btnAdd = btnAdd;
			this.type = type;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (blnAdd) {
				blnAdd = false;
				//checks if selected media type is series or movie
				if (type.equals("series")){
					clientController.generateShow(strShowName, strShowId);
				}
				else if (type.equals("movie")){
					clientController.generateMovie(strShowName, strShowId);
				}
			} else
				blnAdd = true;
			addRemoveShow(strShowName, btnAdd, blnAdd);
		}
	}

	/**
	 * An inner class for letting user's search wby pressing Enter
	 */
	private class EnterListener implements KeyListener {

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                e.consume();
                drawSearchResultPanel(txfSearchBar.getText());
            }
        }

        @Override
        public void keyReleased(KeyEvent arg0) {
        }

        @Override
        public void keyTyped(KeyEvent arg0) {
        }
    }
}