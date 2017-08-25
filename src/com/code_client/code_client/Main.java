package com.code_client.code_client;

import java.util.Arrays;
import java.util.List;

import com.code_client.code_client.scenes.RubyInstallationLayout;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage clientWindow) {
        clientWindow.setTitle("Code Client");     
        VBox leftMenu = new VBox();
        
        BorderPane windowLayout = new BorderPane();
        windowLayout.setPadding(new Insets(10, 10, 10, 10));
        
        Button rubySceneToggle = new Button("Go to Ruby Installer");
        rubySceneToggle.setOnAction(e -> clientWindow.setScene(new Scene(RubyInstallationLayout.init(), 1280, 720)));
        
        leftMenu.getChildren().add(rubySceneToggle);
        
        windowLayout.setLeft(leftMenu);
        
        clientWindow.setScene(new Scene(windowLayout, 1280, 720));
        clientWindow.show();
    }
}
