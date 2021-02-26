package showtracker.server;

import showtracker.Envelope;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

/**
 * @author Filip Spånberg
 * Changes made by Adam
 * 
 * Connection hanterar kopplingen mellan klient och server
 */
class Connection implements IConnection {
    private boolean blnIsOnline = false;
    private Controller controller;
    private Buffer<Socket> socketBuffer = new Buffer<>();
    private int intActiveThreads = 0;
    private LinkedList<Thread> threads = new LinkedList<>();

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Method for starting the server
     * @param intThreads The amount of threads to start for clients
     */
    @Override
    public void startConnection(int intThreads) {
        if (!blnIsOnline) {
            blnIsOnline = true;
            for (int i = 0; i < intThreads; i++) {
                EventHandler thread = new EventHandler();
                thread.start();
                threads.add(thread);
            }
            SocketListener thread = new SocketListener();
            thread.start();
            threads.add(thread);
        }
    }


    @Override
    public void stopConnection() {
        System.out.println("Connection exiting...");
        if (blnIsOnline) {
            for (Thread thread : threads) {
                try {
                    thread.interrupt();
                } catch (Exception e) {
                    System.out.println("Connection: " + e);
                }
            }
            threads.clear();
            controller.setThreadCount(0);
            System.out.println("Connection exited.");
            blnIsOnline = false;
        }
    }

    /**
     * Increase the int keeping track of active threads
     */
    void increaseThreadCount() {
        controller.setThreadCount(intActiveThreads++);
    }

    /**
     * Decrease the int keeping track of active threads
     */
    void decreaseThreadCount() {
        controller.setThreadCount(intActiveThreads--);
    }

    /**
     * Inner class for listening after connections
     */
    private class SocketListener extends Thread {
        public void run() {
            try (ServerSocket serverSocket = new ServerSocket(5555)) {
                while (blnIsOnline) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Found a connection");
                    socketBuffer.put(clientSocket);
                }
            } catch (Exception e) {
                System.out.println("SocketListener: " + e);
            }
        }
    }

    /**
     * Inner class for handling requests from clients
     */
    private class EventHandler extends Thread {
        public void run() {
            System.out.println("Starting eventhandler...");
            while (blnIsOnline) {
                Socket socket = socketBuffer.get();
                System.out.println("Eventhandler got a socket. Processing...");
                increaseThreadCount();
                try {
                    ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                    Envelope e = (Envelope) ois.readObject();
                    System.out.println("Envelope received. Type: " + e.getType());
                    Envelope returnEnvelope = controller.receiveEnvelope(e);
                    ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                    oos.writeObject(returnEnvelope);
                    oos.flush();
                    System.out.println("Return envelope sent.");
                } catch (Exception e) {
                    System.out.println("EventHandler: " + e);
                } finally {
                    decreaseThreadCount();
                }
            }
        }
    }
}