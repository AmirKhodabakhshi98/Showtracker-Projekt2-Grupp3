package showtracker.server;

import org.json.simple.JSONObject;
import showtracker.Episode;
import showtracker.Movie;
import showtracker.Show;

interface IDatabaseReader {
	String[][] searchOMDBdbShows(String strSearchTerms);

	JSONObject searchOmdbShow(String id);

	Show generateShow(String[] arShow);

	Show updateShow(Show show);

	String[] getDetail(String id);

	Movie generateMovie(String[] input);
}
