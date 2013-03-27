package yalp.libs;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.junit.Test;

import yalp.YalpBuilder;
import yalp.exceptions.MailException;

public class MailTest {

    @Test(expected = MailException.class)
    public void buildMessageWithoutFrom() throws EmailException {
        new YalpBuilder().build();

        Email email = new SimpleEmail();
        email.addTo("from@yalpframework.com");
        email.setSubject("subject");
        Mail.buildMessage(new SimpleEmail());
    }

    @Test(expected = MailException.class)
    public void buildMessageWithoutRecipient() throws EmailException {
        new YalpBuilder().build();

        Email email = new SimpleEmail();
        email.setFrom("from@yalpframework.com");
        email.setSubject("subject");
        Mail.buildMessage(email);
    }

    @Test(expected = MailException.class)
    public void buildMessageWithoutSubject() throws EmailException {
        new YalpBuilder().build();

        Email email = new SimpleEmail();
        email.setFrom("from@yalpframework.com");
        email.addTo("to@yalpframework.com");
        Mail.buildMessage(email);
    }

    @Test
    public void buildValidMessages() throws EmailException {
        new YalpBuilder().build();

        Email email = new SimpleEmail();
        email.setFrom("from@yalpframework.com");
        email.addTo("to@yalpframework.com");
        email.setSubject("subject");
        Mail.buildMessage(email);

        email = new SimpleEmail();
        email.setFrom("from@yalpframework.com");
        email.addCc("to@yalpframework.com");
        email.setSubject("subject");
        Mail.buildMessage(email);

        email = new SimpleEmail();
        email.setFrom("from@yalpframework.com");
        email.addBcc("to@yalpframework.com");
        email.setSubject("subject");
        Mail.buildMessage(email);
    }
}
