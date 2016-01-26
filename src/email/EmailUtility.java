package email;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * Created by khodackovskiy on 26.01.2016.
 */
public class EmailUtility {

    public static void sendEmail (Properties smtpProperties,
                                  String toAddress,
                                  String subject,
                                  String message,
                                  File[] attachFiles)
            throws AddressException,MessagingException, IOException{

        final String userName = smtpProperties.getProperty("mail.user");
        final String password = smtpProperties.getProperty("mail.password");

        //creates a new session with an authenticator
        Authenticator auth = new Authenticator() {

            public PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(userName, password);
            }
        };

        Session session = Session.getInstance(smtpProperties);
        Store store = session.getStore("smtp");
        store.connect("smtp.gmail.com", userName, password);

        //creates a new email message
        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses = {new InternetAddress(toAddress)};
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());

        //creates message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(message, "text/html");

        //creates multi-part
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        //adds attachments
        if (attachFiles != null && attachFiles.length > 0){

            for (File aFile : attachFiles){

                MimeBodyPart attachPart = new MimeBodyPart();

                try{

                    attachPart.attachFile(aFile);

                }catch (IOException e){

                    throw e;
                }

                multipart.addBodyPart(attachPart);
            }
        }

        //sets the multi-part as e-mail's content
        msg.setContent(multipart);

        //sends the e-mail
        Transport.send(msg);
    }
}
