package showtracker.client;

import showtracker.Helper;
import showtracker.Movie;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;

public class MovieList extends JPanel {
    private ClientController clientController;
    private JPanel pnlMovieList = new JPanel();
    private JScrollPane scrollPane = new JScrollPane();


    //Constructor, input: ClientController instancce
    MovieList(ClientController clientController){
        this.clientController = clientController;
        pnlMovieList.setBackground(Color.decode("#6A86AA"));
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
        gbc.fill = GridBagConstraints.HORIZONTAL;
        pnlMovieList.setBackground(Color.decode("#6A86AA"));

        pnlMovieList.removeAll();
        if(movies.size() > 0){
            for(Movie movie : movies){
                JButton btnInfo = new JButton("Info");
                JButton btnRemove = new JButton("Remove");

                btnRemove.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                btnInfo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                JPanel pnlMiddle = new JPanel(new FlowLayout());
                JLabel label = new JLabel("Card Label");
                label.setFont(new Font("Monospaced", Font.BOLD, 18));
                label.setText(movie.getTitle());
                pnlMiddle.add(label);
                pnlMiddle.setBackground(Color.decode("#6A86AA"));

                JPanel pnlSouth = new JPanel(new FlowLayout());
                pnlSouth.add(btnInfo);
                pnlSouth.add(btnRemove);
                pnlSouth.setBackground(Color.decode("#6A86AA"));

                JPanel pnlMain = new JPanel(new BorderLayout());
                pnlMain.setPreferredSize(new Dimension(800, 80));
                Border cardBorder = BorderFactory.createRaisedBevelBorder();
                pnlMain.setBorder(cardBorder);
                pnlMain.add(pnlMiddle, BorderLayout.CENTER);
                pnlMain.add(pnlSouth, BorderLayout.SOUTH);
                pnlMain.setBackground(Color.decode("#6A86AA"));

               btnInfo.addActionListener(e -> JOptionPane.showMessageDialog(null, "<html><body>" +
                       "<p style = \"width: 300px;\">" + movie.getPlot() + "</p></body></html>", "Movie Info", JOptionPane.PLAIN_MESSAGE));
                btnRemove.addActionListener(e -> {
                    clientController.getUser().removeMovie(movie);
                    draw();
                });

                gbc.gridx = 0;
                gbc.weightx= 1;

                pnlMovieList.add(pnlMain, gbc);
            }
            gbc.anchor = GridBagConstraints.NORTHWEST;
            gbc.weighty = 1;
            pnlMovieList.add(new JPanel(), gbc);
        }else{
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
