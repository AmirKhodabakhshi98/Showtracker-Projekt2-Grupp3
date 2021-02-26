package showtracker;

import javax.swing.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * Class with helpful methods
 */
public class Helper {
	public static final DecimalFormat df = new DecimalFormat("0.#");

    /**
     * Method for decompressing a Gzip file
     * @param input
     */
    public static void decompressGzip(String input) {
        File fiInput = new File(input);
        String stOutput = fiInput.getName().substring(0, fiInput.getName().lastIndexOf("."));
        File fiOutput = new File(stOutput);
        try (GZIPInputStream stInput = new GZIPInputStream(new FileInputStream(fiInput))){
            try (FileOutputStream fos = new FileOutputStream(fiOutput)){
                byte[] buffer = new byte[1024];
                int len;
                while((len = stInput.read(buffer)) != -1){
                    fos.write(buffer, 0, len);
                }
            }
        } catch (Exception e) {
            System.out.println("DatabaseReader.decompressGzip: " + e);
        }
    }

    /**
     * Method for writing an Object to file
     * @param obj Object to write
     * @param file File to write to
     */
    public static void writeToFile(Object obj, String file) {
        File fiWrite = new File(file);
        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fiWrite)))) {
            oos.writeObject(obj);
        } catch (Exception e) {
            System.out.println("Helper.writeToFile: " + e);
        }
    }

    /**
     * Method for reading an object from a file
     * @param file File to read from
     * @return Object to return
     */
    public static Object readFromFile(String file) {
        File fiRead= new File(file);
        Object o = null;
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fiRead)))) {
            o = ois.readObject();
        } catch (Exception e) {
            System.out.println("Helper.readFromFile: " + e);
        }
        return o;
    }

    /**
     * Method for decoding unicode
     * @param input Input String
     * @return Output String
     */
    public static String decodeUnicode(String input) {
        if (input != null) {
            Pattern pattern = Pattern.compile("\\\\u.{4}");
            Matcher matcher = pattern.matcher(input);
            while (matcher.find()) {
                String s = Character.toString((char) matcher.group().getBytes()[0]);
                input.replaceAll(matcher.group(), s);
            }
        }
        return input;
    }

    /**
     * Method for checking the validity of a username (excluding forbidden symbols)
     * @param username Input username
     * @return
     */
    public static boolean checkUsernameValidity(String username) {
        String pattern = "[\\\\/:*?\"<>|%]";
        Pattern p = Pattern.compile(pattern);
        Matcher match = p.matcher(username);
        return (!(match.find() || (username.equals(""))));
    }

    /**
     * Method for checking the validity of an email (must have name, @, domain, and a top level domain)
     * @param email
     * @return
     */
    public static boolean checkEmailValidity(String email) {
        String pattern = "[a-z0-9]+@[a-z0-9]+\\.[a-z]{1,3}";
        Pattern p = Pattern.compile(pattern);
        Matcher match = p.matcher(email.toLowerCase());
        return (!(email.equals("")) && match.find());
    }

    /**
     * Checking the validity of a password (must contain a capital and a lowercase letter and a number
     * and must be at least 8 characters long)
     * @param password
     * @return
     */
    public static boolean checkPasswordValidity(String password) {
        Pattern p1 = Pattern.compile("[a-z]");
        Pattern p2 = Pattern.compile("[A-Z]");
        Pattern p3 = Pattern.compile("[0-9]");
        Matcher match1 = p1.matcher(password);
        Matcher match2 = p2.matcher(password);
        Matcher match3 = p3.matcher(password);

        return (password.length() > 7 && match1.find() && match2.find() && match3.find());
    }

    /**
     * Displaying an error message
     * @param message The message to display
     */
    public static void errorMessage(String message) {
    	JOptionPane.showMessageDialog(null, message, "No connection" ,JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Comparator for sorting shows by last watched
     */
    public static class LastWatchedComparator implements Comparator<Show> {

        @Override
        public int compare(Show show1, Show show2) {
            return show2.getLastWatched().compareTo(show1.getLastWatched()) ;
        }
    }

    /**
     * Comparator for sorting shows by name
     */
    public static class NameComparator implements Comparator<Show> {

        @Override
        public int compare(Show show1, Show show2) {
            return show1.getName().compareTo(show2.getName()) ;
        }
    }

    public static class NameComparatorMovie implements Comparator<Movie>{
        @Override
        public int compare(Movie movie1, Movie movie2){
            return movie1.getTitle().compareTo(movie2.getTitle());
        }
    }
}