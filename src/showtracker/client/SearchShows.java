package showtracker.client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.text.NumberFormat;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.NumberFormatter;

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
		spnSearchResult.setViewportView(pnlSearchResult);

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

		pnlSearchResult.setLayout(new GridLayout(arrStrSearchResults.length, 2));
		updateSearchResults(arrStrSearchResults);
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

		// No results found
		if (arrStrSearchResults[0] == null ||
			arrStrSearchResults[0][0] == null
		) {
			JOptionPane.showMessageDialog(this,
					"No results found. "+
					"You may create it yourself if you wish.",
					"Not found", JOptionPane.PLAIN_MESSAGE);
		}
		else { // Results found
			for (String[] arrStr : arrStrSearchResults) {
				JPanel pnlMainCard = new JPanel();

				pnlMainCard.setPreferredSize(new Dimension(800, 80));
				pnlMainCard.setBorder(BorderFactory.createRaisedBevelBorder());
				pnlMainCard.setLayout(new BorderLayout());

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
				} catch (Exception e) {
					System.err.println("SearchShow | Poster Exception ");
				}

				// Add button
				boolean showExists;
				if (arrStr[4].equals("series"))
					showExists = clientController.getUser()
							.containsShow(new Show(arrStr[0]));
				else
					showExists = clientController.getUser()
							.containsMovie(new Movie(arrStr[0]));

				Image imageAdd;
				if (showExists)
					imageAdd = new ImageIcon("images/error.png").getImage();
				else
					imageAdd = new ImageIcon("images/plus-2.png").getImage();

				Image newAddImage = imageAdd.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
				ImageIcon icon = new ImageIcon(newAddImage);
				JButton btnAdd = new JButton(icon);
				btnAdd.setBorderPainted(true);
				btnAdd.addActionListener(new AddListener(
						arrStr[0], arrStr[1], arrStr[4], btnAdd, !showExists));

				btnAdd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

				//Result Label
				String showLabel = "\t" + arrStr[0];
				if (arrStr[3] != null)
					showLabel += " ("+arrStr[3]+")";

				JLabel lblSearchResult = new JLabel(showLabel);
				lblSearchResult.setFont(FontsAndColors.getFontTitle(16));

				pnlMainCard.add(pnlPoster, BorderLayout.WEST);
				pnlMainCard.add(btnAdd, BorderLayout.EAST);
				pnlMainCard.add(lblSearchResult, BorderLayout.CENTER);

				gbc.gridx = 0;
				gbc.weightx = 1;

				pnlSearchResult.add(pnlMainCard, gbc);
			}
		}

		JPanel panel = new JPanel();
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.weighty = 1;
		pnlSearchResult.add(panel, gbc);
		pnlSearchResult.revalidate();
	}

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

		NumberFormatter formatter =
				new NumberFormatter(NumberFormat.getInstance());
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(1);
		formatter.setMaximum(Integer.MAX_VALUE);
		formatter.setAllowsInvalid(true);

		// Make sure it only accepts a certain range of integers.
		JFormattedTextField txfNbrOfSeasons = new JFormattedTextField(formatter);
		txfNbrOfSeasons.setValue(1);

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
	private void addRemoveShow(String strShowName, String type,
							   JButton btnAdd, boolean blnAdd) {
		if (!blnAdd) {
			ImageIcon icon = new ImageIcon("images/error.png");
			Image imageAdd = icon.getImage();
			Image newAddImage = imageAdd.getScaledInstance(40,40, Image.SCALE_SMOOTH);
			icon = new ImageIcon(newAddImage);
			btnAdd.setIcon(icon);
		}
		else {
			ImageIcon icon = new ImageIcon("images/plus-2.png");
			Image imageAdd = icon.getImage();
			Image newAddImage = imageAdd.getScaledInstance(40,40, Image.SCALE_SMOOTH);
			icon = new ImageIcon(newAddImage);
			btnAdd.setIcon(icon);

			if (type.equals("series"))
				clientController.getUser().removeShow(new Show(strShowName));
			else
				clientController.getUser().removeMovie(new Movie(strShowName));
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
		private boolean blnAdd;
		private final String strShowName;
		private final String strShowId;
		private final JButton btnAdd;
		private final String type;

		AddListener(String strShowName, String strShowId, String type,
					JButton btnAdd, boolean add) {
			this.strShowName = strShowName;
			this.strShowId = strShowId;
			this.btnAdd = btnAdd;
			this.type = type;
			blnAdd = add;
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
			}
			else {
				blnAdd = true;
			}

			addRemoveShow(strShowName, type, btnAdd, blnAdd);
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