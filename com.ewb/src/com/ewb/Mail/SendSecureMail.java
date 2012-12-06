package com.ewb.Mail;

import java.util.Properties;
import java.util.Date;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;

public class SendSecureMail {

    public static String to,  from = "auto-mailer@theflop.net";
    public static String username,  password,  mailhost,  subject,  msgBody;
    public static String protocol = "",  port = "",  mailer = "msgsend";
    public static boolean debug = false;

    public static void main(String[] argv) throws Exception {
        //test1000Camels("test 999");
        use1000Camels();
        Send("viktoria2@theflop.net", "subject", "body");
    }

    private static void test1000Camels(String tstMsg) throws Exception {
        msgBody = tstMsg;
        use1000Camels();
        Send("viktoria2@theflop.net", tstMsg, tstMsg);
        Send("elliott@theflop.net", tstMsg, tstMsg);
    }

    public static void use1000Camels() {
        username = "elliott";
        password = "admin8192";
        protocol = "smtp";
        port = "587";
        mailhost = "0001.1000camels.com";
    }

    public static void Send(String inTo, String inSubject, String inMsgBody) throws Exception {
        use1000Camels();
        to = inTo;
        subject = inSubject;
        msgBody = inMsgBody;
        sendSecureMail();
    }

    private static void sendSecureMail() throws Exception {
        Properties props = System.getProperties();
        //props.clear();
        props.put("mail.host", mailhost);
        props.put("mail.Transport.protocol", protocol);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        Authenticator auth = null;
        auth = new MyAuthentication(username, password);
        Session session = Session.getInstance(props, auth);
        session.setDebug(debug);
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
        msg.setSubject(subject, "UTF-8");
        msg.setHeader("X-Mailer", mailer);
        msg.setSentDate(new Date());
        msg.setText(msgBody, "UTF-8");
        Transport transport = session.getTransport(protocol);
        transport.connect(mailhost, username, password);
        Transport.send(msg);
    }
}



