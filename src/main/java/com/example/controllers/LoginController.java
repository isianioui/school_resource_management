package com.example.controllers;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import com.example.models.User;

import java.io.IOException;

import org.mindrot.jbcrypt.BCrypt;

import com.example.dao.UserDAO;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    public void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        // Fetch the stored user from the database by email
        UserDAO userDAO = new UserDAO();
        User user = userDAO.loginUser(email, password);  // Fetch user and check password

        if (user != null) {
            // Login successful
            errorLabel.setText("✅ Login successful!");
            // Proceed with the login (e.g., open the main application window)
        } else {
            // Login failed (user not found or incorrect password)
            errorLabel.setText("❌ Incorrect email or password!");
        }
    }
  
 public void goToRegister(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/registration.fxml"));
        VBox registrationView = loader.load();  // Ensure it's a VBox
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();  // Get current stage
        Scene registrationScene = new Scene(registrationView);  // Set the new scene
        stage.setScene(registrationScene);
    }
}
     


