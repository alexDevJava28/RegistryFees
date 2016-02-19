package email;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * A Swing application that allows sending e-mail messages from a SMTP server.
 * @author www.codejava.net
 *
 */
public class SwingEmailSender extends JFrame {

    private ConfigUtility configUtil = new ConfigUtility();

    private JMenuBar menuBar;
    private JMenu menuFile;
    private JMenuItem menuItemSetting;

    private JLabel labelTo;
    private JLabel labelSubject;

    private JTextField fieldTo;
    private JTextField fieldSubject;

    private JButton buttonSend;

    private JFilePicker filePicker;

    private JTextArea textAreaMessage;

    private GridBagConstraints constraints;

    private ResourceBundle resString;

    public SwingEmailSender(ResourceBundle resString) {

        super("E-mail");
        this.resString = resString;

        configUtil = new ConfigUtility();

        menuBar = new JMenuBar();
        menuFile = new JMenu(resString.getString("emailMenu"));
        menuItemSetting = new JMenuItem(resString.getString("emailMenuSettings"));
        labelTo = new JLabel(resString.getString("emailTo"));
        labelSubject = new JLabel(resString.getString("emailSubject"));
        fieldTo = new JTextField(30);
        fieldSubject = new JTextField(30);
        buttonSend = new JButton(resString.getString("emailButtonSend"));
        filePicker = new JFilePicker(resString.getString("emailAttached"), resString.getString("emailButtonAttach"));
        textAreaMessage = new JTextArea(10, 30);
        constraints = new GridBagConstraints();

        // set up layout
        setLayout(new GridBagLayout());
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);

        setupMenu();
        setupForm();

        pack();
        setLocationRelativeTo(null);    // center on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void setupMenu() {

        menuItemSetting.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                SettingsDialog dialog = new SettingsDialog(SwingEmailSender.this, configUtil, resString);
                dialog.setVisible(true);
            }
        });

        menuFile.add(menuItemSetting);
        menuBar.add(menuFile);
        setJMenuBar(menuBar);
    }

    private void setupForm() {

        constraints.gridx = 0;
        constraints.gridy = 0;
        add(labelTo, constraints);

        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(fieldTo, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        add(labelSubject, constraints);

        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(fieldSubject, constraints);

        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.gridheight = 2;
        constraints.fill = GridBagConstraints.BOTH;
        buttonSend.setFont(new Font("Arial", Font.BOLD, 16));
        add(buttonSend, constraints);

        buttonSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {

                buttonSendActionPerformed(event);
            }
        });

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridheight = 1;
        constraints.gridwidth = 3;
        filePicker.setMode(JFilePicker.MODE_OPEN);
        add(filePicker, constraints);

        constraints.gridy = 3;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;

        add(new JScrollPane(textAreaMessage), constraints);
    }

    private void buttonSendActionPerformed(ActionEvent event) {

        if (!validateFields()) {
            return;
        }

        String toAddress = fieldTo.getText();
        String subject = fieldSubject.getText();
        String message = textAreaMessage.getText();

        File[] attachFiles = null;

        if (!filePicker.getSelectedFilePath().equals("")) {
            File selectedFile = new File(filePicker.getSelectedFilePath());
            attachFiles = new File[] {selectedFile};
        }

        try {
            Properties smtpProperties = configUtil.loadProperties();
            EmailUtility.sendEmail(smtpProperties, toAddress, subject, message, attachFiles);

            JOptionPane.showMessageDialog(this,
                    "The e-mail has been sent successfully!");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error while sending the e-mail: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateFields() {

        if (fieldTo.getText().equals("")) {
            JOptionPane.showMessageDialog(this,
                    "Please enter To address!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            fieldTo.requestFocus();
            return false;
        }

        if (fieldSubject.getText().equals("")) {
            JOptionPane.showMessageDialog(this,
                    "Please enter subject!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            fieldSubject.requestFocus();
            return false;
        }

        if (textAreaMessage.getText().equals("")) {
            JOptionPane.showMessageDialog(this,
                    "Please enter message!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            textAreaMessage.requestFocus();
            return false;
        }

        return true;
    }
}