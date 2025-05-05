package kg.alatoo.task_management.services;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendVerificationEmail(String to, String verificationLink) throws MessagingException;
}
