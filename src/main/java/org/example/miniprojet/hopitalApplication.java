package org.example.miniprojet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

public class hopitalApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(hopitalApplication.class.getResource("hopital.fxml"));

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/miniprojet/Views/Login.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1000, 800);
/*
        Group root = new Group();
        Scene scene = new Scene(root, Color.BLACK);
*/
        Image icon = new Image("icon.jpg");
        stage.getIcons().add(icon);
        stage.setTitle("Hospital Management System");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }

}