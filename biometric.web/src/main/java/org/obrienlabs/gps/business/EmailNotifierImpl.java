package org.obrienlabs.gps.business;

import java.util.Properties;

import org.obrienlabs.gps.business.EmailNotifier;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailNotifierImpl implements EmailNotifier {

	public static void main(String[] args) {
		EmailNotifierImpl notifier = new EmailNotifierImpl();
		notifier.sendmail();

	}
	
	@Override
	public void sendmail() {
		Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.mandrillapp.com");//"localhost");
        props.put("mail.smtp.port", "587");//"25");
        props.put("mail.smtp.username", "x@obrienlabs.org");
        props.put("mail.smtp.password", "xb71_GT5fxvW1FsbrJR1JYvLcA");
        Session session = Session.getDefaultInstance(props, null);
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("l@nutridat.org"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("ml@nutridat.org"));
            message.setSubject("File Copy Error3");
            message.setText(
                "Dear Administrator,\n\n" +
                "An error occurred when copying the following file :\n");
            Transport.send(message);
            System.out.println("sent");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
	}

}
