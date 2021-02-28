package showtracker.server;

import org.json.simple.JSONObject;
import showtracker.Show;

interface IDatabaseReader {
	String[][] searchOMDBdbShows(String strSearchTerms);

	JSONObject searchOmdbShow(String id);

	Show generateShow(String[] arShow);

	Show updateShow(Show show);
}
