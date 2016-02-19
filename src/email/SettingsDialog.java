package email;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Created by khodackovskiy on 26.01.2016.
 */
public class SettingsDialog extends JDialog{

    private ConfigUtility configUtil;

    private JLabel labelHost;
    private JLabel labelPort;
    private JLabel labelUser;
    private JLabel labelPass;

    private JTextField textHost;
    private JTextField textPort;
    private JTextField textUser;
    private JTextField textPass;

    private JButton buttonSave;

    public SettingsDialog(JFrame parent, ConfigUtility configUtil, ResourceBundle resString) {

        super(parent, "SMTP Settings", true);
        this.configUtil = configUtil;

        labelHost = new JLabel(resString.getString("settingsHostName"));
        labelPort = new JLabel(resString.getString("settingsPortNumber"));
        labelUser = new JLabel(resString.getString("settingsUsername"));
        labelPass = new JLabel(resString.getString("settingsPassword"));

        textHost = new JTextField(20);
        textPort = new JTextField(20);
        textUser = new JTextField(20);
        textPass = new JTextField(20);

        buttonSave = new JButton(resString.getString("settingsButtonSave"));

        setupForm();

        loadSettings();

        pack();
        setLocationRelativeTo(null);
    }

    private void setupForm() {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(10, 10, 5, 10);
        constraints.anchor = GridBagConstraints.WEST;

        add(labelHost, constraints);

        constraints.gridx = 1;
        add(textHost, constraints);

        constraints.gridy = 1;
        constraints.gridx = 0;
        add(labelPort, constraints);

        constraints.gridx = 1;
        add(textPort, constraints);

        constraints.gridy = 2;
        constraints.gridx = 0;
        add(labelUser, constraints);

        constraints.gridx = 1;
        add(textUser, constraints);

        constraints.gridy = 3;
        constraints.gridx = 0;
        add(labelPass, constraints);

        constraints.gridx = 1;
        add(textPass, constraints);

        constraints.gridy = 4;
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        add(buttonSave, constraints);

        buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                buttonSaveActionPerformed(event);
            }
        });
    }

    private void loadSettings() {
        Properties configProps = null;
        try {
            configProps = configUtil.loadProperties();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error reading settings: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        textHost.setText(configProps.getProperty("mail.smtp.host"));
        textPort.setText(configProps.getProperty("mail.smtp.port"));
        textUser.setText(configProps.getProperty("mail.user"));
        textPass.setText(configProps.getProperty("mail.password"));
    }

    private void buttonSaveActionPerformed(ActionEvent event) {
        try {
            configUtil.saveProperties(textHost.getText(),
                    textPort.getText(),
                    textUser.getText(),
                    textPass.getText());
            JOptionPane.showMessageDialog(SettingsDialog.this,
                    "Properties were saved successfully!");
            dispose();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error saving properties file: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
