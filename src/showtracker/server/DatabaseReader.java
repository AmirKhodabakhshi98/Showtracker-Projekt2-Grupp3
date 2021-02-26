package showtracker.server;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.apache.http.HttpResponse;
import org.json.simple.parser.JSONParser;
import showtracker.Episode;
import showtracker.Show;

import java.io.*;

/**
 * Modification GRP 3
 * Database reader handles queries to the OMDB API
 * @version 1.0.1
 */

class DatabaseReader implements IDatabaseReader {
    private String apicode = "a203d499";


    /**
     * Searches OMdb for shows
     * @param strSearchTerms String with search terms
     * @return A String matrix with name and ID from the shows found
     */ // TODO: 2021-02-09 Could, if time left, improve the search function with separators for better search hits Paul M.
    @Override
    public String[][] searchOMDBdbShows(String strSearchTerms) {

        String[] strArrSearchTerms = strSearchTerms.split(" ");
        StringBuilder stbSearchTerms = new StringBuilder(strArrSearchTerms[0]);

        for (int i = 1; i < strArrSearchTerms.length; i++)
            stbSearchTerms.append("+").append(strArrSearchTerms[i]);

        HttpGet httpGet = createGet("http://www.omdbapi.com/?apikey=" + apicode + "&t=" + stbSearchTerms);
        JSONObject jsoResponse = getJSONFromRequest(httpGet);

        String [][] show = new String[1][5];

        show [0][0] = (String) jsoResponse.get("Title");
        show [0][1] = (String) jsoResponse.get("imdbID");
        show [0][2] = (String) jsoResponse.get("Poster");
        show [0][3] = (String) jsoResponse.get("imdbRating");
        show [0][4] = (String) jsoResponse.get("Type");

        
        return show;
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
        JSONObject jsoResponse = (JSONObject) getJSONFromRequest(httpGet);

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
        String strError = (String) joResponse.get("Error");
        if (strError == null) {
            return (JSONArray) joResponse.get("Episodes");
        } else {
            System.err.println(strError);
            return null;
        }
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
        System.out.println("Title: "+  jsoShow.get("Title"));

        Show show = new Show((String) jsoShow.get("Title"));
        show.setDescription((String) jsoShow.get("Plot"));
        show.setImdbId((String) jsoShow.get("imdbId"));
//        show.setTvdbId(String.valueOf( jsoShow.get("imdbID"))); // planned to be removed // TEMP REMOVED


        int seasons = Integer.parseInt( String.valueOf(jsoShow.get("totalSeasons")));
        System.out.println("Total seasons: " + seasons);
        JSONArray jsaEpisodes;
        for (int i =1; i < seasons + 1; i++) {

            jsaEpisodes = getEpisodesOfSeason(arShow[1], i);

            for (Object obj : jsaEpisodes) {
                JSONObject jso = (JSONObject) obj;

                int intSeason = Integer.parseInt(String.valueOf(i));
                int intEpisode = Integer.parseInt(String.valueOf(jso.get("Episode")));
                String strName = (String) jso.get("Title");
                String strTvdbId = (String.valueOf(jso.get("imdbID")));  // Remove?
                String strIMDBid = (String.valueOf(jso.get("imdbID")));
                String strDescription = ((String) jso.get("Plot"));

                Episode episode = new Episode(show, intEpisode, intSeason);
                episode.setTvdbId(strTvdbId); // Remove or update for use from OMDB?
                episode.setIMDBid(strIMDBid);
                episode.setName(strName);
                episode.setDescription(strDescription);
                show.addEpisode(episode);
            }
        }
        System.out.println("DatabaseReader: Show created.");
        return show;
    }

    String[] generateMovie(String[] input){

        System.out.println("db generateMovie");
        JSONObject jsoShow = searchOmdbShow(input[1]);
        System.out.println("jsoShow created");

        String[] output = new String[9];

        output[0] = (String) jsoShow.get("Title");
        output[1] = (String) jsoShow.get("Year");
        output[2] = (String) jsoShow.get("Released");
        output[3] = (String) jsoShow.get("Plot");
        output[4] = (String) jsoShow.get("Poster");
        output[5] = (String) jsoShow.get("imdbID");
        output[6] = (String) jsoShow.get("imdbRating");
        output[7] = (String) jsoShow.get("BoxOffice");
        output[8] = (String) jsoShow.get("Metascore");

        System.out.println("collected movie info");

        return output;



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
        JSONParser parser = new JSONParser();

        try {
            HttpResponse response = httpClient.execute(request);
            InputStreamReader isr = new InputStreamReader(response.getEntity().getContent());
            BufferedReader bfr = new BufferedReader(isr);
            String strLine = bfr.readLine();
            jsoResponse = (JSONObject) parser.parse(strLine);
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