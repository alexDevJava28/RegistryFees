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

        Properties defaultProps = System.getProperties();

        //sets default properties
        defaultProps.put("mail.smtp.host", "smtp.gmail.com");
        defaultProps.put("mail.smtp.port", "587");
        defaultProps.put("mail.user", "alexwithlenovo@gmail.com");
        defaultProps.put("mail.password", "007007");
        defaultProps.put("mail.smtp.starttls.enable", "true");
        defaultProps.put("mail.smtp.auth", "true");
        defaultProps.put("mail.debug", "true");

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

        configProps.put("mail.smtp.host", host);
        configProps.put("mail.smtp.port", port);
        configProps.put("mail.user", user);
        configProps.put("mail.password", pass);
        configProps.put("mail.smtp.starttls.enable", "true");
        configProps.put("mail.smtp.auth", "true");
        configProps.put("mail.debug", "true");

        OutputStream outputStream = new FileOutputStream(configFile);
        configProps.store(outputStream, "host settings");
        outputStream.close();
    }
}
