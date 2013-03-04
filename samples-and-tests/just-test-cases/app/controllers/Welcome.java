package controllers;

import org.apache.commons.mail.EmailException;
import yalp.mvc.*;

public class Welcome extends Mailer {
    
    public static void welcome() {
        String msg = "Welcome2";
        setFrom("x@x.com");
        setSubject("Yop2");
        addRecipient("toto@localhost");
        send(msg);
    }

}
