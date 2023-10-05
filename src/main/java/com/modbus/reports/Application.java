package com.modbus.reports;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main-form-2.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1062, 477);
        stage.setTitle("Reports");
        stage.getIcons().add(new Image(Files.newInputStream(Paths.get("src/main/resources/images/connectOn.png"))));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}