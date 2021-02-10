package showtracker.client;

import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import showtracker.Helper;
import showtracker.User;

import static showtracker.Helper.*;

/**
 * @author Basir Ramazani, Filip Sp�nberg
 * Changes made by Moustafa & Adam
 * <p>
 * Represents the login panel
 */
public class Login extends JPanel {

    private ClientController clientController;
    private JButton btnSignUp = new JButton("New here? Sign up for free!");

    private JTextField txfUsernameSignUp = new JTextField(20);
    private JTextField txfEmailSignup = new JTextField(20);
    private JPasswordField pwfPasswordSignUp = new JPasswordField(20);

    private JTextField txfUsername = new JTextField();
    private JPasswordField pwfPassword = new JPasswordField();
    private String strImagePath;

    /**
     * Constructor taking a ClientController instance
     *
     * @param clientController
     */
    Login(ClientController clientController) {
        this.clientController = clientController;
        setLayout(null);

        ImageIcon imi = new ImageIcon("images/Showtrack.png");
        Image image = imi.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        JLabel lbLogo = new JLabel(new ImageIcon(image));

        JButton btnLogIn = new JButton(" Log In ");
        btnLogIn.addActionListener(e -> checkUserLogin());
        btnSignUp.addActionListener(e -> signUp());

        lbLogo.setBounds(425, 25, 150, 150);
        txfUsername.setBounds(400, 200, 200, 30);
        pwfPassword.setBounds(400, 240, 200, 30);
        btnLogIn.setBounds(440, 290, 120, 30);
        btnSignUp.setBounds(400, 340, 200, 30);

        add(lbLogo);
        add(txfUsername);
        add(pwfPassword);
        add(btnLogIn);
        add(btnSignUp);
    }

    /**
     * Refreshing the view
     */
    void draw() {
        txfUsername.setText("");
        TextPrompt textPromptUsername = new TextPrompt("Username", txfUsername);
        textPromptUsername.changeAlpha(0.5f);
        textPromptUsername.changeStyle(Font.BOLD + Font.PLAIN);

        pwfPassword.setText("");
        TextPrompt textPromptPassword = new TextPrompt("Password", pwfPassword);
        textPromptPassword.changeAlpha(0.5f);
        textPromptPassword.changeStyle(Font.BOLD + Font.PLAIN);
        clearSignUp();
    }

    /**
     * Clearing the fields in the sign-up window
     */
    private void clearSignUp() {
        strImagePath = null;
        txfUsernameSignUp.setText("");
        txfEmailSignup.setText("");
        pwfPasswordSignUp.setText("");
    }

    /**
     * Method for creating an account
     */
    private void signUp() {
        int res = JOptionPane.showConfirmDialog(null, createAccount(), "Sign up!", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        boolean blnUsernameTaken = clientController.checkUsernameTaken(txfUsernameSignUp.getText());

        while (!(checkPasswordValidity(new String(pwfPasswordSignUp.getPassword())) &&
                checkUsernameValidity(txfUsernameSignUp.getText()) &&
                checkEmailValidity(txfEmailSignup.getText()) &&
                !blnUsernameTaken)
                && res == JOptionPane.OK_OPTION) {

            if (blnUsernameTaken)
                JOptionPane.showMessageDialog(null, "Username already taken!", "Username unavailable", JOptionPane.WARNING_MESSAGE);
            else if (!checkUsernameValidity(txfUsernameSignUp.getText()))
                JOptionPane.showMessageDialog(null, "Username not valid!", "No Username", JOptionPane.WARNING_MESSAGE);

            if (!checkEmailValidity(txfEmailSignup.getText()))
                JOptionPane.showMessageDialog(null, "Email not valid!", "No Email", JOptionPane.WARNING_MESSAGE);

            if (!checkPasswordValidity(new String(pwfPasswordSignUp.getPassword())))
                JOptionPane.showMessageDialog(null,
                        "Your password must contain at least 8 charachters, "
                                + "\none capital letter, one small letter and one digit!",
                        "No Password!", JOptionPane.WARNING_MESSAGE);


            res = JOptionPane.showConfirmDialog(null, createAccount(), "Sign Up!", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);

            blnUsernameTaken = clientController.checkUsernameTaken(txfUsernameSignUp.getText());
        }

        if (res == JOptionPane.OK_OPTION) {
            clientController.signUp(txfUsernameSignUp.getText(), new String(pwfPasswordSignUp.getPassword()), txfEmailSignup.getText(),
                    strImagePath);
            User user = clientController.logIn(txfUsernameSignUp.getText(), new String(pwfPasswordSignUp.getPassword()));
            if (strImagePath != null)
                user.setProfilePicture(strImagePath);
            clientController.finalizeUser(user);
        } else
            clearSignUp();
    }

    /**
     * Method for creating a panel where one can enter info for new account
     *
     * @return
     */
    private JPanel createAccount() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel pnlSouth = new JPanel(new GridLayout(7, 1));
        JPanel pnlProfilebtn = new JPanel(new BorderLayout());
        JLabel lblUsername = new JLabel("Username : ");
        JLabel lblPassword = new JLabel("Password : ");
        JLabel lblEmail = new JLabel("Email : ");

        ImageIcon imiProfile = new ImageIcon("images/add-profile.png");
        Image imgPro = imiProfile.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        imiProfile = new ImageIcon(imgPro);


        JCheckBox checkBox = new JCheckBox("Show password");
        JButton btnAddProfile = new JButton(imiProfile);

        btnAddProfile.addActionListener(e -> {

            int res = JOptionPane.showConfirmDialog(null, chooseImagePanel(), "User profile", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);

            if (JOptionPane.OK_OPTION != res) {
                strImagePath = null;
            }
        });

        pnlProfilebtn.add(btnAddProfile, BorderLayout.EAST);
        pnlSouth.add(lblUsername);
        pnlSouth.add(txfUsernameSignUp);
        pnlSouth.add(lblEmail);
        pnlSouth.add(txfEmailSignup);
        pnlSouth.add(lblPassword);
        pnlSouth.add(pwfPasswordSignUp);
        pnlSouth.add(checkBox);

        panel.add(pnlProfilebtn, BorderLayout.CENTER);
        panel.add(pnlSouth, BorderLayout.SOUTH);

        checkBox.addActionListener(e -> {
            if (checkBox.isSelected()) {
                pwfPasswordSignUp.setEchoChar((char) 0);
            } else {
                pwfPasswordSignUp.setEchoChar('*');
            }
        });

        return panel;

    }

    /**
     * Creates a panel for picking a profile picture
     *
     * @return
     */
    private JPanel chooseImagePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        ImageIcon imiFile = new ImageIcon("images/choose-file.png");
        Image imgfile = imiFile.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        imiFile = new ImageIcon(imgfile);

        JButton btnPickProfileImage = new JButton(imiFile);
        JLabel lblProfilePicture = new JLabel();
        JFileChooser jfc = new JFileChooser();

        FileFilter imageFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
        jfc.setFileFilter(imageFilter);
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        btnPickProfileImage.addActionListener(e -> {
            int res = jfc.showOpenDialog(null);
            if (res == JFileChooser.APPROVE_OPTION) {
                strImagePath = jfc.getSelectedFile().getPath();
                ImageIcon profilePicture = new ImageIcon(strImagePath);
                Image image = profilePicture.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                profilePicture = new ImageIcon(image);
                lblProfilePicture.setIcon(profilePicture);
            }
        });
        panel.setPreferredSize(new Dimension(300, 300));

        lblProfilePicture.setBounds(50, 25, 200, 200);
        btnPickProfileImage.setBounds(105, 240, 90, 35);
        lblProfilePicture.setBorder(new LineBorder(Color.BLACK));

        panel.add(lblProfilePicture);
        panel.add(btnPickProfileImage);

        return panel;
    }

    /**
     * Checks a user's login info and logs them in if correct
     */
    private void checkUserLogin() {
        String username = txfUsername.getText();
        String password = new String(pwfPassword.getPassword());
        User user = clientController.logIn(username, password);
        if (user != null) {
            clientController.finalizeUser(user);
        } else { // ny ide på UI, kan förbättras ^_^
            Helper.errorMessage("Login failed!");
            Border compound = null;
            Border redline = BorderFactory.createLineBorder(Color.red);
            compound = BorderFactory.createCompoundBorder(redline, compound);
            btnSignUp.setBorder(BorderFactory.createCompoundBorder(redline, compound));
            revalidate();
        }
    }
}