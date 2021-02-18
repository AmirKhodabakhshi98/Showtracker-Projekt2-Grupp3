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
    void searchOMDBdbShowsAndSign() {
        String[][] array = new String[1][2];
        array[0][0] = "Love & Anarchy";
        array[0][1] = "tt10888876";
        DatabaseReader databaseReader = new DatabaseReader();
        assertArrayEquals (array,databaseReader.searchOMDBdbShows("Love & Anarchy"));
    }

    @org.junit.jupiter.api.Test
    void searchOMDBdbShowsColon() {
        String[][] array = new String[1][2];
        array[0][0] = "Star Trek: Discovery";
        array[0][1] = "tt5171438";
        DatabaseReader databaseReader = new DatabaseReader();
        assertArrayEquals (array,databaseReader.searchOMDBdbShows("Star Trek: Discovery"));
    }

    @org.junit.jupiter.api.Test
    void searchOMDBdbShowsHyphen() {
        String[][] array = new String[1][2];
        array[0][0] = "9-1-1";
        array[0][1] = "tt7235466";
        DatabaseReader databaseReader = new DatabaseReader();
        assertArrayEquals (array,databaseReader.searchOMDBdbShows("9-1-1"));
    }

    @org.junit.jupiter.api.Test
    void searchOMDBdbShowsDot() {
        String[][] array = new String[1][2];
        array[0][0] = "Agents of S.H.I.E.L.D.";
        array[0][1] = "tt2364582";
        DatabaseReader databaseReader = new DatabaseReader();
        assertArrayEquals (array,databaseReader.searchOMDBdbShows("Agents of S.H.I.E.L.D."));
    }

    @org.junit.jupiter.api.Test
    void searchOMDBdbShowsSingleQuotation() {
        String[][] array = new String[1][2];
        array[0][0] = "The Queen's Gambit";
        array[0][1] = "tt10048342";
        DatabaseReader databaseReader = new DatabaseReader();
        assertArrayEquals(array,databaseReader.searchOMDBdbShows("The Queen's Gambit"));
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
    void searchOMDBdbShowsNumber() {
        String[][] array = new String[1][2];
        array[0][0] = "24";
        array[0][1] = "tt0285331";
        DatabaseReader databaseReader = new DatabaseReader();
        assertArrayEquals(array,databaseReader.searchOMDBdbShows("24"));
    }

    @org.junit.jupiter.api.Test
    void searchOMDBdbShowsIncorrectName() {
        String[][] array = new String[1][2];
        array[0][0] = null;
        array[0][1] = null;
        DatabaseReader databaseReader = new DatabaseReader();
        assertArrayEquals(array,databaseReader.searchOMDBdbShows("asdfgh"));
    }

    @org.junit.jupiter.api.Test
    void searchOMDBdbShowsSwedishLetter() {
        String[][] array = new String[1][2];
        array[0][0] = "Gösta";
        array[0][1] = "tt8456154";
        DatabaseReader databaseReader = new DatabaseReader();
        assertArrayEquals(array,databaseReader.searchOMDBdbShows("Gösta"));
    }


    @org.junit.jupiter.api.Test
    void searchOMDBdbShowsUppercase() {
        String[][] array = new String[1][2];
        array[0][0] = "Friends";
        array[0][1] = "tt0108778";
        DatabaseReader databaseReader = new DatabaseReader();
        assertArrayEquals(array,databaseReader.searchOMDBdbShows("FRIENDS"));
    }

    @org.junit.jupiter.api.Test
    void searchOMDBdbShowsLowercase() {
        String[][] array = new String[1][2];
        array[0][0] = "Friends";
        array[0][1] = "tt0108778";
        DatabaseReader databaseReader = new DatabaseReader();
        assertArrayEquals(array,databaseReader.searchOMDBdbShows("friends"));
    }

    @org.junit.jupiter.api.Test
    void searchOMDBdbShowsNull() {
        DatabaseReader databaseReader = new DatabaseReader();

        assertThrows(NullPointerException.class, () -> {
            databaseReader.searchOMDBdbShows(null);
        });
    }

}