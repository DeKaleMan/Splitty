package client.utils;

import client.utils.Mail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MailTest {

    @Spy
    Mail sut;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void makeEmailTest(){
        String fromEmail = "bla@gmail.com";
        String toEmail = "bla@gmail.com";
        String subject = "subject";
        String body = "body";

        Email email = sut.makeEmail(fromEmail, toEmail, subject, body);
        assertNotNull(email);
        assertEquals(subject, email.getSubject());
    }

    @Test
    public void getSenderInfoTest(){
        String host = "smtp.example.com";
        int port = 587;
        String userEmail = "stijndelangeman49@gmail.com";
        String passwordToken = "txrobxvossaibwat";

        Mailer mailer = sut.getSenderInfo(host, port, userEmail, passwordToken);
        assertNotNull(mailer);
    }

    @Test
    void mailSending(){
        Mailer mailer = mock(Mailer.class);
        Email email = mock(Email.class);

        sut.mailSending(email, mailer);

        verify(mailer).sendMail(email);
    }

}
