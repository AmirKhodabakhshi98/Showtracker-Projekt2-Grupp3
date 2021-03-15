package showtracker.server;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import showtracker.Episode;
import showtracker.Movie;
import showtracker.Show;

import java.io.*;

/**
 * Modification GRP 3
 * Database reader handles queries to the OMDB API
 * @version 1.0.1
 */

class DatabaseReader implements IDatabaseReader {

    private static final String apicode = "a203d499";

    /**
     * Searches OMdb for shows
     * @param strSearchTerms String with search terms
     * @return A 2D String array with the following fields
     * [0] Title
     * [1] imdbID
     * [2] Poster (may be null)
     * [3] Year
     * [4] Type (series/movie)
     */
    @Override
    public String[][] searchOMDBdbShows(String strSearchTerms) {

        String[] strArrSearchTerms = strSearchTerms.split(" ");
        System.out.println(strSearchTerms);
        StringBuilder stbSearchTerms = new StringBuilder(strArrSearchTerms[0]);

        //Used for changing '&' chars to '%26' //Edvin
        for(int i = 0; i <strArrSearchTerms.length; i++){
            strArrSearchTerms[i] = strArrSearchTerms[i].replaceAll("&", "%26");
        }

        for (int i = 1; i < strArrSearchTerms.length; i++)
            stbSearchTerms.append("+").append(strArrSearchTerms[i]);

        HttpGet httpGet = createGet("http://www.omdbapi.com/?apikey="+apicode+
                "&v=1&page=1&s=" + stbSearchTerms);
        JSONObject jsoResponse = getJSONFromRequest(httpGet);
        JSONArray jsonArray = jsoResponse.optJSONArray("Search");
        if (jsonArray == null)
            return null;

        JSONObject obj;
        String[][] list = new String[jsonArray.length()][];
        for (int i = 0; i < jsonArray.length(); i++) {
            obj = jsonArray.optJSONObject(i);
            if (obj == null)
                break;

            list[i] = new String[5];
            list[i][0] = obj.optString("Title", null);
            list[i][1] = obj.optString("imdbID", null);
            list[i][2] = obj.optString("Poster", null);
            list[i][3] = obj.optString("Year", null);
            list[i][4] = obj.optString("Type", null);
        }

        return list;
    }

    /**
     * Searches OMDB for a single show info
     * @param id In query
     * @return query Json Object
     */
    @Override
    public JSONObject searchOmdbShow(String id) {
        System.out.println("Search Query OMDB: " + id);

        HttpGet httpGet = createGet("http://www.omdbapi.com/?apikey=a203d499&i=" + id);
        JSONObject jsoResponse = getJSONFromRequest(httpGet);

        return jsoResponse;
    }

    /**
     * Gets the episodes of a show
     * @param id The show's ID
     * @param season The season of the show
     * @return A JSON array with the episodes
     */
    private JSONArray getEpisodesOfSeason(String id, int season) {
        HttpGet request  = createGet("http://www.omdbapi.com/?apikey=a203d499&i=" + id +"&season=" + season);
        JSONObject joResponse = getJSONFromRequest(request);
        String strError = joResponse.optString("Error", null);
        if (strError == null) {
            return joResponse.optJSONArray("Episodes");
        } else {
            System.err.println(strError);
            return null;
        }
    }

    /**
     * searches with imdb id to find more info
     * info about runtime, plot and poster.
     * @param id the id of the show/episode/movie
     * @return an array of more info
     */
    public String[] getDetail(String id)
    {
        JSONObject jsoEpisode = searchOmdbShow(id);
        String runtime = jsoEpisode.optString("Runtime", null);
        String plot = jsoEpisode.optString("Plot", null);
        String poster = jsoEpisode.optString("Poster", null);
        String[] returnValues = new String[3];
        returnValues[0] = runtime;
        returnValues[1] = plot;
        returnValues[2] = poster;

        return returnValues;
    }


    /**
     * Generates a Show object from a show's name and ID
     * @param arShow Name and ID of the show
     * @return A Show object
     */
    @Override
    public Show generateShow(String[] arShow) {

        System.out.println("DatabaseReader: Generating show \"" + arShow[0] + "\"...");

        JSONObject jsoShow = searchOmdbShow(arShow[1]);
        System.out.println("Title: "+  jsoShow.opt("Title"));

        Show show = new Show(jsoShow.optString("Title", null));
        show.setDescription(jsoShow.optString("Plot", null));
        show.setImdbId(jsoShow.optString("imdbID", null));
        show.setImdbRating(jsoShow.optString("imdbRating", null));
        show.setPoster(jsoShow.optString("Poster", null));
        show.setActors(jsoShow.optString("Actors", null));
        show.setYear(jsoShow.optString("Year", null));

        try {

            int seasons = Integer.parseInt(String.valueOf(
                    jsoShow.optString("totalSeasons", "0")));
            System.out.println("Total seasons: " + seasons);
            JSONArray jsaEpisodes;
            for (int i = 1; i < seasons + 1; i++) {

                jsaEpisodes = getEpisodesOfSeason(arShow[1], i);
                if (jsaEpisodes != null) {
                    for (Object obj : jsaEpisodes) {
                        JSONObject jso = (JSONObject) obj;

                        int intSeason = Integer.parseInt(String.valueOf(i));
                        int intEpisode = Integer.parseInt(String.valueOf(
                                jso.optString("Episode", "1")));
                        String strName = jso.optString("Title", null);
                        String strIMDBid = jso.optString("imdbID", null);
                        //String strDescription = ((String) jso.get("Plot"));

                        Episode episode = new Episode(show, intEpisode, intSeason);
                        episode.setIMDBid(strIMDBid);
                        episode.setName(strName);
                        System.out.println(strName);
                        //episode.setDescription(strDescription);
                        show.addEpisode(episode);
                    }
                }
            }
        } catch (Exception e){
            //Do nothing
        }
        System.out.println("DatabaseReader: Show created.");
        return show;
    }


    public Movie generateMovie(String[] input){
        System.out.println("db generateMovie");
        JSONObject jsoShow = searchOmdbShow(input[1]);
        System.out.println("jsoShow created");

        return new Movie(
                jsoShow.optString("Title", null),
                jsoShow.optString("Year", null),
                jsoShow.optString("Released", null),
                jsoShow.optString("Plot", null),
                jsoShow.optString("Poster", null),
                jsoShow.optString("imdbID", null),
                jsoShow.optString("imdbRating", null),
                jsoShow.optString("BoxOffice", null),
                jsoShow.optString("Metascore", null),
                jsoShow.optString("Actors", null),
                jsoShow.optString("Runtime", "not found"));

    }


    /**
     * Creates a get with standard settings
     * @param route Where to request the get from
     * @return Http Route
     */
    private HttpGet createGet(String route) {
        HttpGet httpGet = new HttpGet(route);
        httpGet.setHeader("Accept-Language", "en");

        return httpGet;
    }


    /**
     * Send in a request, and receives a JSON object in return
     * @param request Http request to API address
     * @return
     */
    private JSONObject getJSONFromRequest(HttpUriRequest request) {
        System.out.println(getClass().getSimpleName() + "| Row 153 | getJsonFromRequest");

        JSONObject jsoResponse = null;
        HttpClient httpClient = HttpClientBuilder.create().build();

        try {
            HttpResponse response = httpClient.execute(request);
            InputStreamReader isr = new InputStreamReader(response.getEntity().getContent());
            BufferedReader bfr = new BufferedReader(isr);
            String strLine = bfr.readLine();
            jsoResponse = new JSONObject(strLine);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsoResponse;
    }


    /**
     * Update a Show with new episodes
     * @param show
     * @return
     */
    @Override
    public Show updateShow(Show show) {
        System.out.println("update show");

        String[] strArrSearchRequest = {show.getName()}; //show.getTvdbId()
        Show shwLatest = generateShow(strArrSearchRequest);
        /*
        for (Episode episode: shwLatest.getEpisodes())
            if (!show.containsById(episode))
                show.addEpisode(episode);
        show.sortEpisodes();

         */

        return show;
    }

}