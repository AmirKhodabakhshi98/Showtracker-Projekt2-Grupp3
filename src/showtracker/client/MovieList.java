package showtracker.client;

import showtracker.Helper;
import showtracker.Movie;
import showtracker.Rating;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MovieList extends JPanel {

    private static final int colorCard1      = 0xF8F8F8;
    private static final int colorCard2      = 0xF2F2EF;
    private static final int colorBackground = 0xDCDBD8;
    private static final int colorTitle      = 0x6A86AA;

    private ClientController clientController;
    private JPanel pnlMovieList = new JPanel();
    private JScrollPane scrollPane = new JScrollPane();

    //Constructor, input: ClientController instancce
    MovieList(ClientController clientController){
        this.clientController = clientController;
        pnlMovieList.setBackground(Color.decode("#E3E2DD"));
        MyDocumentListener myDocumentListener = new MyDocumentListener();
        setLayout(new BorderLayout());
        add(myDocumentListener, BorderLayout.NORTH);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    //Refreshing the view
    public void draw(){
        draw(clientController.getUser().getMovies());
    }

    private void draw(ArrayList<Movie> movies){
        movies.sort(new Helper.NameComparatorMovie());
        GridBagConstraints gbc = new GridBagConstraints();
        pnlMovieList.setLayout(new GridBagLayout());
        pnlMovieList.setBackground(Color.decode("#DCDBD8"));
        pnlMovieList.removeAll();

        if(movies.size() > 0){
            Movie movie;
            Color colorMiddle;

            for(int i = 0; i < movies.size(); i++){
                movie = movies.get(i);

                if (i % 2 == 0)
                    colorMiddle = new Color(colorCard1);
                else
                    colorMiddle = new Color(colorCard2);

                JButton btnInfo = new JButton("Info");
                JButton btnRemove = new JButton("Remove");
                String[] rating = {"No rating","★","★★","★★★","★★★★","★★★★★"};
                JComboBox cb = new JComboBox(rating);

                if(movie.getPersonalRating() != null)
                    cb.setSelectedItem(movie.getPersonalRating().getStrValue());
                else
                    cb.setSelectedItem(rating);

                btnRemove.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                btnInfo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                cb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                JLabel label = new JLabel("Card Label");
                label.setForeground(new Color(colorTitle));
                label.setFont(new Font("Roboto", Font.BOLD, 18));
                label.setText(movie.getTitle());

                JPanel pnlMiddle = new JPanel(new FlowLayout());
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
                pnlMain.setBackground(new Color(colorBackground));

                //Poster container
                Border posterBorder = BorderFactory.createLineBorder(colorMiddle, 10, false);
                JLabel lblImage = new JLabel();
                lblImage.setBorder(posterBorder);
                JPanel pnlPoster = new JPanel(new BorderLayout());

                // Add poster if available
                if (movie.getPoster() != null &&
                    !movie.getPoster().isEmpty()
                ) {
                    BufferedImage image;
                    try {
                        URL url = new URL(movie.getPoster());
                        image = ImageIO.read(url);
                        Image dImg = image.getScaledInstance(96, 142, Image.SCALE_AREA_AVERAGING);
                        ImageIcon imageIcon = new ImageIcon(dImg);
                        lblImage.setIcon(imageIcon);
                        pnlPoster.add(lblImage, BorderLayout.WEST);
                    } catch (IOException e) {
                        System.err.println("Poster exception in class MovieList");
                    }
                }

                final Movie tmpMovie = movie;
                btnInfo.addActionListener(e -> showMovieInfo(tmpMovie));

                cb.addActionListener(e ->{
                    String personalRating = (String) cb.getSelectedItem();
                    tmpMovie.setPersonalRating(Rating.get(personalRating));
                    cb.setSelectedItem(personalRating);
                });
                btnRemove.addActionListener(e -> {
                    clientController.getUser().removeMovie(tmpMovie);
                    draw();
                });

                gbc.gridx = 0;
                gbc.weightx= 1;

                pnlMain.add(pnlPoster, BorderLayout.WEST);
                pnlMovieList.add(pnlMain, gbc);
            }
            gbc.anchor = GridBagConstraints.NORTHWEST;
            gbc.weighty = 1;
            pnlMovieList.add(new JPanel(), gbc);
        }
        else {
            pnlMovieList.add(new JLabel("   Nothing in your list of movies at the moment!"));
            pnlMovieList.add(new JLabel("             "));
            ImageIcon imi = new ImageIcon("images/Showtrack.png");
            Image image = imi.getImage().getScaledInstance(150, 150, Image.SCALE_AREA_AVERAGING);
            JLabel lblLogo = new JLabel(new ImageIcon(image));
            pnlMovieList.add(lblLogo);
        }

        scrollPane.setViewportView(pnlMovieList);
        scrollPane.setLayout(new ScrollPaneLayout());
        pnlMovieList.revalidate();
    }

    private void showMovieInfo(Movie movie) {
        JOptionPane.showMessageDialog(null, "<html><body>" +
                "<p style = \"width: 300px;\">" + movie.getPlot() + "</p><br>" +
                "Imdb Rating: " + movie.getImdbRating() + "</p><br>" +
                "Released: " + movie.getYear() + "</p><br>" +
                "Actors: " + movie.getActors() + "</body></html>",
                "Movie Info", JOptionPane.PLAIN_MESSAGE);
    }

    //Inner class for searching movies when a user writes in the search list
    private class MyDocumentListener extends JTextField implements DocumentListener{
        MyDocumentListener(){
            javax.swing.text.Document doc = this.getDocument();
            this.setPreferredSize(new Dimension(700,30));
            TextPrompt tp7 = new TextPrompt("Search Your Movie List", this);
            tp7.setForeground(Color.GRAY);
            tp7.changeAlpha(0.5f);
            tp7.changeStyle(Font.BOLD + Font.CENTER_BASELINE);
            doc.addDocumentListener(this);
            setBackground(Color.white);
            setBorder(new LineBorder(Color.black));
        }

        public void changedUpdate(DocumentEvent e){
            searchMovie();
        }

        public void insertUpdate(DocumentEvent e){
            searchMovie();
        }

        public void removeUpdate(DocumentEvent e){
            searchMovie();
        }

        //Method for getting the movies that matches the search terms
        private void searchMovie(){
            ArrayList<Movie> searchMovies = new ArrayList<Movie>();
            for(Movie movie: clientController.getUser().getMovies()){
                if(movie.getTitle().toLowerCase().contains(getText().toLowerCase())){
                    searchMovies.add(movie);
                }
            }
            pnlMovieList.removeAll();
            pnlMovieList.setBackground(Color.decode("#6A86AA"));
            draw(searchMovies);
        }
    }
}
