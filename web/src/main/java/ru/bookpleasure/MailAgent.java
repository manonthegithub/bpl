package ru.bookpleasure;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by Админ on 1/9/2016.
 */
public class MailAgent {

    private static final Properties properties = System.getProperties();
    // Sender's email ID needs to be mentioned
    private static final String fromAdmin = "robot@bookpleasure.ru";
    private static final String adminPass = "1qaz@WSX";
    private static final String smtpHost = "smtp.yandex.ru";
    private static final int smptPort = 465;

    private static Session session =  Session.getDefaultInstance(
        properties,
        new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromAdmin, adminPass);
            }
        }
    );
    // Setup mail server
    static {
        properties.setProperty("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smptPort);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.socketFactory.port", smptPort);
        properties.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
    }

    public static void sendMailFromRobot(String to, String subject, String text){
        try{
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(fromAdmin));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject(subject);

            // Now set the actual message
            message.setText(text);

            // Send message
            Transport.send(message);
        }catch (MessagingException mex) {
            throw new RuntimeException(mex);
        }
    }


}
