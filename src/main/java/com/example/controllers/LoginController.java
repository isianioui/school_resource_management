package com.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.stage.Screen;

import com.example.models.User;

import java.io.IOException;
import java.sql.Connection;

import com.example.dao.UserDAO;
import com.example.database.databaseConnection;

public class LoginController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    @FXML
public void handleLogin(ActionEvent event) {
    String email = emailField.getText();
    String password = passwordField.getText();

    Connection conn = databaseConnection.getConnection();
    UserDAO userDAO = new UserDAO();
    User user = userDAO.loginUser(email, password);

    if (user != null) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/dashboard.fxml"));
            Parent root = loader.load();

            dashboardController dashboardController = loader.getController();
            dashboardController.setUser(user);
            dashboardController.setUserRole(user.getRole());  // Add this line

            Scene scene = new Scene(root, Screen.getPrimary().getVisualBounds().getWidth(),
                    Screen.getPrimary().getVisualBounds().getHeight());
            scene.getStylesheets().add(getClass().getResource("/styles/dashboard.css").toExternalForm());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setMaximized(true);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            errorLabel.setText("Error loading dashboard: " + e.getMessage());
        }
    }
}



    public void goToRegister(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/registration.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, Screen.getPrimary().getVisualBounds().getWidth(),
                Screen.getPrimary().getVisualBounds().getHeight());
        scene.getStylesheets().add(getClass().getResource("/styles/app.css").toExternalForm());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }
    private String userRole;
    @FXML
    private Button loginButton;
    private User loggedInUser;  // Add this at class level


}
