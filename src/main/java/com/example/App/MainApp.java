package com.example.App;
import com.example.EmailServise.*;
import jakarta.mail.MessagingException;
// import javafx.application.Application;
// import javafx.fxml.FXMLLoader;
// import javafx.scene.Parent;
// import javafx.scene.Scene;
// import javafx.stage.Stage;

public class MainApp  {
    //@Override
    // public void start(Stage stage) {
    //     try {
    //         Parent root = FXMLLoader.load(getClass().getResource("/views/login.fxml"));
    //         Scene scene = new Scene(root);
    //         scene.getStylesheets().add(getClass().getResource("/styles/app.css").toExternalForm());
            
    //         stage.setMaximized(true);
    //         stage.setScene(scene);
    //         stage.show();
    //         // stage.setTitle("School Resource Management");
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

    public static void main(String[] args) {
        String senderEmail = "mohamedachtout09@gmail.com";  
        String senderPassword = "cqxdsgjqrpcfsqfe";  // Your Gmail app password
        
        try {
            email emailService = new email(senderEmail, senderPassword);
            
            String customerEmail = "achtoutmohamed08@gmail.com";
            String reservationDetails = """
                Reservation ID: 12345
                Date: 2025-01-20
                Time: 19:00
                Number of People: 4
                Special Requests: Window seat
                """;
            
            emailService.sendReservationConfirmation(customerEmail, reservationDetails);
            System.out.println("Confirmation email sent successfully!");
            
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
        // launch(args);
    }
       
}    



