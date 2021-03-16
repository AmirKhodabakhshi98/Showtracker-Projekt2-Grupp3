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
    private ArrayList<ArrayList<Episode>> episodes; // [season][episode]
    private Date dteLastWatched;
    private String actors;
    private String year;
    private int personalRating;
    private boolean custom;

    public Show(String strName) {
        this.strName = strName;
        custom = false;
        setLastWatched();
    }

    public Show(String strName, boolean customFlag) {
        this.strName = strName;
        custom = customFlag;
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
        dteLastWatched  = other.dteLastWatched;
        actors          = other.actors;
        year            = other.year;
        personalRating  = other.personalRating;

        if (other.episodes != null) {
            episodes = new ArrayList<>(other.getSeasons());
            for (int s = 0; s < other.getSeasons(); s++) {
                ArrayList<Episode> eps = new ArrayList<>(
                        other.episodes.get(s).size());

                for (int e = 0; e < other.episodes.get(s).size(); e++) {
                    eps.add(e, other.episodes.get(s).get(e));
                }

                episodes.add(eps);
            }
        }
    }

    public void setActors(String actors){
        this.actors = actors;
    }

    public String getActors(){
        return actors;
    }

    public boolean isCustom()  {return custom; }

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

    public Rating getPersonalRating() {
        return Rating.get(personalRating);
    }

    public void setPersonalRating(Rating personalRating) {
        this.personalRating = personalRating.getIntValue();
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
            show.year == null)
            return false;

        if (show.strName.isEmpty() ||
            show.year.isEmpty())
            return false;

        return true;
    }

    public void addEpisode(Episode episode, int seasonNbr, int episodeNbr) {
        if (episodes == null) {
            episodes = new ArrayList<>(seasonNbr);
            episodes.add(new ArrayList<Episode>(episodeNbr));
        }

        if (seasonNbr > getSeasons())
            for (int i = getSeasons(); i < seasonNbr; i++) {
                episodes.add(new ArrayList<Episode>());
            }

        episodes.get(seasonNbr-1).add(episodeNbr-1, episode);
    }

    public ArrayList<ArrayList<Episode>> getEpisodes() {
        return episodes;
    }

    public void setDescription(String strDescription) {
        this.strDescription = strDescription;
    }

    public String getDescription() {
        return strDescription;
    }

    public void setCustom(boolean value)
    {
        custom = value;
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
     * Returns the number of seasons
     * @return
     */
    public int getSeasons() {
        if (episodes == null)
            return 0;

        return episodes.size();
    }

    /**
     * Get an episode with its season and episode number
     * @param season
     * @param episode
     * @return
     */
    public Episode getEpisode(int season, int episode) {
        int seasons = getSeasons();
        if (season <= 0 || season > seasons)
            return null;

        if (episode <= 0 || episode > episodes.get(season).size())
            return null;

        return episodes.get(season-1).get(episode-1);
    }

    /**
     * Return the first episode that is unwatched
     * @return
     */
    public Episode getFirstUnwatched() {
        int seasonLen;
        Episode ep;
        for (int i = 0; i < getSeasons(); i++) {
            seasonLen = episodes.get(i).size();

            for (int j = 0; j < seasonLen; j++) {
                ep = episodes.get(i).get(j);

                if (!ep.isWatched())
                    return ep;
            }
        }

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
     * @param id the IMDB id
     * @return
     */
    public boolean containsById(String id) {
        int seasonLen;
        Episode ep;
        for (int i = 0; i < getSeasons(); i++) {
            seasonLen = episodes.get(i).size();

            for (int j = 0; j < seasonLen; j++) {
                ep = episodes.get(i).get(j);

                if (ep.getImdbId().equals(id))
                    return true;
            }
        }

        return false;
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