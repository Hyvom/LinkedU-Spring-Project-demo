package com.linkedu.backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public void sendEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
            System.out.println("✅ Email sent to: " + to);
        } catch (MessagingException e) {
            System.err.println("❌ Email failed: " + e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }
    public void sendTicketNotification(String agentEmail, String studentName, Long ticketId) {
        String subject = "🆕 New Support Ticket #" + ticketId;
        String htmlContent = """
                <div style="font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; background-color: #F4F5F0;">
                
                    <!-- Main Card -->
                    <div style="background: white; border-radius: 20px; overflow: hidden; box-shadow: 0 8px 32px rgba(0, 7, 45, 0.1);">
                
                        <!-- Header -->
                        <div style="background: linear-gradient(135deg, #1E4F70 0%%, #0E3A55 100%%); padding: 25px 30px; text-align: center;">
                            <h2 style="margin: 0; color: white; font-size: 24px; font-weight: 700; display: flex; align-items: center; justify-content: center; gap: 10px;">
                                <span>🎫</span> New Student Support Ticket
                            </h2>
                        </div>
                
                        <!-- Content -->
                        <div style="padding: 30px;">
                            <!-- Ticket Info -->
                            <div style="margin-bottom: 25px;">
                                <div style="display: flex; align-items: center; gap: 12px; margin-bottom: 15px; padding-bottom: 15px; border-bottom: 2px solid #F4F5F0;">
                                    <div style="width: 40px; height: 40px; background: linear-gradient(135deg, #d9c682 0%%, #c4b56a 100%%); border-radius: 50%%; display: flex; align-items: center; justify-content: center; font-size: 18px;">👤</div>
                                    <div>
                                        <p style="margin: 0; font-size: 11px; color: #64748b; text-transform: uppercase; letter-spacing: 0.5px;">Student</p>
                                        <p style="margin: 0; font-size: 16px; font-weight: 600; color: #1E4F70;">%s</p>
                                    </div>
                                </div>
                
                                <div style="display: flex; align-items: center; gap: 12px;">
                                    <div style="width: 40px; height: 40px; background: linear-gradient(135deg, #1E4F70 0%%, #0E3A55 100%%); border-radius: 50%%; display: flex; align-items: center; justify-content: center; font-size: 18px;">🎫</div>
                                    <div>
                                        <p style="margin: 0; font-size: 11px; color: #64748b; text-transform: uppercase; letter-spacing: 0.5px;">Ticket ID</p>
                                        <p style="margin: 0; font-size: 16px; font-weight: 600;">
                                            <a href="http://localhost:4200/agent?tab=tickets" style="color: #d9c682; text-decoration: none; border-bottom: 2px solid #d9c682;">#%d</a>
                                        </p>
                                    </div>
                                </div>
                            </div>
                
                            <!-- Ticket Details Card -->
                            <div style="background: #F4F5F0; border-radius: 16px; padding: 20px; margin: 25px 0; border-left: 4px solid #d9c682;">
                                <h3 style="margin: 0 0 15px 0; font-size: 16px; font-weight: 700; color: #1E4F70; display: flex; align-items: center; gap: 8px;">
                                    <span>📋</span> Ticket Details
                                </h3>
                
                                <div style="margin-bottom: 15px;">
                                    <p style="margin: 0 0 5px 0; font-size: 11px; color: #64748b; text-transform: uppercase; letter-spacing: 0.5px;">Object</p>
                                    <p style="margin: 0; font-size: 14px; font-weight: 500; color: #1E4F70;">%s</p>
                                </div>
                
                                <div style="margin-bottom: 15px;">
                                    <p style="margin: 0 0 5px 0; font-size: 11px; color: #64748b; text-transform: uppercase; letter-spacing: 0.5px;">Description</p>
                                    <p style="margin: 0; font-size: 14px; color: #475569; line-height: 1.5;">%s</p>
                                </div>
                
                                <div>
                                    <p style="margin: 0 0 5px 0; font-size: 11px; color: #64748b; text-transform: uppercase; letter-spacing: 0.5px;">Student Availability</p>
                                    <p style="margin: 0; font-size: 14px; font-weight: 500; color: #d9c682; display: flex; align-items: center; gap: 6px;">
                                        <span>🕐</span> %s
                                    </p>
                                </div>
                            </div>
                
                            <!-- CTA Button -->
                            <div style="text-align: center; margin: 30px 0;">
                                <a href="http://localhost:4200/agent?tab=tickets"\s
                                   style="display: inline-block; background: linear-gradient(135deg, #d9c682 0%%, #c4b56a 100%%); color: #1E4F70; padding: 14px 32px; text-decoration: none; border-radius: 12px; font-weight: 700; font-size: 16px; transition: all 0.3s ease; box-shadow: 0 4px 12px rgba(217, 198, 130, 0.3);">
                                    🚀 Review Ticket Now
                                </a>
                            </div>
                
                            <!-- Help Text -->
                            <div style="background: #F4F5F0; border-radius: 12px; padding: 15px; text-align: center; margin-top: 20px;">
                                <p style="margin: 0; font-size: 12px; color: #64748b;">
                                    <span>💡</span> Log in to your agent dashboard to respond to this ticket
                                </p>
                            </div>
                        </div>
                
                        <!-- Footer -->
                        <div style="background: #F4F5F0; padding: 20px 30px; text-align: center; border-top: 1px solid #e2e8f0;">
                            <p style="margin: 0; font-size: 11px; color: #64748b;">
                                © 2024 LinkedU Study Abroad Platform. All rights reserved.
                            </p>
                            <p style="margin: 10px 0 0 0; font-size: 11px; color: #94a3b8;">
                                This is an automated notification, please do not reply to this email.
                            </p>
                        </div>
                    </div>
                </div>
        """.formatted(studentName, ticketId, ticketId, "Visa Issue", "Need help...", "2026-04-10 14:00", ticketId);

        sendEmail(agentEmail, subject, htmlContent);
    }

    public void sendMeetLink(String studentEmail, String agentName, String meetLink, LocalDateTime dateTime) {
        String subject = "Meeting Confirmed - LinkedU Support";
        String formattedDate = dateTime.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy 'at' hh:mm a"));

        String htmlContent = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                </head>
                <body style="margin: 0; padding: 0; font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; background-color: #F4F5F0;">
                    <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                
                        <!-- Main Card -->
                        <div style="background: white; border-radius: 24px; overflow: hidden; box-shadow: 0 8px 32px rgba(0, 7, 45, 0.1);">
                
                            <!-- Header -->
                            <div style="background: linear-gradient(135deg, #10b981 0%%, #059669 100%%); padding: 30px; text-align: center; border-bottom: 3px solid #d9c682;">
                                <div style="width: 70px; height: 70px; background: rgba(255, 255, 255, 0.2); border-radius: 50%%; display: inline-flex; align-items: center; justify-content: center; margin-bottom: 15px;">
                                    <span style="font-size: 35px;">🎉</span>
                                </div>
                                <h1 style="margin: 0 0 10px 0; font-size: 28px; font-weight: 700; color: white;">
                                    Meeting Confirmed!
                                </h1>
                                <p style="margin: 0; color: rgba(255, 255, 255, 0.9); font-size: 16px;">
                                    Your support request has been accepted
                                </p>
                            </div>
                
                            <!-- Content -->
                            <div style="padding: 30px;">
                                <!-- Greeting -->
                                <div style="text-align: center; margin-bottom: 25px;">
                                    <p style="margin: 0 0 5px 0; font-size: 14px; color: #64748b;">Dear Student,</p>
                                    <p style="margin: 0; font-size: 18px; font-weight: 600; color: #1E4F70;">
                                        Agent <span style="color: #d9c682;">%s</span> has scheduled a meeting with you
                                    </p>
                                </div>
                
                                <!-- Meeting Details Card -->
                                <div style="background: linear-gradient(135deg, #ecfdf5 0%%, #d1fae5 100%%); border-radius: 16px; padding: 25px; margin-bottom: 25px; border: 1px solid #10b981;">
                                    <h3 style="margin: 0 0 20px 0; font-size: 18px; font-weight: 700; color: #065f46; display: flex; align-items: center; justify-content: center; gap: 8px;">
                                        <span>📅</span> Meeting Details
                                    </h3>
                
                                    <div style="margin-bottom: 20px;">
                                        <div style="display: flex; align-items: center; gap: 12px; padding: 12px; background: white; border-radius: 12px; margin-bottom: 12px;">
                                            <div style="width: 40px; height: 40px; background: #10b981; border-radius: 50%%; display: flex; align-items: center; justify-content: center; font-size: 20px;">🕐</div>
                                            <div>
                                                <p style="margin: 0; font-size: 11px; color: #64748b; text-transform: uppercase;">Date & Time</p>
                                                <p style="margin: 0; font-size: 15px; font-weight: 600; color: #1E4F70;">%s</p>
                                            </div>
                                        </div>
                
                                        <div style="display: flex; align-items: center; gap: 12px; padding: 12px; background: white; border-radius: 12px;">
                                            <div style="width: 40px; height: 40px; background: #d9c682; border-radius: 50%%; display: flex; align-items: center; justify-content: center; font-size: 20px;">🎥</div>
                                            <div>
                                                <p style="margin: 0; font-size: 11px; color: #64748b; text-transform: uppercase;">Google Meet Link</p>
                                                <p style="margin: 0; font-size: 13px; font-family: monospace; color: #1E4F70; word-break: break-all;">%s</p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                
                                <!-- CTA Button -->
                                <div style="text-align: center; margin: 30px 0;">
                                    <a href="%s"\s
                                       style="display: inline-block; background: linear-gradient(135deg, #10b981 0%%, #059669 100%%); color: white; padding: 16px 40px; text-decoration: none; border-radius: 50px; font-weight: 700; font-size: 18px; transition: all 0.3s ease; box-shadow: 0 4px 16px rgba(16, 185, 129, 0.3);">
                                        🟢 Join Google Meet
                                    </a>
                                </div>
                
                                <!-- Tips Card -->
                                <div style="background: #F4F5F0; border-radius: 12px; padding: 20px; margin-top: 20px;">
                                    <h4 style="margin: 0 0 10px 0; font-size: 14px; font-weight: 700; color: #1E4F70; display: flex; align-items: center; gap: 6px;">
                                        <span>💡</span> Meeting Tips
                                    </h4>
                                    <ul style="margin: 0; padding-left: 20px; color: #475569; font-size: 13px; line-height: 1.6;">
                                        <li>Test your microphone and camera before the meeting</li>
                                        <li>Join 5 minutes early to ensure everything works</li>
                                        <li>Have your questions ready for the agent</li>
                                        <li>Ensure you have a stable internet connection</li>
                                    </ul>
                                </div>
                            </div>
                
                            <!-- Footer -->
                            <div style="background: #F4F5F0; padding: 20px 30px; text-align: center; border-top: 1px solid #e2e8f0;">
                                <p style="margin: 0; font-size: 11px; color: #64748b;">
                                    © 2024 LinkedU Study Abroad Platform. All rights reserved.
                                </p>
                                <p style="margin: 10px 0 0 0; font-size: 11px; color: #94a3b8;">
                                    Need help? Contact us at <a href="mailto:support@linkedu.com" style="color: #d9c682; text-decoration: none;">support@linkedu.com</a>
                                </p>
                            </div>
                        </div>
                    </div>
                </body>
                </html>
        """.formatted(agentName, formattedDate, meetLink, meetLink, meetLink);

        sendEmail(studentEmail, subject, htmlContent);
    }
}
