package com.kamil;

/**
 * Created by DELL on 2020-07-18.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("/fxml/weather.fxml"));
            primaryStage.setTitle("WeatherApplication");
            primaryStage.getIcons().add(new Image("/images/01d.png"));
            primaryStage.setScene(new Scene(root, 1270, 700));
            primaryStage.getScene().getStylesheets().addAll(getClass().getResource("/styles/style.css").toExternalForm());
            primaryStage.show();
            primaryStage.setResizable(false);
            primaryStage.sizeToScene();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}

