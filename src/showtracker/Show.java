package showtracker;

import java.io.Serializable;
import java.util.*;

/**
 * A class for keeping track on a show
 */
public class Show implements Serializable {
    private static final long serialVersionUID = -7641780883231752094L;
    private String poster;
    private String strImdbId;
    private String imdbRating;
    private String strName;
    private String strDescription;
    private LinkedList<Episode> episodes = new LinkedList<>();
    private Date dteLastWatched;
    private String actors;
    private String year;

    public Show(String strName) {
        this.strName = strName;
        setLastWatched();
    }

    /**
     * Copy constructor
     */
    public Show(Show other) {
        poster          = other.poster;
        strImdbId       = other.strImdbId;
        imdbRating      = other.imdbRating;
        strName         = other.strName;
        strDescription  = other.strDescription;
        episodes        = (LinkedList<Episode>)other.episodes.clone();
        dteLastWatched  = other.dteLastWatched;
        actors          = other.actors;
        year            = other.year;
    }

    public void setActors(String actors){
        this.actors = actors;
    }

    public String getActors(){
        return actors;
    }

    public void setYear(String year){
        this.year = year;
    }

    public String getYear(){
        return year;
    }

    public void setPoster(String Poster){
        this.poster = Poster;
    }

    public String getPoster(){
        return poster;
    }

    public void setImdbRating(String ImdbRating){
        this.imdbRating = ImdbRating;
    }

    public String getImdbRating(){
        return imdbRating;
    }

    public void setImdbId(String strImdbId) {
        this.strImdbId = strImdbId;
    }

    public String getImdbId() {
        return strImdbId;
    }

    public void setName(String strName) {
        this.strName = strName;
    }

    public String getName() {
        return strName;
    }

    /**
     * Determines whether the information on the show is complete
     * enough to be considered valid. This is to help prevent adding
     * shows that contain certain null or empty members.
     * @param show the show to check
     * @return true if valid, false if invalid
     * @author Kasper S. Skott
     */
    public static boolean isValid(Show show) {
        if (show.strName == null ||
            show.strImdbId == null)
            return false;

        if (show.strName.isEmpty() ||
            show.strImdbId.isEmpty())
            return false;

        return true;
    }

    public void addEpisode(Episode episode) {
        episodes.add(episode);
    }

    public LinkedList<Episode> getEpisodes() {
        return episodes;
    }

    /**
     * Sort episodes by season and episode number
     */
    public void sortEpisodes() {
        Collections.sort(episodes);
    }

    public void setDescription(String strDescription) {
        this.strDescription = strDescription;
    }

    public String getDescription() {
        return strDescription;
    }

    /**
     * Saving when a user last watched a show
     */
    public void setLastWatched() {
        dteLastWatched = Calendar.getInstance().getTime();
    }

    public Date getLastWatched() {
        return dteLastWatched;
    }

    /**
     * Returns a list of doubles with the amount of seasons the Show has
     * @return
     */
    public LinkedList<Double> getSeasons() {
        LinkedList<Double> seasons = new LinkedList<>();
        for (Episode episode : episodes)
            if (!seasons.contains(episode.getSeasonNumber()))
                seasons.add(episode.getSeasonNumber());
        Collections.sort(seasons);
        return seasons;
    }

    /**
     * Return the episodes of a single season
     * @param
     * @return
     */
    public LinkedList<Episode> getSeason(double dbl) {
        LinkedList<Episode> season = new LinkedList<>();
        for (Episode episode : episodes)
            if (episode.getSeasonNumber() == dbl)
                season.add(episode);
        Collections.sort(season);
        return season;
    }

    /**
     * Get an episode with its season and episode number
     * @param dblSeason
     * @param dblEpisode
     * @return
     */
    public Episode getEpisode(double dblSeason, double dblEpisode) {
        for (Episode episode : episodes)
            if (episode.getSeasonNumber() == dblSeason && episode.getEpisodeNumber() == dblEpisode)
                return episode;
        return null;
    }

    /**
     * Return the first episode that is unwatched
     * @return
     */
    public Episode getFirstUnwatched() {
        for (Episode episode : episodes)
            if (!episode.isWatched() && episode.getSeasonNumber() != 0)
                return episode;
        return null;
    }

    /**
     * Compares the this show to an Object by name
     * @param obj
     * @return
     */
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof Show))
            return false;

        Show show = (Show) obj;

        return show.getName().equals(strName);
    }

    /**
     * Checks in an episode is present by its ID
     * @param episode
     * @return
     */
    public boolean containsById(Episode episode) {
        return episodes.stream().anyMatch(listItem -> (new IdComparator()).compare(listItem, episode) == 0);
    }

    /**
     * Compares to Episodes by ID
     */
    private class IdComparator implements Comparator<Episode> {

        @Override
        public int compare(Episode episode1, Episode episode2) {
            return (Integer.parseInt(episode1.getTvdbId()) - Integer.parseInt(episode2.getTvdbId()));
        }
    }
}