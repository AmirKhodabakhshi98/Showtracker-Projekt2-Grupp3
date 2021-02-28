package showtracker.server;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseReaderTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void searchOMDBdbShowsAndSign() {
        String[][] array = new String[1][2];
        array[0][0] = "Love & Anarchy";
        array[0][1] = "tt10888876";
        DatabaseReader databaseReader = new DatabaseReader();
        assertArrayEquals (array,databaseReader.searchOMDBdbShows("Love & Anarchy"));
    }

    @Test
    void searchOMDBdbShowsColon() {
        String[][] array = new String[1][2];
        array[0][0] = "Star Trek: Discovery";
        array[0][1] = "tt5171438";
        DatabaseReader databaseReader = new DatabaseReader();
        assertArrayEquals (array,databaseReader.searchOMDBdbShows("Star Trek: Discovery"));
    }

    @Test
    void searchOMDBdbShowsHyphen() {
        String[][] array = new String[1][2];
        array[0][0] = "9-1-1";
        array[0][1] = "tt7235466";
        DatabaseReader databaseReader = new DatabaseReader();
        assertArrayEquals (array,databaseReader.searchOMDBdbShows("9-1-1"));
    }

    @Test
    void searchOMDBdbShowsDot() {
        String[][] array = new String[1][2];
        array[0][0] = "Agents of S.H.I.E.L.D.";
        array[0][1] = "tt2364582";
        DatabaseReader databaseReader = new DatabaseReader();
        assertArrayEquals (array,databaseReader.searchOMDBdbShows("Agents of S.H.I.E.L.D."));
    }

    @Test
    void searchOMDBdbShowsSingleQuotation() {
        String[][] array = new String[1][2];
        array[0][0] = "The Queen's Gambit";
        array[0][1] = "tt10048342";
        DatabaseReader databaseReader = new DatabaseReader();
        assertArrayEquals(array,databaseReader.searchOMDBdbShows("The Queen's Gambit"));
    }

    @Test
    void searchOMDBdbShowsEmptyString() {
        String[][] array = new String[1][2];
        array[0][0] = null;
        array[0][1] = null;
        DatabaseReader databaseReader = new DatabaseReader();
        assertArrayEquals(array,databaseReader.searchOMDBdbShows(""));
    }


    @Test
    void searchOMDBdbShowsNumber() {
        String[][] array = new String[1][2];
        array[0][0] = "24";
        array[0][1] = "tt0285331";
        DatabaseReader databaseReader = new DatabaseReader();
        assertArrayEquals(array,databaseReader.searchOMDBdbShows("24"));
    }

    @Test
    void searchOMDBdbShowsIncorrectName() {
        String[][] array = new String[1][2];
        array[0][0] = null;
        array[0][1] = null;
        DatabaseReader databaseReader = new DatabaseReader();
        assertArrayEquals(array,databaseReader.searchOMDBdbShows("asdfgh"));
    }

    @Test
    void searchOMDBdbShowsSwedishLetter() {
        String[][] array = new String[1][2];
        array[0][0] = "Gösta";
        array[0][1] = "tt8456154";
        DatabaseReader databaseReader = new DatabaseReader();
        assertArrayEquals(array,databaseReader.searchOMDBdbShows("Gösta"));
    }


    @Test
    void searchOMDBdbShowsUppercase() {
        String[][] array = new String[1][2];
        array[0][0] = "Friends";
        array[0][1] = "tt0108778";
        DatabaseReader databaseReader = new DatabaseReader();
        assertArrayEquals(array,databaseReader.searchOMDBdbShows("FRIENDS"));
    }

    @Test
    void searchOMDBdbShowsLowercase() {
        String[][] array = new String[1][2];
        array[0][0] = "Friends";
        array[0][1] = "tt0108778";
        DatabaseReader databaseReader = new DatabaseReader();
        assertArrayEquals(array,databaseReader.searchOMDBdbShows("friends"));
    }

    @Test
    void searchOMDBdbShowsNull() {
        DatabaseReader databaseReader = new DatabaseReader();

        assertThrows(NullPointerException.class, () -> {
            databaseReader.searchOMDBdbShows(null);
        });
    }

}