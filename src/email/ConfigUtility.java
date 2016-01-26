package email;

import java.io.*;
import java.util.Properties;

/**
 * Created by khodackovskiy on 26.01.2016.
 */
public class ConfigUtility {

    private File configFile = new File("smtp.properties");
    private Properties configProps;

    public Properties loadProperties() throws IOException{

        Properties defaultProps = new Properties();

        //sets default properties
        defaultProps.put("mail.smtp.ssl.enable", "true"); //required for gmail
        defaultProps.put("mail.smtp.auth.mechanisms", "XOAUTH2");
        defaultProps.setProperty("mail.smtp.host", "smtps.gmail.com");
        defaultProps.setProperty("mail.smtp.port", "587");
        defaultProps.setProperty("mail.user", "alexwithlenovo@gmail.com");
        defaultProps.setProperty("mail.password", "007007");
        defaultProps.setProperty("mail.smtp.starttls.enable", "true");
        defaultProps.setProperty("mail.smtp.auth", "true");

        configProps = new Properties(defaultProps);

        //loads properties from file
        if (configFile.exists()){

            InputStream inputStream = new FileInputStream(configFile);
            configProps.load(inputStream);
            inputStream.close();
        }

        return configProps;
    }

    public void saveProperties(String host,
                               String port,
                               String user,
                               String pass)
        throws IOException{

        configProps.put("mail.smtp.ssl.enable", "true"); //required for gmail
        configProps.put("mail.smtp.auth.mechanisms", "XOAUTH2");
        configProps.setProperty("mail.smtp.host", host);
        configProps.setProperty("mail.smtp.port", port);
        configProps.setProperty("mail.user", user);
        configProps.setProperty("mail.password", pass);
        configProps.setProperty("mail.smtp.starttls.enable", "true");
        configProps.setProperty("mail.smtp.auth", "true");

        OutputStream outputStream = new FileOutputStream(configFile);
        configProps.store(outputStream, "host settings");
        outputStream.close();
    }
}
