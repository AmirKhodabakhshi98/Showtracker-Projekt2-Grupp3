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
		show.setYear("2000");

		usr.addShow(show);

		assertEquals(show, usr.getShows().get(0));
	}

	@Test
	void addShow_valid_null() {
		User usr = new User("TestUser", "user@test.com", null);
		Show show = new Show("Test Show");
		show.setYear("2000");

		usr.addShow(show);
		usr.addShow(null);

		assertEquals(1, usr.getShows().size());
	}

	@Test
	void addShow_same() {
		User usr = new User("TestUser", "user@test.com", null);
		Show show = new Show("Test Show");
		show.setYear("2000");

		usr.addShow(show);
		usr.addShow(show);

		assertEquals("Test Show", usr.getShows().get(0).getName());
		assertEquals("Test Show (1)", usr.getShows().get(1).getName());
	}

	@Test
	void addShow_null_content() {
		User usr = new User("TestUser", "user@test.com", null);
		Show show = new Show((String)null);

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
		show.setYear("2000");

		usr.addShow(show);

		usr.removeShow(null);

		assertEquals(1, usr.getShows().size());
	}

	@Test
	void removeShow_valid() {
		User usr = new User("TestUser", "user@test.com", null);

		Show show = new Show("Test Show");
		show.setYear("2000");

		usr.addShow(show);

		usr.removeShow(show);

		assertFalse(usr.containsShow(show));
	}

	@Test
	void removeShow_nonexistent() {
		User usr = new User("TestUser", "user@test.com", null);

		Show show = new Show("Test Show");
		show.setYear("2000");

		usr.addShow(show);
		int size = usr.getShows().size();

		usr.removeShow(new Show("1dj4s3dha90s8d12d"));

		assertEquals(size, usr.getShows().size());
	}

	@Test
	void removeShow_null_content() {
		User usr = new User("TestUser", "user@test.com", null);

		Show show = new Show("Test Show");
		show.setYear("2000");

		usr.addShow(show);
		int size = usr.getShows().size();

		usr.removeShow(new Show((String)null));

		assertEquals(size, usr.getShows().size());
	}

	//------------------------------------------------------------------------//
	// These tests test part of CLFN01 (specification v3.6)
	//------------------------------------------------------------------------//

//	@Test
//	void setWatched() {
//		User usr = new User("TestUser", "user@test.com", null);
//
//		Show show = new Show("Test Show");
//		show.setYear("2000");
//
//		usr.addShow(show);
//
//		Episode episode = new Episode(show, 1,2);
//		show.addEpisode(episode, 1,2);
//
//		episode = usr.getShows().get(0).getEpisode(1, 2);
//		episode.setWatched(true);
//
//		assertNotNull(episode);
//		assertTrue(usr.getShows().get(0).getSeasons() != 0);
//	}
//
//	@Test
//	void setNotWatched() {
//		User usr = new User("TestUser", "user@test.com", null);
//
//		Show show = new Show("Test Show");
//		show.setYear("2000");
//
//		Episode episode = new Episode(show, 1, 1);
//		episode.setWatched(true);
//
//		usr.addShow(show);
//
//		episode = usr.getShows().get(0).getEpisode(1, 1);
//		episode.setWatched(false);
//
//		assertNotNull(episode);
//		assertFalse(usr.getShows().get(0).getEpisode(1, 1).isWatched());
//	}

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
		movie.setYear("2000");

		usr.addMovie(movie);

		assertEquals(movie.getTitle(), usr.getMovies().get(0).getTitle());
	}

	@Test
	void addMovie_valid_null() {
		User usr = new User("TestUser", "user@test.com", null);
		Movie movie = new Movie("Test Movie");
		movie.setYear("2000");

		usr.addMovie(movie);
		usr.addMovie(null);

		assertEquals(1, usr.getMovies().size());
	}

	@Test
	void addMovie_same() {
		User usr = new User("TestUser", "user@test.com", null);
		Movie movie = new Movie("Test Movie");
		movie.setYear("2000");

		usr.addMovie(movie);

		movie = new Movie("Test Movie");
		movie.setYear("2000");

		usr.addMovie(movie);

		assertEquals("Test Movie", usr.getMovies().get(0).getTitle());
		assertEquals("Test Movie (1)", usr.getMovies().get(1).getTitle());
	}

	@Test
	void addMovie_null_content() {
		User usr = new User("TestUser", "user@test.com", null);
		Movie movie = new Movie("Test Movie");
		movie.setYear(null);

		usr.addMovie(movie);

		assertTrue(usr.getMovies().isEmpty());
	}

	//------------------------------------------------------------------------//
	// These tests test part of CLFN10B (specification v3.6)
	//------------------------------------------------------------------------//

	@Test
	void removeMovie_null() {
		User usr = new User("TestUser", "user@test.com", null);

		Movie movie = new Movie("Test Movie");
		movie.setYear("2000");
		usr.addMovie(movie);

		usr.removeMovie(null);

		assertEquals(1, usr.getMovies().size());
	}

	@Test
	void removeMovie_valid() {
		User usr = new User("TestUser", "user@test.com", null);

		Movie movie = new Movie("Test Movie");
		movie.setYear("2000");
		usr.addMovie(movie);

		usr.removeMovie(movie);

		assertFalse(usr.containsMovie(movie));
	}

	@Test
	void removeMovie_nonexistent() {
		User usr = new User("TestUser", "user@test.com", null);

		Movie movie = new Movie("Test Movie");
		movie.setYear("2000");
		usr.addMovie(movie);

		int size = usr.getMovies().size();

		usr.removeMovie(new Movie("1dj4s3dha90s8d12d"));

		assertEquals(size, usr.getMovies().size());
	}

	@Test
	void removeMovie_null_content() {
		User usr = new User("TestUser", "user@test.com", null);

		Movie movie = new Movie("Test Movie");
		movie.setYear("2000");
		usr.addMovie(movie);

		int size = usr.getMovies().size();

		usr.removeMovie(new Movie((String)null));

		assertEquals(size, usr.getMovies().size());
	}


	//------------------------------------------------------------------------//
	// These tests test part of CLFN06B (specification v3.8)
	//------------------------------------------------------------------------//

	@Test
	void rateMovie_invalid(){
		assertEquals(Rating.NO_RATING, Rating.get(null));
		assertEquals(Rating.NO_RATING, Rating.get(8));
		assertEquals(Rating.NO_RATING, Rating.get(-1));
	}

	@Test
	void rateMovie_valid(){
		assertEquals(Rating.NO_RATING, Rating.get(0));
		assertEquals(Rating.ONE_STAR, Rating.get(1));
		assertEquals(Rating.TWO_STARS, Rating.get(2));
		assertEquals(Rating.THREE_STARS, Rating.get(3));
		assertEquals(Rating.FOUR_STARS, Rating.get(4));
		assertEquals(Rating.FIVE_STARS, Rating.get(5));


	}

	//------------------------------------------------------------------------//
	// These tests test part of CLFN21 (specification v3.8)
	//------------------------------------------------------------------------//

	@Test
	void createMovie(){
		User usr = new User("TestUser", "user@test.com", null);

		Movie movie = new Movie("Test Movie");
		movie.setPlot("Film om Paul");
		movie.setYear("2021");
		movie.setActors("Brad Pitt");
		movie.setImdbId("tt990021002");
		movie.setImdbRating("9.6");
		movie.setPoster("https://www.bclulea.se/countdown-7/");
		usr.addMovie(movie);

		assertEquals("Film om Paul", movie.getPlot());
		assertEquals("2021", movie.getYear());
		assertEquals("Brad Pitt", movie.getActors());
		assertEquals("tt990021002", movie.getImdbId());
		assertEquals("9.6", movie.getImdbRating());
		assertEquals("https://www.bclulea.se/countdown-7/", movie.getPoster());
	}




}
