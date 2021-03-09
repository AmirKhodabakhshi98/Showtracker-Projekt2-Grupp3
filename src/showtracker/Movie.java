package showtracker;


import java.io.Serializable;

/**
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
    private String actors;
    private String personalRating;
    private boolean isWatched;

    public Movie(String title){
        this.title = title;
    }

    public Movie(String title,
                 String year,
                 String released,
                 String plot,
                 String poster,
                 String imdbId,
                 String imdbRating,
                 String boxOffice,
                 String metascore,
                 String actors) {

        this.title = title;
        this.year = year;
        this.released = released;
        this.plot = plot;
        this.poster = poster;
        this.imdbId = imdbId;
        this.imdbRating = imdbRating;
        this.boxOffice = boxOffice;
        this.metascore = metascore;
        this.actors = actors;
    }

    /**
     * Copy constructor
     */
    public Movie (Movie movie) {
        title           = movie.title;
        year            = movie.year;
        released        = movie.released;
        plot            = movie.plot;
        poster          = movie.poster;
        imdbId          = movie.imdbId;
        imdbRating      = movie.imdbRating;
        boxOffice       = movie.boxOffice;
        metascore       = movie.metascore;
        actors          = movie.actors;
        personalRating  = movie.personalRating;
        isWatched       = movie.isWatched;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getActors(){
        return actors;
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

    public String getImdbRating(){
        return imdbRating;
    }

    public void setImdbRating(){
        this.imdbRating = imdbRating;
    }

    public void setPoster(){
        this.poster = poster;
    }

    public String getPoster(){
        return poster;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getBoxOffice() {
        return boxOffice;
    }

    public void setBoxOffice(String boxOffice) {
        this.boxOffice = boxOffice;
    }

    public String getMetascore() {
        return metascore;
    }

    public void setMetascore(String metascore) {
        this.metascore = metascore;
    }

    public String getPersonalRating(){
        return personalRating;
    }

    public void setPersonalRating(String personalRating){
        this.personalRating = personalRating;
    }

    /**
     * Determines whether the information on the movie is complete
     * enough to be considered valid. This is to help prevent adding
     * movies that contain certain null or empty members.
     * @param movie the movie to check
     * @return true if valid, false if invalid
     * @author Kasper S. Skott
     */
    public static boolean isValid(Movie movie) {
        if (movie.title == null ||
            movie.imdbId == null)
            return false;

        if (movie.title.isEmpty() ||
            movie.imdbId.isEmpty())
            return false;

        return true;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Movie))
            return false;

        Movie movie = (Movie) other;
        return movie.getTitle().equals(title);
    }

}
