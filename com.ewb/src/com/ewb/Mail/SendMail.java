package com.ewb.Mail;

import java.util.Properties;
import java.util.Date;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Message;

public class SendMail {

    // these from 2kal
    public static int debugLevel = 0;

    public static void main(String[] argv) throws Exception {
        //SendVia1000Camels("elliott@finitemind.com", "test subj", "test msg");
        SendViaServerL("elliott@finitemind.com", "test subj", "test msg");
        
    }

    public static void Send(String to, String subject, String msgText) throws Exception {
        SendViaServerL(to,subject,msgText);
    }
    
    public static void SendViaServerL(String to, String subject, String msgText) throws Exception {
        boolean debug = true;
        String mailer = "SendMail";
        String from = "elliott@finitemind.com";
        String mailhost = "poker.theflop.net";
        String user = "elliott", password = "@dm1n8192";
        try {
            Properties props = System.getProperties();
            props.put("mail.smtp.host", mailhost);
            props.put("mail.smtp.port", "25"); // 25-normal 587-darcy
            props.put("mail.smtp.starttls.enable", "true"); // true-darcy
            Session session = Session.getDefaultInstance(props, null);
            if (debugLevel > 0) {
                session.setDebug(debug);
            }
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setSubject(subject);
            StringBuffer sb = new StringBuffer();
            sb.append(msgText);
            msg.setText(sb.toString());
            msg.setHeader("X-Mailer", mailer);
            msg.setSentDate(new Date());
            Transport.send(msg);
        } catch (Exception e) {
            //e.printStackTrace();
            throw e;
        }
    }
    
    public static void SendVia1000Camels(String to, String subject, String msgText) throws Exception {
        boolean debug = true;
        String mailer = "SendMail";
        String from = "elliott@finitemind.com";
        String mailhost = "0001.1000camels.com";
        String user = "elliott", password = "admin8192";
        try {
            Properties props = System.getProperties();
            props.put("mail.smtp.host", mailhost);
            props.put("mail.smtp.port", "587"); // 25-normal 587-darcy
            props.put("mail.smtp.starttls.enable", "true"); // true-darcy
            Session session = Session.getDefaultInstance(props, null);
            if (debugLevel > 0) {
                session.setDebug(debug);
            }
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setSubject(subject);
            StringBuffer sb = new StringBuffer();
            sb.append(msgText);
            msg.setText(sb.toString());
            msg.setHeader("X-Mailer", mailer);
            msg.setSentDate(new Date());
            Transport.send(msg);
        } catch (Exception e) {
            //e.printStackTrace();
            throw e;
        }
    }
}
