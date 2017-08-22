package com.code_client.code_client;

import com.code_client.code_client.installers.NodejsInstaller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class HelloWorld extends Application {



    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");

        Button nodejsInstallButton = new Button();
        nodejsInstallButton.setText("Install Node.js");

        nodejsInstallButton.setOnAction(event -> {
            NodejsInstaller.install();
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(nodejsInstallButton);

        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}
