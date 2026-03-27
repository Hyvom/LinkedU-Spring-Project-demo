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
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset="UTF-8">
            </head>
            
            <body style="margin:0; padding:0; background:#0f172a; font-family:Arial,sans-serif;">
            
              <div style="max-width:600px; margin:40px auto; background:#111c33; border-radius:16px; overflow:hidden; box-shadow:0 10px 30px rgba(0,0,0,0.4);">
            
                <!-- TOP BAR -->
                <div style="height:5px; background:#fbbf24;"></div>
            
                <!-- LOGO -->
                <div style="text-align:center; padding:25px 20px 10px;">
                  <img src="http://localhost:8080/logo.png" 
                       alt="LinkedU Logo"
                       style="height:60px; object-fit:contain;" />
                </div>
            
                <!-- TITLE -->
                <div style="padding:10px 30px; text-align:center;">
                  <h2 style="color:#f8fafc; margin-bottom:10px;">Welcome to LinkedU, %s 👋</h2>
                  <p style="color:#94a3b8; font-size:14px;">
                    Almost there! Just confirm your email to activate your account.
                  </p>
                </div>
            
                <!-- BUTTON -->
                <div style="text-align:center; padding:20px;">
                  <a href="%s"
                     style="
                       display:inline-block;
                       background:#fbbf24;
                       color:#0f172a;
                       padding:12px 28px;
                       font-weight:bold;
                       border-radius:10px;
                       text-decoration:none;
                     ">
                    Verify Your Email
                  </a>
                </div>
            
                <!-- LINK -->
                <div style="padding:10px 30px; text-align:center;">
                  <p style="color:#94a3b8; font-size:12px;">
                    If the button doesn't work, copy this link:
                  </p>
                  <code style="color:#fbbf24; font-size:12px; word-break:break-all;">
                    %s
                  </code>
                </div>
            
                <!-- FOOTER -->
                <div style="padding:20px; text-align:center; color:#64748b; font-size:11px;">
                  This link expires in 24 hours.<br/>
                  © LinkedU - Study Abroad Platform
                </div>
            
              </div>
            
            </body>
            </html>
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
