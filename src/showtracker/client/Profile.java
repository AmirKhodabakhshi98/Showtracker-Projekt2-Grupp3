
package showtracker.client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import showtracker.Helper;
import showtracker.User;

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
     *
     * @param cc
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
        add(initiatePanel(), BorderLayout.CENTER);
        changePanel();
        add(pnlSouth, BorderLayout.SOUTH);
    }

    /**
     * Creates the Profile panel
     *
     * @return
     */
    private JPanel initiatePanel() {
        JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(3, 2, 6, 1));
        JLabel lblInputName = new JLabel(user.getUserName());
        lblInputEmail = new JLabel(user.getEmail());

        JLabel lblTitle = new JLabel(" My profile");

        JLabel lblUsername = new JLabel("    Username:  ");
        JLabel lblEmail = new JLabel("    Email:  ");
        txfChangeMail = new JTextField();
        panel.add(lblTitle);
        panel.add(new JLabel());

        panel.add(lblUsername);
        panel.add(lblInputName);

        panel.add(lblEmail);
        panel.add(lblInputEmail);

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