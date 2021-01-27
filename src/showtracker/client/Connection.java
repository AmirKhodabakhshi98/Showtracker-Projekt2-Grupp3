package showtracker.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import showtracker.Envelope;


/**
 * @author Filip Spånberg
 * Handles the connection and streams between the client program and the server connection
 */
class Connection {

	private String strIp;
	private int intPort;

	Connection(String strIp, int intPort) {
		this.strIp = strIp;
		this.intPort = intPort;
	}

	/**
	 * Packs an envelope to send an Envelope to the server with content and a descriptor
	 * @param object The Object to send
	 * @param strType The description of the content
	 * @return The content of the returning Envelope
	 */
	Object packEnvelope(Object object, String strType) {
		Envelope envSend = new Envelope(object, strType);
		Envelope envReturn = sendEnvelope(envSend);
		if (envReturn != null)
			return envReturn.getContent();
		else
			return null;
	}

	/**
	 * Sends an Envelope to the server
	 * @param envelope The Envelope to send
	 * @return The Envelope received in return
	 */
	private Envelope sendEnvelope(Envelope envelope) {
		Envelope returnEnvelope = null;
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		Socket socket = null;
		try {
			socket = new Socket(strIp, intPort);
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(envelope);
			oos.flush();
			System.out.println("Connection: Envelope sent.");
			ois = new ObjectInputStream(socket.getInputStream());
			returnEnvelope = (Envelope) ois.readObject();
			System.out.println("Connection: Envelope received.");

		} catch (Exception e) {
			System.out.println("Connection: " + e);
		} finally {
			try {
				socket.close();
				oos.close();
				ois.close();
			} catch (Exception e) {
				System.out.println("Connection: " + e);
			}
		}
		return returnEnvelope;
	}
}