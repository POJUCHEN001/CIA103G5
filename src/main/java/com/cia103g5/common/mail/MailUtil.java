package com.cia103g5.common.mail;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**##################################################
 #                                                  #
 #                                                  #
 #                  mail util                       #
 #                                                  #
 #                                                  #
 ###################################################*/

@Component
public class MailUtil {

    /**
     * 寄送 mail
     * @param mailModel 請先繼承MailModel
     * @param subject 標題
     * @param content 內文
     */
    public static void sendEmail(MailModel mailModel, String subject, String content) {
        seed(mailModel.getEmail(), subject, content);
    }

    public static void seed(String mail, String subject, String content) {
        String host = "smtp.gmail.com";
        final String username = "cia103.g5@gmail.com";
        final String password = "zvmt liuu ryxi tvxe";
        String to = mail;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        // 获取 Session 实例
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
