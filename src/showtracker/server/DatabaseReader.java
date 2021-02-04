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

import javax.swing.*;
import javax.xml.crypto.Data;
import java.io.*;
import java.util.ArrayList;
import java.util.Timer;

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

        System.out.println("authenthicate tvdb");

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


    private String apicode = "a203d499";

    String[][] searchTheTVDBShows(String strSearchTerms) {
        System.out.println("searchthetvdbshows string");

        String[] strArrSearchTerms = strSearchTerms.split(" ");
        StringBuilder stbSearchTerms = new StringBuilder(strArrSearchTerms[0]);
        for (int i = 1; i < strArrSearchTerms.length; i++)
            stbSearchTerms.append("+").append(strArrSearchTerms[i]);

        HttpGet httpGet = createGet("http://www.omdbapi.com/?apikey=" + apicode + "&t=" + stbSearchTerms);


        JSONObject jsoResponse = getJSONFromRequest(httpGet);


        String[][] test = new String[2][2];

        test[0][0] = "Title";
        test[0][1] = "imdbID";

        System.out.println((String) jsoResponse.get("Title") + "\t" + (String) jsoResponse.get("imdbID"));
        System.out.println("rjedlgksgs");

        test[1][0] = (String) jsoResponse.get("Title");
        test[1][1] = (String) jsoResponse.get("imdbID");

        System.out.println(test[0][0]);
        for (int i = 0; i< test.length; i++){

            for (int j= 0; j<test[i].length; j++){
                System.out.println(test[i][j]);
            }
        }

        return test;


    }

    private void test(){


        HttpGet httpGet = createGet("http://www.omdbapi.com/?apikey=a203d499&t=Friends");
        JSONObject jsoResponse = getJSONFromRequest(httpGet);
    //    System.out.println(jsoResponse);
        System.out.println(jsoResponse.get("Title"));
        System.out.println(jsoResponse.get("Year"));

    }

    public static void main(String[] args) {
        String [][] arr;
        DatabaseReader dbr = new DatabaseReader();
        //dbr.test();
        dbr.searchTheTVDBShows("License to kill");

    }

    /**
     * Searches TheTVDB for a single shows info
     * @param id
     * @return
     */
    JSONObject searchTheTVDBShow(String id) {

        System.out.println("searchthetvdbshow");


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



        System.out.println("episode of show");
        // HttpGet request = createGet("http://www.omdbapi.com/?apikey=a203d499&i=" + id + "&page=" + page); tog bort för testning, hårdkodat till friends
        //HttpGet request  = createGet("http://www.omdbapi.com/?apikey=a203d499&i=tt0108778&page=1");
        HttpGet request  = createGet("http://www.omdbapi.com/?apikey=a203d499&i=" + id +"&season=" + season);
        JSONObject joResponse = getJSONFromRequest(request);
        String strError = (String) joResponse.get("Error");
        if (strError == null) {
            JSONArray jsaResponse = (JSONArray) joResponse.get("Episodes");
            System.out.print("getEpisodesOfSeason " + jsaResponse);
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

        System.out.println("200, "+  jsoShow.get("Title"));

        Show show = new Show((String) jsoShow.get("Title"));

        show.setDescription((String) jsoShow.get("Plot"));

        show.setTvdbId(String.valueOf( jsoShow.get("imdbID"))); // planned to be removed

        show.setImdbId((String) jsoShow.get("imdbId"));


        int seasons = Integer.parseInt( String.valueOf(jsoShow.get("totalSeasons")));
        System.out.println(seasons);
        JSONArray jsaEpisodes;
        for (int i =1; i < seasons + 1; i++) {
            jsaEpisodes = getEpisodesOfSeason(arShow[1], i);
            System.out.println(jsaEpisodes.get(i));


            for (Object obj : jsaEpisodes) {
                JSONObject jso = (JSONObject) obj;

                int intSeason = Integer.parseInt(String.valueOf(i));
                int intEpisode = Integer.parseInt(String.valueOf(jso.get("Episode")));
                String strName = (String) jso.get("Title");
                String strTvdbId = (String.valueOf(jso.get("imdbID")));
                String strImdbId = (String.valueOf(jso.get("imdbID")));
                String strDescription = ((String) jso.get("Plot"));


                Episode episode = new Episode(show, intEpisode, intSeason);
                episode.setTvdbId(strTvdbId);
                episode.setImdbId(strImdbId);
                episode.setName(strName);
                episode.setDescription(strDescription);
                show.addEpisode(episode);
            }
        }





    //    do {


     //       }
      //      intPage++;
      //      jsaEpisodes = getEpisodesOfShow(arShow[1], intPage);
     //   } while (jsaEpisodes != null);

    //    show.sortEpisodes();
        System.out.println("DatabaseReader: Show created.");
   //     for (Episode episode: show.getEpisodes())
     //       System.out.print(episode.getName() + ", ");

        return show;
    }


    /**
     * Creates a get with standard settings
     * @param route Where to request the get from
     * @return
     */
    private HttpGet createGet(String route) {
        System.out.println("Create get");


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
        System.out.println("getjsonfromrequest");

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
        System.out.println("update show");

        String[] strArrSearchRequest = {show.getName(), show.getTvdbId()};
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