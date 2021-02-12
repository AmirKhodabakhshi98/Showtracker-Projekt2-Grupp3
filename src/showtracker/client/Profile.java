
package showtracker.client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import showtracker.Helper;
import showtracker.User;
import showtracker.client.View.FontsAndColors;

/**
 * @author Basir Ramazani
 * Changes made by Moustafa & Filip
 * <p>
 * A profile panel for user
 */

class Profile extends JPanel {
    private ClientController clientController;
    private User user;
    private JPanel pnlSouth;
    private JLabel lblInputEmail;
    private JTextField txfChangeMail;
    private JTextField txfConfirmPassword;
    private JPasswordField pwfPassword;



    /**
     * Constructor that takes a ClientController instance
     */
    Profile(ClientController clientController) {
        this.clientController = clientController;
        this.setLayout(new BorderLayout());
    }

    /**
     * Refreshes the view
     */
    void draw() {
        removeAll();
        user = clientController.getUser();
        add(profilePicturePanel(), BorderLayout.NORTH);
        add(initiatePanel(), BorderLayout.WEST);
        add(rightFillerPanel(), BorderLayout.CENTER);
        changePanel();
        add(pnlSouth, BorderLayout.SOUTH);
    }


    /**
     * Method to create the right side of the Profile page.
     * Contain the app logo.
     * @return Right side of Profile page.
     * Added by Paul to balance the former GUI.
     */
    private JPanel rightFillerPanel () {
        JPanel panelFillerRight = new JPanel();
        panelFillerRight.setLayout(new BorderLayout());
        panelFillerRight.setBackground(FontsAndColors.getProjectBlue());

        JPanel panelLogo = new JPanel();
        JLabel lbLogo = new JLabel(new ImageIcon(FontsAndColors.getLogo(250, 250)));

        panelLogo.setBackground(FontsAndColors.getProjectBlue());
        JPanel panelGlue = new JPanel();
        panelGlue.setPreferredSize(new Dimension(200, 100));
        panelGlue.setBackground(FontsAndColors.getProjectBlue());

        panelLogo.add(lbLogo);
        panelFillerRight.add(panelGlue, BorderLayout.NORTH);
        panelFillerRight.add(panelLogo, BorderLayout.CENTER);

        return panelFillerRight;
    }


    /**
     * Creates the Profile panel
     *
     * @return
     */
    private JPanel initiatePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 3, 1));
        panel.setBackground(Color.decode("#6A86AA"));

        //Title
        JLabel lblTitle = new JLabel("\t\tMy profile");         //Profile
        lblTitle.setFont(FontsAndColors.getFontTitle(20));

        //Email
        JLabel lblEmail = new JLabel("\t\tEmail:");
        lblEmail.setFont(FontsAndColors.getFontBold(16));
        lblInputEmail = new JLabel(user.getEmail());
        lblInputEmail.setFont(FontsAndColors.getFontPlain(16));

        //Username
        JLabel lblUsername = new JLabel("\t\tUsername:");
        lblUsername.setFont(FontsAndColors.getFontBold(16));
        JLabel lblInputName = new JLabel(user.getUserName());
        lblInputName.setFont(FontsAndColors.getFontPlain(16));

        txfChangeMail = new JTextField();

        panel.add(lblTitle);
        panel.add(new JLabel());
        panel.add(lblEmail);
        panel.add(lblInputEmail);
        panel.add(lblUsername);
        panel.add(lblInputName);
        panel.add(new JLabel());
        panel.add(new JLabel());
        panel.add(new JLabel());
        panel.add(new JLabel());
        panel.add(new JLabel());

        return panel;
    }

    /**
     * Creates a panel with buttons for changing information
     */
    private void changePanel() {
        pnlSouth = new JPanel(new BorderLayout());
        JPanel panel = new JPanel();

        JButton btnChangeEmail = new JButton("Change Email?");
        JButton btnChangePass = new JButton("Change Password?");

        panel.add(btnChangeEmail);
        panel.add(btnChangePass);

        pnlSouth.add(panel, BorderLayout.SOUTH);

        btnChangeEmail.addActionListener(new ValidateEmailListener());

        btnChangePass.addActionListener(new ValidatePasswordListener());
    }

    /**
     * Creates a panel that lets a user change their emailexit.png
     * @return
     */
    private JPanel changeEmailPanel() {
        JPanel panel = new JPanel();
        JLabel lblChangEmail = new JLabel("Enter your email");

        panel.setLayout(new BorderLayout());

        panel.add(lblChangEmail, BorderLayout.NORTH);
        panel.add(txfChangeMail, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates a panel that displays the profile picture in the Profile panel
     * @return
     */
    private JPanel profilePicturePanel() {
        ImageIcon imageIcon = getUserProfilePicture();

        JPanel pnlTop = new JPanel();

        pnlTop.setLayout(new GridLayout(1, 1));
        if (imageIcon != null) {
            Image img = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            ImageIcon imgIcon = new ImageIcon(img);
            JLabel lblImage = new JLabel(imgIcon);
            pnlTop.add(lblImage);
        }

        return pnlTop;
    }

    /**
     * Returns the user's profile picture
     * @return
     */
    private ImageIcon getUserProfilePicture() {
        return user.getProfilePicture();
    }

    /**
     * Returns a panel for changing the user's password
     * @return
     */
    private JPanel changePasswordPanel() {

        JPanel panel = new JPanel();
        panel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        panel.setLayout(new GridLayout(3, 2, 2, 2));

        pwfPassword = new JPasswordField();
        txfConfirmPassword = new JTextField();

        JCheckBox check = new JCheckBox("Show password");

        JLabel lblCurrentPassword = new JLabel("Current password");
        JLabel lblNewPassword = new JLabel("New password");

        panel.add(lblCurrentPassword);
        panel.add(txfConfirmPassword);

        panel.add(lblNewPassword);
        panel.add(pwfPassword);

        panel.add(check);

        check.addActionListener(e -> {
            if (check.isSelected())
                pwfPassword.setEchoChar((char) 0);
            else
                pwfPassword.setEchoChar('*');
        });

        return panel;
    }

    /**
     * An inner class for validating an email address
     */
    private class ValidateEmailListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            int res = JOptionPane.showConfirmDialog(null, changeEmailPanel(), "Change Email",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            while (!(Helper.checkEmailValidity(txfChangeMail.getText())) && res == JOptionPane.OK_OPTION) {

                if (!Helper.checkEmailValidity(txfChangeMail.getText()))
                    JOptionPane.showMessageDialog(null, "Email not valid!", "No Email",
                            JOptionPane.WARNING_MESSAGE);

                res = JOptionPane.showConfirmDialog(null, changeEmailPanel(), "Email", JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE);
            }

            if (res == JOptionPane.OK_OPTION) {
                user.setEmail(txfChangeMail.getText());
                lblInputEmail.setText(user.getEmail());
            }

        }
    }

    /**
     * An inner class for validating a password
     */
    private class ValidatePasswordListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            boolean passwordChanged = false;
            int res;
            do {
                res = JOptionPane.showConfirmDialog(null, changePasswordPanel(), "Change pwfPassword!",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                while (!(Helper.checkPasswordValidity(new String(pwfPassword.getPassword()))) && res == JOptionPane.OK_OPTION) {

                    if (!Helper.checkPasswordValidity(new String(pwfPassword.getPassword())))
                        JOptionPane.showMessageDialog(null,
                                "Your password must contain at least 8 charachters, \n one capital letter,"
                                        + " one small letter and one digit!",
                                "Weak password", JOptionPane.WARNING_MESSAGE);

                    res = JOptionPane.showConfirmDialog(null, changePasswordPanel(), "Change password!",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                }

                if (res == JOptionPane.OK_OPTION) {
                    String reply = clientController.updatePassword(user.getUserName(), txfConfirmPassword.getText(), new String(pwfPassword.getPassword()));
                    if (reply.equals("Password changed")) {
                        JOptionPane.showMessageDialog(null, reply, "Request approved", JOptionPane.INFORMATION_MESSAGE);
                        passwordChanged = true;
                    } else {
                        JOptionPane.showMessageDialog(null, reply, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } while (!passwordChanged && res == JOptionPane.OK_OPTION);
        }
    }
}