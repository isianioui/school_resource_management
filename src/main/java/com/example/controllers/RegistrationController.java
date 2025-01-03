package com.example.controllers;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import com.example.models.User;

import java.io.IOException;

import com.example.dao.UserDAO;

public class RegistrationController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;

    public void handleRegister(ActionEvent event) {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match!");
            return;
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setRole("user");  // Set the default role here (can be adjusted based on your needs)


        UserDAO userDAO = new UserDAO();
        if (userDAO.registerUser(newUser)) {
            errorLabel.setText("✅ Registration successful!");
        } else {
            errorLabel.setText("❌ Registration failed!");
        }
    }

   public void goToLogin(ActionEvent event) throws IOException {
    // Load the login form (FXML)
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
    VBox loginView = loader.load();  // Assuming the login screen uses VBox as the root

    // Get the current stage (window)
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

    // Set the new scene (login screen)
    Scene loginScene = new Scene(loginView);
    stage.setScene(loginScene);
}

}

