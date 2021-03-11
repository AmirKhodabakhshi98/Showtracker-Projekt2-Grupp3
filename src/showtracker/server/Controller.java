package showtracker.server;

import showtracker.*;

import java.io.File;
import java.util.HashMap;

/**
 * @author Filip Spånberg
 * Changes made by Adam
 */
public class Controller {
	private IDatabaseReader dbr;
	private GUI gui = new GUI(this);
	private IConnection connection;
	private HashMap<String, String> users = new HashMap<>();

	/**
	 * Constructor that reads the current list of users
	 */
	Controller(IDatabaseReader dbr, IConnection connection) {
		this.dbr = dbr;
		this.connection = connection;
		this.connection.setController(this);

		gui.start();
		File folFiles = new File("files/");
		File folUsers = new File("files/users/");
		if (!folFiles.exists())
			folFiles.mkdir();
		if (!folUsers.exists())
			folUsers.mkdir();

		if (new File("files/users.obj").exists())
			users = (HashMap<String, String>) Helper.readFromFile("files/users.obj");
	}

	/**
	 * Method for handling a request from a client
	 * @param envInput The incoming request
	 * @return The reply
	 */
	Envelope receiveEnvelope(Envelope envInput) {
		Envelope returnEnvelope = null;

		if (envInput.getType() == null)
			return null;

		switch (envInput.getType()) {
			case "searchShows":
				String strSearchTerms = (String) envInput.getContent();
				if (strSearchTerms == null) {
					returnEnvelope = new Envelope(null, "shows");
				}
				else {
					String[][] strArr = dbr.searchOMDBdbShows(strSearchTerms);
					returnEnvelope = new Envelope(strArr, "shows");
				}
				break;

			case "getShow":
				String[] strArrEpisodeQuery = (String[]) envInput.getContent();
				Show show;
				if (strArrEpisodeQuery == null)
					show = new Show((String)null);
				else
					show = dbr.generateShow(strArrEpisodeQuery);

				returnEnvelope = new Envelope(show, "show");
				break;

			case "getDetail":
				String id  =  (String)envInput.getContent();
				String[] info = dbr.getDetail(id);

				returnEnvelope = new Envelope(info,"detail");
				break;

			case "signUp":
				String[] strArrSignup = (String[]) envInput.getContent();
				returnEnvelope = signUp(strArrSignup);
				break;

			case "checkName":
				String username = (String) envInput.getContent();
				returnEnvelope = new Envelope(users.containsKey(username),
						"checkUsername");
				break;

			case "logIn":
				String[] strArrLogin = (String[]) envInput.getContent();
				if (strArrLogin == null)
					returnEnvelope = new Envelope(null, "user");
				else
					returnEnvelope = loginUser(strArrLogin);

				break;

			case "updateUser":
				User usrUpdate = (User) envInput.getContent();
				returnEnvelope = updateUser(usrUpdate);
				break;

			case "updatePassword":
				String[] strArrPassword = (String[]) envInput.getContent();
				if (strArrPassword == null) {
					returnEnvelope = new Envelope(
							"No match with current password!", "reply");
				}
				else {
					returnEnvelope = updatePass(strArrPassword);
				}

				break;

			case "getMovie":
				String[] info = (String[]) envInput.getContent();
				Movie movie;
				if (info == null)
					movie = new Movie((String)null);
				else
					movie = dbr.generateMovie(info);

				Movie movie = dbr.generateMovie(info);
				System.out.println("returning movie");

				String[] infoMovie = (String[]) envInput.getContent();
				Movie movie;
				if (infoMovie == null)
					movie = new Movie((String)null);
				else
					movie = dbr.generateMovie(infoMovie);


				returnEnvelope = new Envelope(movie, "movie");
				break;

			default: break;
		}
		return returnEnvelope;
	}

	/**
	 * Updates a user's password
	 * @param strArrUserInfo Array with user info
	 * @return The reply
	 */
	Envelope updatePass(String[] strArrUserInfo) {
		String strPassword = users.get(strArrUserInfo[0]);
		if (strPassword == null)
			return new Envelope("No match with current password!", "reply");

		if (strPassword.equals(strArrUserInfo[1])) {
			users.put(strArrUserInfo[0], strArrUserInfo[2]);
			Helper.writeToFile(users, "files/users.obj"); // Fixes bugg B2

			return new Envelope("Password changed", "reply");
		} else {
			return new Envelope("No match with current password!", "reply");
		}
	}

	/**
	 * Signs a user up
	 * @param strArrUserInfo Array with user info
	 * @return The reply
	 */
	Envelope signUp(String[] strArrUserInfo) {
		String strUser = users.get(strArrUserInfo[0]);
		if (strUser == null) {
			User user = new User(strArrUserInfo[0], strArrUserInfo[2], null);
			synchronized (this) {
				users.put(strArrUserInfo[0], strArrUserInfo[1]);
				Helper.writeToFile(users, "files/users.obj");
				Helper.writeToFile(user, "files/users/" + strArrUserInfo[0] + ".usr");
			}
			return new Envelope("User registered", "signin");
		} else
			{
			return new Envelope("Username already taken", "signin");
		}
	}

	/**
	 * Logs a user in
	 * @param strArrUserInfo Array with the user info
	 * @return The reply
	 */
	Envelope loginUser(String[] strArrUserInfo) {
		User user = null;
		String strPassword = users.get(strArrUserInfo[0]);
		if (strPassword != null && strPassword.equals(strArrUserInfo[1]))
			user = (User) Helper.readFromFile("files/users/" + strArrUserInfo[0] + ".usr");
		return new Envelope(user, "user");
	}

	/**
	 * Updates a user object
	 * @param user The user to update
	 * @return The reply
	 */
	Envelope updateUser(User user) {
		if (user == null || user.getUserName() == null)
			return new Envelope("Failed to save profile.", "rejection");

		if (users.containsKey(user.getUserName())) {
			Helper.writeToFile(user, "files/users/" + user.getUserName() + ".usr");
			return new Envelope("Profile saved", "confirmation");
		} else {
			return new Envelope("No such user exists.", "rejection");
		}
	}

	/**
	 * Starts the Connection class, which listens for clients' requests
	 * @param threads Amount of threads to start
	 */
	void startConnection(int threads) {
		connection.startConnection(threads);
	}

	/**
	 * Stops the Connection class
	 */
	void stopConnection() {
		System.out.println("Controller exiting...");
		connection.stopConnection();
		System.out.println("Controller exited.");
	}

	/**
	 * Sets the amount of threads active
	 * @param intThreads
	 */
	void setThreadCount(int intThreads) {
		gui.setActiveThreads(intThreads);
	}


	/**
	 * Main method to start the Server
	 * @param args
	 */
	public static void main (String[] args) {
		DatabaseReader dbr = new DatabaseReader();
		Connection connection = new Connection();
		Controller controller = new Controller(dbr, connection);
	}
}