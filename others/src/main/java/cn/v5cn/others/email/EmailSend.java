package cn.v5cn.others.email;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class EmailSend {
    public static void main(String[] args) throws EmailException {
        Email email = new SimpleEmail();
        email.setHostName("163.com");
        email.setSmtpPort(25);
        email.setDebug(true);
        email.setSslSmtpPort("25");
        email.setAuthenticator(new DefaultAuthenticator("xxx", "xxxxx"));
        email.setSSLOnConnect(false);
        email.setCharset("UTF-8");
        email.setSubject("TestMail");
        email.setFrom("xxxx@163.com");
        email.setMsg("你好,This is a test mail ... :-)");
        email.addTo("xxxx@163.com");
        email.send();
        System.out.println("Success!");
    }
}
