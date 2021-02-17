package showtracker.server;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseReaderTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void authenticateTheTVDB() {
    }

    @org.junit.jupiter.api.Test
    void setToken() {
    }

    @org.junit.jupiter.api.Test
    void refreshToken() {
    }

    @org.junit.jupiter.api.Test
    void searchOMDBdbShows() {
        String[][] array = new String[1][2];
        array[0][0] = "Friends";
        array[0][1] = "tt0108778";
        DatabaseReader databaseReader = new DatabaseReader();
  //      databaseReader.searchOMDBdbShows("Friends");
        assertArrayEquals (array,databaseReader.searchOMDBdbShows("Friends"));

    }
    @org.junit.jupiter.api.Test
    void searchOMDBdbShowsNull() {
        String[][] array = new String[1][2];
        array[0][0] = null;
        array[0][1] = null;
        DatabaseReader databaseReader = new DatabaseReader();
        //      databaseReader.searchOMDBdbShows("Friends");
//        assertArrayEquals (array,databaseReader.searchOMDBdbShows("Friends"));
        assertArrayEquals(array,databaseReader.searchOMDBdbShows(""));
    }

    //Testar för serie med "totalseason: N/A"
    @org.junit.jupiter.api.Test
    void searchOMDBdbShowsTotalSeasonNA() {
        String[][] array = new String[1][2];
        array[0][0] = "Blacklist";
        array[0][1] = "tt9892494";
        DatabaseReader databaseReader = new DatabaseReader();
        //      databaseReader.searchOMDBdbShows("Friends");
//        assertArrayEquals (array,databaseReader.searchOMDBdbShows("Friends"));
        assertArrayEquals(array,databaseReader.searchOMDBdbShows("Blacklist"));
    }



    @org.junit.jupiter.api.Test
    void main() {
    }

    @org.junit.jupiter.api.Test
    void searchTheTVDBShow() {
    }

    @org.junit.jupiter.api.Test
    void generateShow() {
    }

    @org.junit.jupiter.api.Test
    void updateShow() {
    }

    @org.junit.jupiter.api.Test
    void searchTheTVDBShow2() {

    }

}