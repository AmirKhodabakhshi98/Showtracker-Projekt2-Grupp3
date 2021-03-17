package showtracker.client;

import org.junit.jupiter.api.Assertions;
import showtracker.Show;
import showtracker.User;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class ClientControllerTest {
    ClientController clientController;
    Show show;
    String username = "test";
    String pwd = "testTest123";
    User user;
    User user2;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        clientController = new ClientController();
        user = new User("test", "test@test.com", null);
        user2 = new User("test2", "test2@test.com", null);
        clientController.setUser(user);
        show = new Show("Breaking Bad");
        // create a new instance of ClientController before each test
    }

    @org.junit.jupiter.api.Test
    void setPanelHome() {
        Show show = new Show("Breaking Bad");
        Assertions.assertEquals("Home", clientController.setPanel("Home", show));
    }

    @org.junit.jupiter.api.Test
    void setPanelHomeNull() {
        Assertions.assertEquals("Home", clientController.setPanel("Home", null));
    }

    @org.junit.jupiter.api.Test
    void setPanelShowList() {
        Assertions.assertEquals("ShowList", clientController.setPanel("ShowList", show));
    }

    @org.junit.jupiter.api.Test
    void setPanelShowListNull() {
        Assertions.assertEquals("ShowList", clientController.setPanel("ShowList", null));
    }

    @org.junit.jupiter.api.Test
    void setPanelProfile() {
        Assertions.assertEquals("Profile", clientController.setPanel("Profile", show));
    }

    @org.junit.jupiter.api.Test
    void setPanelProfileNull() {
        Assertions.assertEquals("Profile", clientController.setPanel("Profile", null));
    }

//    @org.junit.jupiter.api.Test
//    void setPanelInfoNull() {
//        Assertions.assertEquals("Info", clientController.setPanel("Info", null));
//    }

    @org.junit.jupiter.api.Test
    void setPanelInfo() {
        Assertions.assertEquals("Info", clientController.setPanel("Info", show));
    }

    @org.junit.jupiter.api.Test
    void setPanelEmpty() {
        Assertions.assertEquals("Error", clientController.setPanel("", null));
    }

    @org.junit.jupiter.api.Test
    void setPanelNumber() {
        Assertions.assertEquals("Error", clientController.setPanel("123", null));
    }

    @org.junit.jupiter.api.Test
    void setPanelNull() {
        Assertions.assertEquals("Error", clientController.setPanel(null, null));
    }

    @org.junit.jupiter.api.Test
    void logIn() {
        Assertions.assertEquals(user.getUserName(), clientController.logIn(username,pwd).getUserName());
    }

    @org.junit.jupiter.api.Test
    void logInBothNull() {
        Assertions.assertEquals(null, clientController.logIn(null,null));
    }

    @org.junit.jupiter.api.Test
    void logInPwdNull() {
        Assertions.assertEquals(null, clientController.logIn(username,null));
    }

    @org.junit.jupiter.api.Test
    void logInUsrNull() {
        Assertions.assertEquals(null, clientController.logIn(null,pwd));
    }

    @org.junit.jupiter.api.Test
    void signUp() {
        Assertions.assertEquals("User registered", clientController.signUp("test2", pwd,
                "test@gmail.com", null));
    }

    @org.junit.jupiter.api.Test
    void signUpExisting() {
        Assertions.assertEquals("Username already taken", clientController.signUp("test", pwd,
                "test@gmail.com", null));
    }
//
//    @org.junit.jupiter.api.Test
//    void signUpEmptyUser() {
//        Assertions.assertEquals("Username not available", clientController.signUp("", pwd,
//                "test@gmail.com", null));
//    }

//    @org.junit.jupiter.api.Test
//    void signUpEmptyPwd() {
//        Assertions.assertEquals("Invalid password", clientController.signUp("Test2", "",
//                "test@gmail.com", null));
//    }

    @org.junit.jupiter.api.Test
    void checkUsernameTakenExisting() {
        Assertions.assertEquals(true, clientController.checkUsernameTaken(username));
    }

//    @org.junit.jupiter.api.Test
//    void checkUsernameTakenNonExisting() {
//        Assertions.assertEquals(false, clientController.checkUsernameTaken("notTakenUsername"));
//    }

//    @org.junit.jupiter.api.Test
//    void checkUsernameTakenEmpty() {
//        try {
//            Files.deleteIfExists(Paths.get("files/users/.usr"));
//        }
//        catch (Exception e)
//        {
//            System.out.println(e);
//        }
//        Assertions.assertEquals(false, clientController.checkUsernameTaken(""));
//    }

//    @org.junit.jupiter.api.Test
//    void checkUsernameTakenNull() {
//        Assertions.assertEquals(false, clientController.checkUsernameTaken(null));
//    }

    @org.junit.jupiter.api.Test
    void finalizeUser() {
        Assertions.assertEquals("Success", clientController.finalizeUser(user));
    }

    @org.junit.jupiter.api.Test
    void finalizeUserNull() {
        User user = null;
        Assertions.assertEquals("fail", clientController.finalizeUser(user));
    }


    @org.junit.jupiter.api.Test
    void updatePassword() {
        clientController.updatePassword("test2", "testTest321", "testTest123");
        Assertions.assertEquals("Password changed",clientController.updatePassword("test2", pwd, "testTest321"));
    }

//    @org.junit.jupiter.api.Test
//    void updatePasswordToNull() {
//        clientController.updatePassword("test2", "testTest321", "testTest123");
//        Assertions.assertEquals("Password changed",clientController.updatePassword("test2", pwd, null));
//    }
//
//    @org.junit.jupiter.api.Test
//    void updatePasswordFromNull() {
//        clientController.updatePassword("test2", "testTest321", "testTest123");
//        Assertions.assertEquals("Password changed",clientController.updatePassword("test2", pwd, "testTest321"));
//    }

    @org.junit.jupiter.api.Test
    void updateUser() {
        Assertions.assertEquals("Profile saved",clientController.updateUser(user));
    }

    @org.junit.jupiter.api.Test
    void searchShows() {
        String[][] strings = clientController.searchShows("Breaking Bad");
        Assertions.assertEquals("tt0903747",strings[0][1]);
    }

    @org.junit.jupiter.api.Test
    void searchShowsEmpty()
    {
        String[][] strings = clientController.searchShows("");
        Assertions.assertEquals(null,strings);
    }

    /*@org.junit.jupiter.api.Test
    void searchShowsNull() {
        String[][] strings = clientController.searchShows(null);
        Assertions.assertEquals(null,strings[0][1]);
    }

    @org.junit.jupiter.api.Test
    void generateShow()
    {
        clientController.generateShow("Breaking Bad", "tt0903747");
    }

    @org.junit.jupiter.api.Test
    void generateShowEmptyName()
    {
        clientController.generateShow("", "tt0903747");
    }

    @org.junit.jupiter.api.Test
    void generateShowNullName()
    {
        clientController.generateShow(null, "tt0903747");
    }


    @org.junit.jupiter.api.Test
    void generateShowEmptyId()
    {
        clientController.generateShow("Breaking Bad", "");
    }

    @org.junit.jupiter.api.Test
    void generateShowNullId()
    {
        clientController.generateShow("Breaking Bad", null);
    }

     */

}

