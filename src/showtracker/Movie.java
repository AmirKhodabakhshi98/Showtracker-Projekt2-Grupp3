package showtracker;


import java.io.Serializable;
import java.util.Timer;

/**
 *
 *
 * A class for saving a movie
 */

public class Movie implements Serializable {

    private static final long serialVersionUID = -7641780883231752094L;
    private String title;
    private String year;
    private String released;
    private String plot;
    private String poster;
    private String imdbId;
    private String imdbRating;
    private String boxOffice;
    private String metascore;

    private boolean isWatched;


    public Movie(String title){
        this.title=title;
    }


    public Movie(String title,
                 String year,
                 String released,
                 String plot,
                 String poster,
                 String imdbId,
                 String imdbRating,
                 String boxOffice,
                 String metascore) {

        this.title = title;
        this.year = year;
        this.released = released;
        this.plot = plot;
        this.poster = poster;
        this.imdbId = imdbId;
        this.imdbRating = imdbRating;
        this.boxOffice = boxOffice;
        this.metascore = metascore;
    }

    public void setWatched(boolean isWatched) {
        this.isWatched = isWatched;
    }
    public boolean isWatched(){
        return isWatched;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title= title;
    }

    public String getPlot(){
        return plot;
    }

    public void setPlot(String plot){
        this.plot = plot;
    }



}
