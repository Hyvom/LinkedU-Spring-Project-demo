package com.linkedu.backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String username, String verificationUrl) {
        String subject = "Verify your LinkedU account";
        String htmlContent = """
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                <h2>Hello %s!</h2>
                <p>Thank you for registering at LinkedU.</p>
                <p><a href="%s" style="background: #007bff; color: white; padding: 12px 24px; text-decoration: none; border-radius: 5px;">Verify your email</a></p>
                <p>Or copy this link: <code>%s</code></p>
                <p>This link expires in 24 hours.</p>
            </div>
            """.formatted(username, verificationUrl, verificationUrl);

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
            System.out.println("✅ Email sent to: " + to);  // Debug
        } catch (MessagingException e) {
            System.err.println("❌ Email failed: " + e.getMessage());
            throw new RuntimeException("Failed to send verification email", e);
        }
    }
}
