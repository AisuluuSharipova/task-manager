package kg.alatoo.task_management.services.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kg.alatoo.task_management.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendVerificationEmail(String to, String verificationLink) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("Email Verification");
        helper.setText(
                "<p>Thank you for registering!</p>" +
                        "<p>Please verify your email by clicking the link below:</p>" +
                        "<p><a href=\"" + verificationLink + "\">Verify Email</a></p>",
                true
        );

        mailSender.send(message);
    }
}
