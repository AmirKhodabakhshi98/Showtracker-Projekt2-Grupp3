package showtracker.server;

interface IConnection {
	void setController(Controller controller);

	void startConnection(int intThreads);

	void stopConnection();
}
