package client.scenes;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

public class Mail {

    public static Email makeEmail(String fromEmail, String toEmail, String subject, String body){
        Email email = EmailBuilder.startingBlank()
                .from(fromEmail)
                .to(toEmail)
                .withSubject(subject)
                .withPlainText(body)
                .buildEmail();

        return email;
    }

    public static Mailer getSenderInfo(String host, int port, String userEmail, String passwordToken){
        Mailer mailerInfo = MailerBuilder
                .withSMTPServerHost(host)
                .withSMTPServerPort(port)
                .withSMTPServerUsername(userEmail)
                .withSMTPServerPassword(passwordToken)
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .buildMailer();
        return mailerInfo;
    }

    public static void mailSending(Email email, Mailer mailerInfo){
        mailerInfo.sendMail(email);
        System.out.println("an Email has been succesfully send");
    }

}
