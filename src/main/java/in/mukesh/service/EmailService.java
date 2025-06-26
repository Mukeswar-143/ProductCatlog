package in.mukesh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendRegistrationEmail(String toEmail, String userName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("vasanamukeswar@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Registration Successful");
        message.setText("Hi " + userName + ",\n\nThank you for registering with us!");

        mailSender.send(message);
    }
}
