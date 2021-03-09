package showtracker;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kasper S. Skott
 */
public class UserTest {

	//------------------------------------------------------------------------//
	// These tests test part of CLFN02A (specification v3.6)
	//------------------------------------------------------------------------//

	@Test
	void addShow_null() {
		User usr = new User("TestUser", "user@test.com", null);

		usr.addShow(null);

		assertTrue(usr.getShows().isEmpty());
	}

	@Test
	void addShow_null_null() {
		User usr = new User("TestUser", "user@test.com", null);

		usr.addShow(null);
		usr.addShow(null);

		assertTrue(usr.getShows().isEmpty());
	}

	@Test
	void addShow_valid() {
		User usr = new User("TestUser", "user@test.com", null);
		Show show = new Show("Test Show");
		show.setImdbId("tt0000000");
		show.setDescription("Test description.");

		usr.addShow(show);

		assertEquals(show, usr.getShows().get(0));
	}

	@Test
	void addShow_valid_null() {
		User usr = new User("TestUser", "user@test.com", null);
		Show show = new Show("Test Show");
		show.setImdbId("tt0000000");
		show.setDescription("Test description.");

		usr.addShow(show);
		usr.addShow(null);

		assertEquals(1, usr.getShows().size());
	}

	@Test
	void addShow_same() {
		User usr = new User("TestUser", "user@test.com", null);
		Show show = new Show("Test Show");
		show.setImdbId("tt0000000");
		show.setDescription("Test description.");

		usr.addShow(show);
		usr.addShow(show);

		assertEquals("Test Show", usr.getShows().get(0).getName());
		assertEquals("Test Show (1)", usr.getShows().get(1).getName());
	}

	@Test
	void addShow_null_content() {
		User usr = new User("TestUser", "user@test.com", null);
		Show show = new Show(null);

		usr.addShow(show);

		assertTrue(usr.getShows().isEmpty());
	}

	//------------------------------------------------------------------------//
	// These tests test part of CLFN10A (specification v3.6)
	//------------------------------------------------------------------------//

	@Test
	void removeShow_null() {
		User usr = new User("TestUser", "user@test.com", null);

		Show show = new Show("Test Show");
		show.setImdbId("tt0000000");
		show.setDescription("Test description.");

		usr.addShow(show);

		usr.removeShow(null);

		assertEquals(1, usr.getShows().size());
	}

	@Test
	void removeShow_valid() {
		User usr = new User("TestUser", "user@test.com", null);

		Show show = new Show("Test Show");
		show.setImdbId("tt0000000");
		show.setDescription("Test description.");

		usr.addShow(show);

		usr.removeShow(show);

		assertFalse(usr.containsShow(show));
	}

	@Test
	void removeShow_nonexistent() {
		User usr = new User("TestUser", "user@test.com", null);

		Show show = new Show("Test Show");
		show.setImdbId("tt0000000");
		show.setDescription("Test description.");

		usr.addShow(show);
		int size = usr.getShows().size();

		usr.removeShow(new Show("1dj4s3dha90s8d12d"));

		assertEquals(size, usr.getShows().size());
	}

	@Test
	void removeShow_null_content() {
		User usr = new User("TestUser", "user@test.com", null);

		Show show = new Show("Test Show");
		show.setImdbId("tt0000000");
		show.setDescription("Test description.");

		usr.addShow(show);
		int size = usr.getShows().size();

		usr.removeShow(new Show(null));

		assertEquals(size, usr.getShows().size());
	}

	//------------------------------------------------------------------------//
	// These tests test part of CLFN01 (specification v3.6)
	//------------------------------------------------------------------------//

	@Test
	void setWatched() {
		User usr = new User("TestUser", "user@test.com", null);

		Show show = new Show("Test Show");
		show.setImdbId("tt0000000");
		show.setDescription("Test description.");

		Episode episode = new Episode(show, 1, 1);
		show.addEpisode(episode);

		usr.addShow(show);

		episode = usr.getShows().get(0).getEpisode(1, 1);
		episode.setWatched(true);

		assertNotNull(episode);
		assertTrue(usr.getShows().get(0).getEpisode(1, 1).isWatched());
	}

	@Test
	void setNotWatched() {
		User usr = new User("TestUser", "user@test.com", null);

		Show show = new Show("Test Show");
		show.setImdbId("tt0000000");
		show.setDescription("Test description.");

		Episode episode = new Episode(show, 1, 1);
		episode.setWatched(true);
		show.addEpisode(episode);

		usr.addShow(show);

		episode = usr.getShows().get(0).getEpisode(1, 1);
		episode.setWatched(false);

		assertNotNull(episode);
		assertFalse(usr.getShows().get(0).getEpisode(1, 1).isWatched());
	}

	//------------------------------------------------------------------------//
	// These tests test part of CLFN02B (specification v3.6)
	//------------------------------------------------------------------------//

	@Test
	void addMovie_null() {
		User usr = new User("TestUser", "user@test.com", null);

		usr.addMovie(null);

		assertTrue(usr.getMovies().isEmpty());
	}

	@Test
	void addMovie_null_null() {
		User usr = new User("TestUser", "user@test.com", null);

		usr.addMovie(null);
		usr.addMovie(null);

		assertTrue(usr.getMovies().isEmpty());
	}

	@Test
	void addMovie_valid() {
		User usr = new User("TestUser", "user@test.com", null);
		Movie movie = new Movie("Test Movie");

		usr.addMovie(movie);

		assertEquals(movie, usr.getMovies().get(0));
	}

	@Test
	void addMovie_valid_null() {
		User usr = new User("TestUser", "user@test.com", null);
		Movie movie = new Movie("Test Movie");

		usr.addMovie(movie);
		usr.addMovie(null);

		assertEquals(1, usr.getMovies().size());
	}

	@Test
	void addMovie_same() {
		User usr = new User("TestUser", "user@test.com", null);
		Movie movie = new Movie("Test Movie");

		usr.addMovie(movie);
		usr.addMovie(new Movie("Test Movie"));

		assertEquals("Test Movie", usr.getMovies().get(0).getTitle());
		assertEquals("Test Movie (1)", usr.getMovies().get(1).getTitle());
	}

	@Test
	void addMovie_null_content() {
		User usr = new User("TestUser", "user@test.com", null);
		Movie movie = new Movie("Test Movie");

		usr.addMovie(movie);

		assertTrue(usr.getMovies().isEmpty());
	}

	//------------------------------------------------------------------------//
	// These tests test part of CLFN10B (specification v3.6)
	//------------------------------------------------------------------------//

	@Test
	void removeMovie_null() {
		User usr = new User("TestUser", "user@test.com", null);

		usr.addMovie(new Movie("Test Movie"));

		usr.removeMovie(null);

		assertEquals(1, usr.getMovies().size());
	}

	@Test
	void removeMovie_valid() {
		User usr = new User("TestUser", "user@test.com", null);

		Movie movie = new Movie("Test Movie");
		usr.addMovie(movie);

		usr.removeMovie(movie);

		assertFalse(usr.containsMovie(movie));
	}

	@Test
	void removeMovie_nonexistent() {
		User usr = new User("TestUser", "user@test.com", null);

		Movie movie = new Movie("Test Movie");
		usr.addMovie(movie);

		int size = usr.getMovies().size();

		usr.removeMovie(new Movie("1dj4s3dha90s8d12d"));

		assertEquals(size, usr.getMovies().size());
	}

	@Test
	void removeMovie_null_content() {
		User usr = new User("TestUser", "user@test.com", null);

		Movie movie = new Movie("Test Movie");
		usr.addMovie(movie);

		int size = usr.getMovies().size();

		usr.removeMovie(new Movie(null));

		assertEquals(size, usr.getMovies().size());
	}

	//------------------------------------------------------------------------//

}
