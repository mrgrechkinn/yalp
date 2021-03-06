package unit;

import org.junit.*;
import yalp.test.*;
import yalp.libs.*;

import models.*;
import notifiers.*;

public class MailTest extends UnitTest {

    @Test
    public void signUpEmail() throws Exception {
        User toto = new User("toto@sampleforum.com", "hello", "Toto");
        Notifier.welcome(toto);
        String email = Mail.Mock.getLastMessageReceivedBy("toto@sampleforum.com");
        assertTrue(email.contains("Subject: Welcome Toto"));
        assertTrue(email.contains("/signup/" + toto.needConfirmation));
    }
}