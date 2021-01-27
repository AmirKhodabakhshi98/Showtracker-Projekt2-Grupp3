package showtracker.server;

import org.json.simple.JSONObject;
import showtracker.Episode;
import showtracker.Helper;
import showtracker.Show;
import showtracker.User;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.*;

public class Test implements Serializable {
    private static final long serialVersionUID = -3565900616939999809L;
    private JPanel pnMain;
    private static String createTableTitles = "CREATE TABLE IMDB_TITLES (ID VARCHAR(10) NOT NULL PRIMARY KEY,NAME VARCHAR(100));";
    private static String createTableEpisodes = "CREATE TABLE IMDB_EPISODES (ID VARCHAR(10) NOT NULL PRIMARY KEY,PARENT VARCHAR(10),SEASON SMALLINT,EPISODE INT);";
    private DatabaseReader dbr = new DatabaseReader();
    private DecimalFormat df = new DecimalFormat("0.#");


    public static void main(String[] args) {
        Test test = new Test();
        //test.testJSON();
        //test.refresh();
        //test.writeShows();
        //test.createMailSSL("filip.spanberg@gmail.com", "Test", "Test");
        //test.printShow();
        //test.createShowObject("got", "121361");
        //test.getDescription("121361");
        //test.testUpdate();
        //test.showEpisodes();
        /*User u = new User("Filip", "f@f.se", null);*/

    }

    /*public void getHamburger() {
        //Hamburger hamburger = null;
        try {
            ExecutorService es = Executors.newCachedThreadPool();
            String t = "6";
            CompletableFuture<String> test = CompletableFuture.supplyAsync(() ->  "Bread done.");
            System.out.println("Grilling burger...");
            Thread.sleep(3000);
            System.out.println("Burger grilled.");
            System.out.println(test.get());
        } catch (Exception e) {

        }



                    /*new Supplier() {
                public Integer get() {
                    return Integer.parseInt("6");
                }
            }) <>();
            es.submit(test.complete(e -> Integer.parseInt(e)));
            Burger burger = Freezer.getBurger;
            CompletableFuture<GrilledBurger> cfGrilledBurger = CompletableFuture.supplyAsync(() -> Oven.grill(burger));

            Bread bread = BreadBox.getBread();
            CompletableFuture<GrilledBread> cfGrilledBread = CompletableFuture.supplyAsync(() -> Toaster.grill(bread));

            Salad salad = Fridge.getSalad();
            try {
                GrilledBread grilledBread = cfGrilledBread.get();
                GrilledBurger grilledBurger = cfGrilledBurger.get();
                hamburger = Chef.makeBurger(grilledBread, grilledBurger, salad);
            } catch (Exception e) {
                System.out.println(e);
        }
        return hamburger;
    }

    public void showEpisodes() {
        User u = (User) Helper.readFromFile("files/users/filip.usr");
        for (Show s : u.getShows()) {
            System.out.println(s.getName());
            for (Episode e : s.getEpisodes()) {
                System.out.print("S" + Helper.df.format(e.getSeasonNumber()) + "E" + Helper.df.format(e.getEpisodeNumber()) + ", ");
            }
            System.out.println();
        }
    }

    public void testUpdate() {
        Show show1 = new Show("My show");
        for (int i = 0; i < 3; i++) {
            Episode e = new Episode(show1, i + 1, 1);
            e.setTvdbId(Integer.toString(i + 4));
            show1.addEpisode(e);
        }
        show1.sortEpisodes();
        Show show2 = new Show("My show");
        for (int i = 0; i < 4; i++) {
            Episode e = new Episode(show1, i + 1, 1);
            e.setTvdbId(Integer.toString(i + 4));
            show2.addEpisode(e);
        }

        for (Episode e : show1.getEpisodes())
            System.out.print("S0" + Helper.df.format(e.getSeasonNumber()) + "E0" + Helper.df.format(e.getEpisodeNumber()) + ", ");
        System.out.println("After:");
        //dbr.updateShow(show1, show2);

        for (Episode e : show1.getEpisodes())
            System.out.print("S0" + Helper.df.format(e.getSeasonNumber()) + "E0" + Helper.df.format(e.getEpisodeNumber()) + ", ");
    }

    public void createShowObject(String name, String id) {
        String[] input = {"temp", id};
        Show show = dbr.generateShow(input);
        Helper.writeToFile(show, "files/" + name + ".obj");
    }

    public void getDescription(String id) {
        JSONObject obj = dbr.searchTheTVDBShow(id);
        System.out.println(obj.get("overview"));
    }

    public void showUsers() {
        HashMap<String, String> users = (HashMap<String, String>) Helper.readFromFile("files/users.obj");
        for (Map.Entry<String, String> e : users.entrySet())
            System.out.println("Username: " + e.getKey() + ", password: " + e.getValue());
    }

    public void printShow() {
        Show s = (Show) Helper.readFromFile("files/venture_bros.obj");
        for (Episode e : s.getEpisodes())
            System.out.println("S" + df.format(e.getSeasonNumber()) + "E" + df.format(e.getEpisodeNumber()) + ", " + e.getName());
    }

    public void refresh() {
        System.out.println(dbr.refreshToken());
    }

    public void writeShows() {
        dbr.authenticateTheTVDB();
        String search = JOptionPane.showInputDialog("Search for a show:");
        String[][] results = dbr.searchTheTVDBShows(search);
        String message = "Results:";
        for (int i = 0; i < results.length; i++)
            message += "\n" + i + ": " + results[i][0];
        message += "\nEnter show #:";
        int number = Integer.parseInt(JOptionPane.showInputDialog(message));
        Show show = dbr.generateShow(results[number]);
        Helper.writeToFile(show, "files/venture_bros.obj");
    }

    private void createMailSSL(String toAddress, String subject, String body) {
        final String fromEmail = "showtracker.da336a@gmail.com"; //requires valid gmail id
        final String password = "Sven78Kristina"; // correct password for gmail id
        final String toEmail = toAddress; // can be any email id

        System.out.println("SSLEmail Start");
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.socketFactory.port", "465"); //SSL Port
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory"); //SSL Factory Class
        props.put("mail.smtp.auth", "true"); //Enabling SMTP Authentication
        props.put("mail.smtp.port", "465"); //SMTP Port
        props.put("mail.debug", "true");

        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };

        Session session = Session.getDefaultInstance(props, auth);
        System.out.println("Session created");
        sendEmail(session, toEmail, subject, body);
    }

    private void createMailTLS(String toAddress, String subject, String body) {
        final String fromEmail = "showtracker.da336a@gmail.com"; //requires valid gmail id
        final String password = "Sven78Kristina"; // correct password for gmail id
        final String toEmail = toAddress; // can be any email id

        System.out.println("TLSEmail Start");
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
        props.put("mail.debug", "true");

        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };
        Session session = Session.getInstance(props, auth);

        sendEmail(session, toEmail, subject, body);
    }

    public static void sendEmail(Session session, String toEmail, String subject, String body) {
        try {
            MimeMessage msg = new MimeMessage(session);
            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress("admin@showtracker.com", "NoReply-JD"));

            msg.setReplyTo(InternetAddress.parse("no_reply@example.com", false));

            msg.setSubject(subject, "UTF-8");

            msg.setText(body, "UTF-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            System.out.println("Message is ready");
            Transport.send(msg);

            System.out.println("EMail Sent Successfully!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testList() {
        JTextField tfSearch = new JTextField();
        JButton btSearch = new JButton("Search");
        JPanel pnMain = new JPanel();
        JPanel pnTop = new JPanel();
        JPanel pnCenter = new JPanel();

        pnMain.setPreferredSize(new Dimension(350, 400));
        pnTop.setPreferredSize(new Dimension(350, 30));
        //pnCenter.setPreferredSize(new Dimension(350, 320));

        pnMain.setLayout(new BorderLayout());
        pnMain.add(pnTop, BorderLayout.NORTH);
        pnMain.add(pnCenter, BorderLayout.CENTER);

        String[] arShows = {"Game of Thrones", "The Venture Bros.", "Steven Universe"};

        for (String s : arShows) {
            JPanel panel = new JPanel();
            panel.setBorder(BorderFactory.createBevelBorder(1));
            JLabel label = new JLabel(s);
            JButton button = new JButton("Info");
            panel.setPreferredSize(new Dimension(350, 30));
            panel.setLayout(new GridLayout(1, 2));
            panel.add(label);
            panel.add(button);
            pnCenter.add(panel);
        }

        startFrame(pnMain);
    }

    private void testSQL() {
        DatabaseReader dbr = new DatabaseReader();
        dbr.setupDBConnection();

        /*File archive = new File("ShowTracker/files/title.episode.tsv.gz");
        File output = new File("ShowTracker/files/title_episode.txt");
        try {
            Helper.decompressGzip("ShowTracker/files/title.episode.tsv.gz");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println("Connection started.");
        dbr.updateSql("use ai8934");
        //System.out.println("DB selected");

        // Delete table
        //dbr.updateSql("drop table IMDB_EPISODES");
        //dbr.updateSql("drop table IMDB_TITLES");

        // Empty table
        //dbr.updateSql("truncate table IMDB_EPISODES");
        //dbr.updateSql("truncate table IMDB_SHOWS");

        // Create table
        //dbr.updateSql(createTableEpisodes);
        //dbr.updateSql(createTableTitles);

        // Read all episodes (ID, parent, episode, season)
        //dbr.readEpisodes();
        //dbr.readTitles();
    }

    private void testJSON() {
        DatabaseReader dbr = new DatabaseReader();
        System.out.println(dbr.searchTheTVDBShow("72306"));
        dbr.authenticateTheTVDB();

        dbr.searchTheTVDBShows("black books");
        //dbr.searchTheTVDBShow("76924");
        //dbr.getEpisodesOfShow("188551");
        Show show = dbr.generateShow("188551");
        System.out.println("Show: " + show.getName());
        for (Season s: show.getSeasons()) {
            System.out.println("\tSeason " + s.getNumber() + ":");
            for (Episode e : s.getEpisodes())
                System.out.println("\t\tEpisode " + e.getNumber() + ": " + e.getName());
        }

        String search = JOptionPane.showInputDialog("Search for a show:");
        String[][] results = dbr.searchTheTVDBShows(search);
        if (results != null) {
            System.out.println(results.length);
            String message = "Results:";
            for (int i = 0; i < results.length; i++)
                message += "\n" + i + ": " + results[i][0];
            message += "\nEnter show #:";
            int number = Integer.parseInt(JOptionPane.showInputDialog(message));
            Show show = dbr.generateShow(results[number]);
            String stShow = show.getName();
            for (double d : show.getSeasons()) {
                stShow += "\nSeason " + df.format(d);
            }
            stShow += "\nEnter season number for episodes:";
            double inSeason = Double.parseDouble(JOptionPane.showInputDialog(stShow));
            LinkedList<Episode> season = show.getSeason(inSeason);
            String stSeason = "Season " + df.format(inSeason) + ":";
            for (Episode e : season)
                stSeason += "\nEpisode " + df.format(e.getEpisodeNumber()) + ": " + e.getName();
            double inEpisode = Double.parseDouble(JOptionPane.showInputDialog(stSeason));
            Episode episode = show.getEpisode(inSeason, inEpisode);
            JOptionPane.showMessageDialog(null,
                    String.format("%s%nSeason %s, episode %s:%n%s",
                            show.getName(),
                            df.format(episode.getSeasonNumber()),
                            df.format(episode.getEpisodeNumber()),
                            episode.getDescription()
                    )
            );
        }
    }

    public void testFoldout() {
        JScrollPane spMain = new JScrollPane();
        pnMain = new JPanel();
        pnMain.setPreferredSize(new Dimension(200, 400));

        JPanel pn1 = new JPanel();
        pn1.setPreferredSize(new Dimension(200, 40));
        JPanel pn1ep = new JPanel();
        pn1ep.setPreferredSize(new Dimension(200, 0));
        JButton bn1 = new JButton("1");
        JLabel lb1 = new JLabel("Ep");
        bn1.addActionListener(e -> addLabel(pn1ep, lb1));

        JPanel pn2 = new JPanel();
        pn2.setPreferredSize(new Dimension(200, 40));
        JPanel pn2ep = new JPanel();
        pn2ep.setPreferredSize(new Dimension(200, 0));
        JButton bn2 = new JButton("2");
        JLabel lb2 = new JLabel("Ep");
        bn2.addActionListener(e -> addLabel(pn2ep, lb2));

        JPanel pn3 = new JPanel();
        pn3.setPreferredSize(new Dimension(200, 40));
        JPanel pn3ep = new JPanel();
        pn3ep.setPreferredSize(new Dimension(200, 0));
        JButton bn3 = new JButton("3");
        JLabel lb3 = new JLabel("Ep");
        bn3.addActionListener(e -> addLabel(pn3ep, lb3));

        pn1.add(bn1);
        pn2.add(bn2);
        pn3.add(bn3);

        pnMain.add(pn1);
        pnMain.add(pn1ep);
        pnMain.add(pn2);
        pnMain.add(pn2ep);
        pnMain.add(pn3);
        pnMain.add(pn3ep);

        //spMain.add(pnMain);
        startFrame(pnMain);
    }

    private void startFrame(JPanel panel) {
        JFrame frame = new JFrame("Test");
        frame.add(panel);
        frame.setVisible(true);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void addLabel(JPanel panel, JLabel label) {
        if (panel.getPreferredSize().getHeight() == 0) {
            System.out.println(panel.getPreferredSize());
            panel.setPreferredSize(null);
            panel.add(label);
            pnMain.revalidate();
        } else {
            System.out.println("Test");
            panel.setPreferredSize(new Dimension(200, 0));
            panel.removeAll();
            panel.revalidate();
        }
    }*/
}