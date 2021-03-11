package showtracker.server;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import showtracker.Envelope;
import showtracker.Movie;
import showtracker.Show;
import showtracker.User;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kasper S. Skott
 */
public class ControllerTest {

	private static final String TEST_POSTER_URL = "https://m.media-amazon.com/"+
		"images/M/MV5BNDVkYjU0MzctMWRmZi00NTkxLTgwZWEtOWVhYjZlYjllYmU4XkEyXkFq"+
		"cGdeQXVyNTA4NzY1MzY@._V1_SX300.jpg";

	private Controller controller;
	private TestDatabaseReader dbr;
	private TestConnection connection;

	@BeforeEach
	void beforeEach() {
		dbr = new TestDatabaseReader();
		connection = new TestConnection();
		controller = new Controller(dbr, connection);
		controller.startConnection(1);
	}

	@AfterEach
	void afterEach() {
		controller.stopConnection();
		controller = null;
		connection = null;
		dbr = null;
	}

	////////////////////////////////////////////////////////////////////////////

	@Test
	void receiveEmpty() {
		Envelope in = new Envelope(null, "");
		Envelope result = controller.receiveEnvelope(in);

		assertNull(result);
	}

	@Test
	void receiveNull() {
		Envelope in = new Envelope(null, null);
		Envelope result = controller.receiveEnvelope(in);

		assertNull(result);
	}

	@Test
	void receiveInvalid() {
		Envelope in = new Envelope(null, "SENDING INVALID TYPE");
		Envelope result = controller.receiveEnvelope(in);

		assertNull(result);
	}

	//------------------------------------------------------------------------//

	@Test
	void receiveSearchShows_type() {
		Envelope in = new Envelope("Friends", "searchShows");
		Envelope result = controller.receiveEnvelope(in);

		assertTrue(result.getContent() instanceof String[][]);
	}

	@Test
	void receiveSearchShows_null() {
		Envelope in = new Envelope(null, "searchShows");
		Envelope result = controller.receiveEnvelope(in);

		assertNull(result.getContent());
	}

	@Test
	void receiveSearchShows_existing() {
		Envelope in = new Envelope("Friends", "searchShows");
		Envelope result = controller.receiveEnvelope(in);

		assertEquals("tt0108778", ((String[][])result.getContent())[0][1]);
	}

	@Test
	void receiveSearchShows_nonexisting() {
		Envelope in = new Envelope("d908sa6hd1d02", "searchShows");
		Envelope result = controller.receiveEnvelope(in);

		assertNull(((String[][])result.getContent())[0][0]);
	}

	//------------------------------------------------------------------------//

	@Test
	void receiveGetShow_type() {
		String[] arrStr = {"Friends", "tt0108778"};
		Envelope in = new Envelope(arrStr, "getShow");
		Envelope result = controller.receiveEnvelope(in);

		assertTrue(result.getContent() instanceof Show);
	}

	@Test
	void receiveGetShow_null() {
		Envelope in = new Envelope(null, "getShow");
		Envelope result = controller.receiveEnvelope(in);
		Show show = (Show) result.getContent();

		assertNull(show.getImdbId());
	}

	@Test
	void receiveGetShow_existing() {
		String[] arrStr = {"Friends", "tt0108778"};
		Envelope in = new Envelope(arrStr, "getShow");
		Envelope result = controller.receiveEnvelope(in);
		Show show = (Show) result.getContent();

		assertEquals("tt0108778", show.getImdbId());
	}

	@Test
	void receiveGetShow_nonexisting() {
		String[] arrStr = {"d908sa6hd1d02", "df85h1jds1f9ds2"};
		Envelope in = new Envelope(arrStr, "getShow");
		Envelope result = controller.receiveEnvelope(in);
		Show show = (Show) result.getContent();

		assertNull(show.getImdbId());
	}

	//------------------------------------------------------------------------//

	@Test
	void receiveCheckName_type() {
		Envelope in = new Envelope(null, "checkName");
		Envelope result = controller.receiveEnvelope(in);
		String name = result.getContent().getClass().getName();


		assertTrue(result.getContent() instanceof Boolean);
	}

	@Test
	void receiveCheckName_null() {
		Envelope in = new Envelope(null, "checkName");
		Envelope result = controller.receiveEnvelope(in);

		assertEquals(false, result.getContent());
	}

	@Test
	void receiveCheckName_nonexisting() {
		// Make sure this username doesn't exist!
		Envelope in = new Envelope("dc724af18fbdd4e59189f5fe768a5f8311527050", "checkName");
		Envelope result = controller.receiveEnvelope(in);

		assertEquals(false, result.getContent());
	}

	@Test
	void receiveCheckName_existing() {
		// Sign up in case the user doesn't exist
		String[] arrStrUserInfo = {"TestUser", "Test1Password", "user@test.org"};
		Envelope usrEnv = controller.signUp(arrStrUserInfo);

		Envelope in = new Envelope("TestUser", "checkName");
		Envelope result = controller.receiveEnvelope(in);

		assertEquals(true, result.getContent());
	}

	//------------------------------------------------------------------------//

	@Test
	void receiveLogIn_type() {
		// Sign up in case the user doesn't exist
		String[] arrStrUserInfo = {"TestUser1", "Test1Password", "user1@test.org"};
		Envelope usrEnv = controller.signUp(arrStrUserInfo);

		String[] arrStrUser = {"TestUser1", "Test1Password"};
		Envelope in = new Envelope(arrStrUser, "logIn");
		Envelope result = controller.receiveEnvelope(in);

		assertTrue(result.getContent() instanceof User);
	}

	@Test
	void receiveLogIn_null() {
		Envelope in = new Envelope(null, "logIn");
		Envelope result = controller.receiveEnvelope(in);

		assertNull(result.getContent());
	}

	@Test
	void receiveLogIn_nonexisting() {
		// Make sure this username doesn't exist!
		String[] arrStrUser = {"dc724af18fbdd4e59189f5fe768a5f8311527050",
				"dcr24bf18fe5d4e29189f5fe7q3a5f8311567250"};
		Envelope in = new Envelope(arrStrUser, "logIn");
		Envelope result = controller.receiveEnvelope(in);

		assertNull(result.getContent());
	}

	@Test
	void receiveLogIn_existing() {
		// Sign up in case the user doesn't exist
		String[] arrStrUserInfo = {"TestUser1", "Test1Password", "user1@test.org"};
		Envelope usrEnv = controller.signUp(arrStrUserInfo);

		String[] arrStrUser = {"TestUser1", "Test1Password"};
		Envelope in = new Envelope(arrStrUser, "logIn");
		Envelope result = controller.receiveEnvelope(in);

		assertNotNull(result.getContent());
		assertEquals("TestUser1", ((User)result.getContent()).getUserName());
	}

	//------------------------------------------------------------------------//

	@Test
	void receiveUpdateUser_type() {
		// Make sure this user doesn't exist!
		User usr = new User("dc724af18fbdd4e59189f5fe768a5f8311527050",
				"dcr24bf18fe5d4e29189f5fe7q3a5f8311567250", null);
		Envelope in = new Envelope(usr, "updateUser");
		Envelope result = controller.receiveEnvelope(in);

		assertTrue(result.getContent() instanceof String);
	}

	@Test
	void receiveUpdateUser_null() {
		Envelope in = new Envelope(null, "updateUser");
		Envelope result = controller.receiveEnvelope(in);

		assertEquals("rejection", result.getType());
		assertEquals("Failed to save profile.", result.getContent());
	}

	@Test
	void receiveUpdateUser_nonexisting() {
		// Make sure this user doesn't exist!
		User usr = new User("dc724af18fbdd4e59189f5fe768a5f8311527050",
				"dcr24bf18fe5d4e29189f5fe7q3a5f8311567250", null);
		Envelope in = new Envelope(usr, "updateUser");
		Envelope result = controller.receiveEnvelope(in);

		assertEquals("rejection", result.getType());
		assertEquals("No such user exists.", result.getContent());

		// 2021-02-25, Kasper S. Skott
		// At the time this test was written Controller.updateUser doesn't
		// control whether the user exists before updating it, resulting in
		// "Profile saved", which is probably unwanted behaviour.
	}

	@Test
	void receiveUpdateUser_existing() {
		// Sign up in case the user doesn't exist
		String[] arrStrUserInfo = {"TestUser", "Test1Password", "user@test.org"};
		Envelope usrEnv = controller.signUp(arrStrUserInfo);

		User usr = new User("TestUser", "Test1Password", null);
		Envelope in = new Envelope(usr, "updateUser");
		Envelope result = controller.receiveEnvelope(in);

		assertEquals("confirmation", result.getType());
		assertEquals("Profile saved", (String)result.getContent());
	}

	//------------------------------------------------------------------------//

	@Test
	void receiveUpdatePasswd_type() {
		// Sign up in case the user doesn't exist
		String[] arrStrUserInfo = {"TestUser", "Test1Password", "user@test.org"};
		Envelope usrEnv = controller.signUp(arrStrUserInfo);

		String[] arrStr = {"TestUser", "InvalidPasswd", "Test2Password"};
		Envelope in = new Envelope(arrStr, "updatePassword");
		Envelope result = controller.receiveEnvelope(in);

		assertTrue(result.getContent() instanceof String);
	}

	@Test
	void receiveUpdatePasswd_null() {
		Envelope in = new Envelope(null, "updatePassword");
		Envelope result = controller.receiveEnvelope(in);

		assertEquals("reply", result.getType());
		assertEquals("No match with current password!",
					 (String)result.getContent());
	}

	@Test
	void receiveUpdatePasswd_nonexisting() {
		// Make sure this user doesn't exist!
		String[] arrStr = {"dcr24bf18fe5d4e29189f5fe7q3a5f8311567250",
				"12As8we51fh1f91283hd1hf91", "Fj51ahf93jh28fh1s35d6a"};
		Envelope in = new Envelope(arrStr, "updatePassword");
		Envelope result = controller.receiveEnvelope(in);

		assertEquals("reply", result.getType());
		assertEquals("No match with current password!",
				(String)result.getContent());
	}

	@Test
	void receiveUpdatePasswd_invalid() {
		// Sign up in case the user doesn't exist
		String[] arrStrUserInfo = {"TestUser", "Test1Password", "user@test.org"};
		Envelope usrEnv = controller.signUp(arrStrUserInfo);

		String[] arrStr = {"TestUser", "InvalidPasswd", "Test2Password"};
		Envelope in = new Envelope(arrStr, "updatePassword");
		Envelope result = controller.receiveEnvelope(in);

		assertEquals("reply", result.getType());
		assertEquals("No match with current password!",
				(String)result.getContent());
	}

	@Test
	void receiveUpdatePasswd_valid() {
		// Sign up in case the user doesn't exist
		String[] arrStrUserInfo = {"TestUser", "Test1Password", "user@test.org"};
		Envelope usrEnv = controller.signUp(arrStrUserInfo);

		// If user already exists attempt to set password to Test1Password.
		if (((String)usrEnv.getContent()).equals("Username already taken")) {
			String[] arrStrChange = {"TestUser", "Test2Password", "Test1Password"};
			controller.updatePass(arrStrChange);
		}

		String[] arrStr = {"TestUser", "Test1Password", "Test2Password"};
		Envelope in = new Envelope(arrStr, "updatePassword");
		Envelope result = controller.receiveEnvelope(in);

		assertEquals("reply", result.getType());
		assertEquals("Password changed",
				(String)result.getContent());
	}

	//------------------------------------------------------------------------//

	@Test
	void receiveGetMovie_type() {
		String[] arrStr = {"Inception", "tt1375666"};
		Envelope in = new Envelope(arrStr, "getMovie");
		Envelope result = controller.receiveEnvelope(in);

		assertTrue(result.getContent() instanceof Movie);
	}

	@Test
	void receiveGetMovie_null() {
		Envelope in = new Envelope(null, "getMovie");
		Envelope result = controller.receiveEnvelope(in);
		Movie movie = (Movie) result.getContent();

		assertNull(movie.getTitle());
	}

	@Test
	void receiveGetMovie_existing() {
		String[] arrStr = {"Inception", "tt1375666"};
		Envelope in = new Envelope(arrStr, "getMovie");
		Envelope result = controller.receiveEnvelope(in);
		Movie movie = (Movie) result.getContent();

		assertEquals("Inception", movie.getTitle());
	}

	@Test
	void receiveGetMovie_nonexisting() {
		String[] arrStr = {"d908sa6hd1d02", "df85h1jds1f9ds2"};
		Envelope in = new Envelope(arrStr, "getMovie");
		Envelope result = controller.receiveEnvelope(in);
		Movie movie = (Movie) result.getContent();

		assertNull(movie.getTitle());
	}

	////////////////////////////////////////////////////////////////////////////

	private class TestConnection implements IConnection {
		private Controller control;

		@Override
		public void setController(Controller controller) {
			this.control = controller;
		}

		@Override
		public void startConnection(int intThreads) {

		}

		@Override
		public void stopConnection() {

		}
	}

	private class TestDatabaseReader implements IDatabaseReader {

		@Override
		public String[][] searchOMDBdbShows(String strSearchTerms) {
			String [][] show = new String[1][5];

			switch (strSearchTerms) {
				case "Friends":
					show[0][0] = "Friends";
					show[0][1] = "tt0108778";
					show[0][2] = TEST_POSTER_URL;
					show[0][3] = "8.9";
					show[0][4] = "series";
				break;

				default:
					show[0][0] = null;
					show[0][1] = null;
					show[0][2] = null;
					show[0][3] = null;
					show[0][4] = null;
				break;
			}

			return show;
		}

		@Override
		public JSONObject searchOmdbShow(String id) {
			return null;
		}

		@Override
		public Show generateShow(String[] arShow) {
			Show show = new Show((String)null);
			switch (arShow[0]) {
				case "Friends":
					show.setName("Friends");
					show.setImdbId("tt0108778");
					break;

				default:
					break;
			}

			return show;
		}

		@Override
		public Show updateShow(Show show) {
			return null;
		}

		@Override
		public Movie generateMovie(String[] input) {
			Movie result;

			switch (input[0]) {
				case "Inception":
					result = new Movie(
							"Inception",
							"2010",
							"16 Jul 2010",
							"A thief who steals corporate secrets through "+
									"the use of dream-sharing technology is "+
									"given the inverse task of planting an "+
									"idea into the mind of a C.E.O.",
							TEST_POSTER_URL,
							"tt1375666",
							"8.8",
							"$292,576,195",
							"74",
							"Actor A, Actor B");
				break;

				default:
					result = new Movie(
							null,
							null,
							null,
							null,
							null,
							null,
							null,
							null,
							null,
							null);
				break;
			}

			return result;
		}
	}

}
