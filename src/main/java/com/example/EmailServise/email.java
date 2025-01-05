package com.example.EmailServise;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
public class email {

    private final String username;
    private final String password;
    private final Properties prop;
    private final String displayName = "No Reply";
    private final String noReplyEmail = "noreply@yourdomain.com";

    public email(String username, String password) {
        this.username = username;  // Your actual Gmail address
        this.password = password;  // Your Gmail app password
        
        prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
    }

    public void sendReservationConfirmation(String toEmail, String reservationDetails) throws MessagingException {
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        
        // Set the "From" address with a display name
try {
    message.setFrom(new InternetAddress(username, displayName));
    // other code
} catch (UnsupportedEncodingException e) {
    e.printStackTrace(); // or handle it accordingly
}        
        // Set Reply-To header to no-reply address
        message.setReplyTo(new InternetAddress[]{new InternetAddress(noReplyEmail)});
        
        // Set the header to show no-reply address in email clients
        ((MimeMessage) message).setHeader("From", displayName + " <" + noReplyEmail + ">");
        
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Reservation Confirmation");

        String emailContent = String.format("""
            Dear Customer,
                        
            Thank you for your reservation. Your booking has been confirmed!
                        
            Reservation Details:
            %s
                        
            Please note that this is an automated message. Do not reply to this email.
                        
            Best regards,
            Your Service Team
            """, reservationDetails);

        message.setText(emailContent);
        Transport.send(message);
    }
 }
