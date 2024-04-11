package client.scenes;

import org.junit.jupiter.api.Test;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;

import static org.junit.jupiter.api.Assertions.*;

public class MailTest {

    @Test
    public void makeEmailTest(){
        String fromEmail = "bla@gmail.com";
        String toEmail = "bla@gmail.com";
        String subject = "subject";
        String body = "body";

        Email email = Mail.makeEmail(fromEmail, toEmail, subject, body);
        assertNotNull(email);
        assertEquals(subject, email.getSubject());
    }

    @Test
    public void getSenderInfoTest(){
        String host = "smtp.example.com";
        int port = 587;
        String userEmail = "stijndelangeman49@gmail.com";
        String passwordToken = "txrobxvossaibwat";

        Mailer mailer = Mail.getSenderInfo(host, port, userEmail, passwordToken);
        assertNotNull(mailer);
    }

}
