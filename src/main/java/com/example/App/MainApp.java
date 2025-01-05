package com.example.App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
   @Override
public void start(Stage stage) {
    try {
        Parent root = FXMLLoader.load(getClass().getResource("/views/login.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/styles/app.css").toExternalForm());
        
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
        // Stage.setTitle("School Resource Management");

    } catch(Exception e) {
        e.printStackTrace();
    }
}

}


