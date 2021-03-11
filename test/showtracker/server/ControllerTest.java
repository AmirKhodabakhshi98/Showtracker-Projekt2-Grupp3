package showtracker.server;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import showtracker.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kasper S. Skott
 */
public class ControllerTest {

	private Controller controller;
	private TestDatabaseReader dbr;
	private TestConnection connection;

	@BeforeEach
	void beforeEach() {
		dbr = new TestDatabaseReader();
		connection = new TestConnection();
		controller = new Controller(dbr, connection);
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
		String[] arrStrUserInfo = {"TestUser", "Test1Password", "user@test.org"};
		Envelope usrEnv = controller.signUp(arrStrUserInfo);

		String[] arrStrUser = {"TestUser", "Test1Password"};
		Envelope in = new Envelope(arrStrUser, "logIn");
		Envelope result = controller.receiveEnvelope(in);

		assertNotNull(result.getContent());
		assertEquals("TestUser", ((User)result.getContent()).getUserName());
	}

	//------------------------------------------------------------------------//

	@Test
	void receiveUpdateUser_null() {
		Envelope in = new Envelope(null, "updateUser");
		Envelope result = controller.receiveEnvelope(in);

		assertEquals("rejection", result.getType());
		assertEquals("Failed to save profile.", (String)result.getContent());
	}

	@Test
	void receiveUpdateUser_nonexisting() {
		// Make sure this user doesn't exist!
		User usr = new User("dc724af18fbdd4e59189f5fe768a5f8311527050",
				"dcr24bf18fe5d4e29189f5fe7q3a5f8311567250", null);
		Envelope in = new Envelope(usr, "updateUser");
		Envelope result = controller.receiveEnvelope(in);

		assertEquals("rejection", result.getType());
		assertEquals("Failed to save profile.", (String)result.getContent());

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
			return new String[0][];
		}

		@Override
		public JSONObject searchOmdbShow(String id) {
			return null;
		}

		@Override
		public Show generateShow(String[] arShow) {
			return null;
		}

		@Override
		public Show updateShow(Show show) {
			return null;
		}

		@Override
		public String[] getDetail(String id) {
			return new String[0];
		}

		@Override
		public Movie generateMovie(String[] input) {
			return null;
		}
	}

}
