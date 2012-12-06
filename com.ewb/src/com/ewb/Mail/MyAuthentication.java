package com.ewb.Mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

class MyAuthentication extends Authenticator {

    public String username,  password;

    public MyAuthentication() {
    }

    public MyAuthentication(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }
}
