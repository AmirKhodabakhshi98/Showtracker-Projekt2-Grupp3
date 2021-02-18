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
    void searchOMDBdbShowsOneWord() {
        String[][] array = new String[1][2];
        array[0][0] = "Friends";
        array[0][1] = "tt0108778";
        DatabaseReader databaseReader = new DatabaseReader();
        assertArrayEquals (array,databaseReader.searchOMDBdbShows("Friends"));

    }

    @org.junit.jupiter.api.Test
    void searchOMDBdbShowsAndSign() {
        String[][] array = new String[1][2];
        array[0][0] = "Love & Anarchy";
        array[0][1] = "tt10888876";
        DatabaseReader databaseReader = new DatabaseReader();
        assertArrayEquals (array,databaseReader.searchOMDBdbShows("Love & Anarchy"));
    }



    @org.junit.jupiter.api.Test
    void searchOMDBdbShowsEmptyString() {
        String[][] array = new String[1][2];
        array[0][0] = null;
        array[0][1] = null;
        DatabaseReader databaseReader = new DatabaseReader();
        assertArrayEquals(array,databaseReader.searchOMDBdbShows(""));
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