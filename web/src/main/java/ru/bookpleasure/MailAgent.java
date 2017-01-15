package ru.bookpleasure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 * Created by Админ on 1/9/2016.
 */
@Service
public class MailAgent {

    @Autowired
    private JavaMailSenderImpl robotMailSender;

    private static final String fromRobotName = "BookPleasure";

    public void sendMailFromRobot(String to, String subject, String text) {
        try{
            MimeMessage message = robotMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(new InternetAddress(robotMailSender.getUsername(), fromRobotName));
            helper.setTo(new InternetAddress(to));
            helper.setSubject(subject);
            helper.setText(text);
            robotMailSender.send(message);
        } catch (Exception mex) {
            throw new RuntimeException(mex);
        }
    }


}
