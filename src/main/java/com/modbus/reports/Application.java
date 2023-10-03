package com.modbus.reports;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main-form-2.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1062, 477);
        stage.setTitle("Reports");
        stage.getIcons().add(new Image(new FileInputStream("src/main/resources/images/connectOn.png")));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}