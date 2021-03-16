package showtracker;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.*;

/**
 * @author Filip Spånberg
 * Represents a user
 */
public class User implements Serializable {

    private static final long serialVersionUID = -6358452193067562790L;
    private SerializableImage profilePicture = null;
    private String strUserName, strUserEmail;

    private ArrayList<Show> shows = new ArrayList<>();
    private ArrayList<Movie> movies = new ArrayList<>();

    public User(String strUserName, String strUserEmail, String strImagePath) {
        this.strUserName = strUserName;
        if (strImagePath != null)
            this.profilePicture = new SerializableImage(strImagePath);
        this.strUserEmail = strUserEmail;
    }

    public void setUserName(String strUserName) {
        this.strUserName = strUserName;
    }

    public void setRuntime(Movie movie, int watchedTime)
    {
        for (int i = 0; i < movies.size() ; i++)
        {
            if (movies.get(i).getTitle().equals(movie.getTitle()))
            {
                movies.get(i).setMinutesWatched(watchedTime);
            }
        }
        movie.setMinutesWatched(watchedTime);
    }

    public String getMinutesWatched(Movie movie)
    {
        for (int i = 0; i < movies.size() ; i++)
        {
            if (movies.get(i).getTitle().equals(movie.getTitle()))
            {
               return movies.get(i).getMinutesWatched() + " min";
            }
        }
        return "";
    }

    public String getUserName() {
        return strUserName;
    }

    public void setProfilePicture(String strImagePath) {
        this.profilePicture = new SerializableImage(strImagePath);
    }

    public void setEmail(String strUserEmail) {
        this.strUserEmail = strUserEmail;
    }

    public String getEmail() {
        return strUserEmail;
    }

    public ImageIcon getProfilePicture() {
        if (profilePicture != null)
            return profilePicture.getImageIcon();
        else
            return null;
    }

    /**
     * Adds a show to the user's library
     * @param show
     */
    public void addShow(Show show) {
        if (show == null || !Show.isValid(show))
            return;

        show = new Show(show);
        if (shows.contains(show)) {
            int i = 1;
            String newName;
            do {
                newName = show.getName() + " (" + i++ + ")";
            } while (shows.contains(new Show(newName)));
            do {
                newName = JOptionPane.showInputDialog("A show with that name "+
                        "already exists, please enter a new name.", newName);
            } while (shows.contains(new Show(newName)));
            if (newName != null)
                show.setName(newName);
        }

        shows.add(show);
    }

    public void addMovie(Movie movie){
        if (movie == null || !Movie.isValid(movie))
            return;

        movie = new Movie(movie);
        if (movies.contains(movie)){
            int i = 1;
            String newName;
            do {
                newName = movie.getTitle() + " (" + i++ + ")";
            } while (movies.contains(new Movie(newName)));

            do {
                newName = JOptionPane.showInputDialog("A movie with that name "+
                        "already exists, please enter a new name.", newName);
            } while (movies.contains(new Movie(newName)));

            if (newName != null)
                movie.setTitle(newName);
        }

        movies.add(movie);
    }

    /**
     * Updates a Show in the User's library
     * @param show
     */
    public void updateShow(Show show) {
        for (int i = 0; i < shows.size(); i++) {
            if (shows.get(i).equals(show)) {
                shows.set(i, new Show(show));
                break;
            }
        }
    }

    /**
     * Removes a Show from the User's library
     * @param show
     */
    public void removeShow(Show show) {
        shows.remove(show);
    }

    public void removeMovie(Movie movie){
        movies.remove(movie);
    }

    public ArrayList<Show> getShows() {
        return shows;
    }

    public ArrayList<Movie> getMovies(){
        return movies;
    }

    /**
     * Checks if a User's library contains a show
     * @param show
     * @return
     */
    public boolean containsShow(Show show) {
        return shows.contains(show);
    }

    public boolean containsMovie(Movie movie){
        return movies.contains(movie);
    }
}