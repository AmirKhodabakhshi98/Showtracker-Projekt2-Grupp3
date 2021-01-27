package showtracker.server;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.apache.http.HttpResponse;
import org.json.simple.parser.JSONParser;
import showtracker.Episode;
import showtracker.Helper;
import showtracker.Show;

import java.io.*;

/**
 * @author Filip Spånberg
 * Changes made by Adam Joulak
 * @author paulmoustakas
 * 
 * DatabaseReader hanterar förfrågningar till TheTVDB
 */

class DatabaseReader {
    private String strToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NTY2MjUyOTksImlkIjoiU2hvd1RyYWNrZXIiLCJvcmlnX2lhdCI6MTU1NjUzODg5OSwidXNlcmlkIjo1MjQzMDIsInVzZXJuYW1lIjoiZmlsaXAuc3BhbmJlcmdxcnMifQ.NriC7481n32bFACSLLZwSAgf9Ll835_xHwxvuAHgTmqdYRs3RT0TJhetgCwRsCSNlRMmWYoROXOrYGCGLIz8izkMIS2_OwaygqiX4XBbYMwxjdcBtuhdhy-a34WureLEdGvqAUwx6tFNYWXH27x2evNGgbOMYFyN03idqQhyqHJBcXsRtAKD9NhmrL5R33y0O8jmXyu5QT-B0FWyGJ1dQ-15PK49feRauofZ1s72uaE_xTvwlyHSZbRTX5DiOtH8FZgNGMkqvARkR0B5MoqEat24-xUyjDb5VKNkhpr9oZsJwl_nZKMm8jZrKgPHHuZ6E4CUyip38EgbqPMipXqhMg";
    private String strLanguage = "en";

    /**
     * Gets an authentication token from TheTVDB
     * @return
     */
    String authenticateTheTVDB() {
        JSONObject obj = new JSONObject();

        obj.put("apikey", "BK2A524N2MT0IJWU");
        obj.put("username", "filip.spanbergqrs");
        obj.put("userkey", "J52T5FJR4CUESBPF");

        StringEntity stringEntity = new StringEntity(obj.toString(), ContentType.APPLICATION_JSON);

        HttpPost httpPost = new HttpPost("https://api.thetvdb.com/login");
        httpPost.setEntity(stringEntity);

        JSONObject jsoResponse = getJSONFromRequest(httpPost);

        strToken = (String) jsoResponse.get("token");
        return strToken;
    }

    /**
     * Sets the token
     * @param strToken
     */
    void setToken(String strToken) {
        this.strToken = strToken;
    }

    /**
     * Refreshes the token
     * @return
     */
    JSONObject refreshToken() {
        HttpGet request = createGet("https://api.thetvdb.com/refresh_token");
        JSONObject jso = getJSONFromRequest(request);
        return jso;
    }

    /**
     * Searches TheTVDB for shows
     * @param strSearchTerms String with search terms
     * @return A String array with name and ID from the shows found
     */
    String[][] searchTheTVDBShows(String strSearchTerms) {
        String[] strArrSearchTerms = strSearchTerms.split(" ");
        StringBuilder stbSearchTerms = new StringBuilder(strArrSearchTerms[0]);
        for (int i = 1; i < strArrSearchTerms.length; i++)
            stbSearchTerms.append("%20").append(strArrSearchTerms[i]);

        HttpGet httpGet = createGet("https://api.thetvdb.com/search/series?name=" + stbSearchTerms);
        JSONObject jsoResponse = getJSONFromRequest(httpGet);
        String strError = (String) jsoResponse.get("Error");
        if (strError == null) {
            JSONArray jsaResponse = (JSONArray) jsoResponse.get("data");

            String[][] shows = new String[jsaResponse.size()][2];
            for (int i = 0; i < jsaResponse.size(); i++) {
                shows[i][0] = (String) ((JSONObject) jsaResponse.get(i)).get("seriesName");
                shows[i][1] = Long.toString((Long) ((JSONObject) jsaResponse.get(i)).get("id"));
            }
            return shows;
        } else {
            System.out.println(strError);
            return null;
        }
    }

    /**
     * Searches TheTVDB for a single shows info
     * @param id
     * @return
     */
    JSONObject searchTheTVDBShow(String id) {
        HttpGet httpGet = createGet("https://api.thetvdb.com/series/" + id);
        JSONObject jsoResponse = (JSONObject) getJSONFromRequest(httpGet).get("data");

        return jsoResponse;
    }

    /**
     * Gets the episodes of a show
     * @param id The show's ID
     * @param page The page (Searches are limited to 100 episodes per page)
     * @return A JSON array with the episodes
     */
    private JSONArray getEpisodesOfShow(String id, int page) {
        HttpGet request = createGet("https://api.thetvdb.com/series/" + id + "/episodes?page=" + page);
        JSONObject joResponse = getJSONFromRequest(request);
        String strError = (String) joResponse.get("Error");
        if (strError == null) {
            JSONArray jsaResponse = (JSONArray) joResponse.get("data");
            return jsaResponse;
        } else {
            System.out.println(strError);
            return null;
        }
    }

    /**
     * Generates a Show object from a show's name and ID
     * @param arShow Name and ID of the show
     * @return A Show object
     */
    Show generateShow(String[] arShow) {
        System.out.println("DatabaseReader: Generating show \"" + arShow[0] + "\"...");
        JSONObject jsoShow = searchTheTVDBShow(arShow[1]);
        Show show = new Show((String) jsoShow.get("seriesName"));
        show.setDescription((String) jsoShow.get("overview"));
        show.setTvdbId(Long.toString((Long) jsoShow.get("id")));
        show.setImdbId((String) jsoShow.get("imdbId"));

        int intPage = 1;

        JSONArray jsaEpisodes = getEpisodesOfShow(arShow[1], intPage);

        do {
            System.out.println(jsaEpisodes);
            for (Object obj : jsaEpisodes) {
                JSONObject jso = (JSONObject) obj;

                int intSeason = ((Long) jso.get("airedSeason")).intValue();
                int intEpisode = ((Long) jso.get("airedEpisodeNumber")).intValue();
                String strName = (String) jso.get("episodeName");
                String strTvdbId = Long.toString((Long) jso.get("id"));
                String strImdbId = (String) jso.get("imdbId");
                String strDescription = Helper.decodeUnicode((String) jso.get("overview"));

                Episode episode = new Episode(show, intEpisode, intSeason);
                episode.setTvdbId(strTvdbId);
                episode.setImdbId(strImdbId);
                episode.setName(strName);
                episode.setDescription(strDescription);
                show.addEpisode(episode);
            }
            intPage++;
            jsaEpisodes = getEpisodesOfShow(arShow[1], intPage);
        } while (jsaEpisodes != null);

        show.sortEpisodes();
        System.out.println("DatabaseReader: Show created.");
        for (Episode episode: show.getEpisodes())
            System.out.print(episode.getName() + ", ");
        return show;
    }

    /**
     * Creates a get with standard settings
     * @param route Where to request the get from
     * @return
     */
    private HttpGet createGet(String route) {
        HttpGet httpGet = new HttpGet(route);
        httpGet.setHeader("Authorization", "Bearer " + strToken);
        httpGet.setHeader("Accept-Language", strLanguage);
        return httpGet;
    }

    /**
     * Send in a request, and receives a JSON object in return
     * @param request
     * @return
     */
    private JSONObject getJSONFromRequest(HttpUriRequest request) {
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
    Show updateShow(Show show) {
        String[] strArrSearchRequest = {show.getName(), show.getTvdbId()};
        Show shwLatest = generateShow(strArrSearchRequest);
        for (Episode episode: shwLatest.getEpisodes())
            if (!show.containsById(episode))
                show.addEpisode(episode);
        show.sortEpisodes();
        return show;
    }
}