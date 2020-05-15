package com.oxygencobalt.sweepered;

import javafx.application.Application;
import javafx.stage.Stage;

import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.Group;

public class SweeperedApp extends Application {
    private Stage window;
    private static Scene scene;

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;

        window.setResizable(false);
        window.setTitle("Sweepered");

        Group root = new Group();
        Scene scene = new Scene(root, 400, 500);

        scene.setFill(Color.BLACK);

        window.setScene(scene);
        window.show();
    }

    public static void main(String[] args) {
        launch();
    }
}