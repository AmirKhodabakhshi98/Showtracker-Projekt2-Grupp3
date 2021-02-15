package showtracker;

import java.io.Serializable;

/**
 * An Episode object
 */
public class Episode implements Comparable<Episode>, Serializable {
    private static final long serialVersionUID = -8815667314209923140L;
    private String tvdbId;
    private String imdbId;
    private String name;
    private double episodeNumber;
    private double seasonNumber;
    private String description;
    private boolean isWatched = false;
    private Show show;

    public Episode(Show show, int episodeNumber, int seasonNumber) {
        this.show = show;
        this.episodeNumber = episodeNumber;
        this.seasonNumber = seasonNumber;
    }

    public void setTvdbId(String id) {
        tvdbId = id;
    }

    public String getTvdbId() {
        return tvdbId;
    }

    public void setIMDBid(String id) {
        imdbId = id;
    }

    public String getImdbId() {
        return imdbId;
    }

    public String getName() {
        return name;
    }

    public double getEpisodeNumber() {
        return episodeNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Double getSeasonNumber() {
        return seasonNumber;
    }

    public boolean isWatched() {
        return isWatched;
    }

    public void setWatched(boolean isWatched) {
        this.isWatched = isWatched;
        show.setLastWatched();
    }

    /**
     * Compares this episode to an Object to see if they are the same
     * @param obj
     * @return
     */
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof Episode))
            return false;

        Episode e = (Episode) obj;

        return (e.getEpisodeNumber() == episodeNumber) && (e.getSeasonNumber() == seasonNumber);
    }

    /**
     * Compares this episode to another to see which comes first
     * @param episode
     * @return
     */
    @Override
    public int compareTo(Episode episode) {
        if (seasonNumber > episode.getSeasonNumber())
            return 1;
        else if (seasonNumber < episode.getSeasonNumber())
            return -1;
        else if (episodeNumber > episode.getEpisodeNumber())
            return 1;
        else if (episodeNumber < episode.getEpisodeNumber())
            return -1;
        else
            return 0;
    }
}