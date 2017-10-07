package com.oyyx.weektag.utils;

import java.util.Date;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by 123 on 2017/10/7.
 */

public class EmailHelper {

    private static final String myEmailAccount = "768471488@qq.com";
    private static final String myEmailPassword = "gtnwlahiuhkxbcgi";
    private static final String myEmailSMTPHost = "smtp.qq.com";
    private static final String receiveMainAccount = "768471488@qq.com";

    public static void sendEmail(String text,String userAccount) throws Exception {

        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.host", myEmailSMTPHost);
        properties.setProperty("mail.smtp.auth", "true");

        final String smtpPort = "465";
        properties.setProperty("mail.smtp.port", smtpPort);
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.socketFactory.fallback", "false");
        properties.setProperty("mail.smtp.socketFactory.port", smtpPort);

        Session session = Session.getDefaultInstance(properties);
        session.setDebug(true);

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(myEmailAccount, "15295528630", "UTF-8"));
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(myEmailAccount, "欧阳宇翔", "UTF-8"));
        message.setSubject("FeedBack From"+userAccount, "UTF-8");
        message.setContent(text, "text/plain;charset=UTF-8");
        message.setSentDate(new Date());
        message.saveChanges();

        Transport transport = session.getTransport();
        transport.connect(myEmailAccount, myEmailPassword);

        transport.sendMessage(message, message.getAllRecipients());

        transport.close();
    }



}
