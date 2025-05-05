package kg.alatoo.task_management.services.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kg.alatoo.task_management.services.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

class EmailServiceImplTest {

    private JavaMailSender mailSender;
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        mailSender = mock(JavaMailSender.class);
        emailService = new EmailServiceImpl(mailSender);
    }

    @Test
    void sendVerificationEmail_ShouldSendEmailSuccessfully() throws MessagingException {
        String to = "test@example.com";
        String link = "http://localhost:8080/api/auth/verify?token=abc123";

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        assertThatCode(() -> emailService.sendVerificationEmail(to, link)).doesNotThrowAnyException();

        verify(mailSender, times(1)).send(mimeMessage);
    }
}
