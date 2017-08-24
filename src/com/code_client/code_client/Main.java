package com.code_client.code_client;

import java.util.Arrays;
import java.util.List;

import com.code_client.code_client.installers.RubyInstaller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	RubyInstaller rubyInstaller = new RubyInstaller();
	
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage clientWindow) {
        clientWindow.setTitle("Code Client");     
        StackPane root = new StackPane();
        
        // Installer Buttons
        root.getChildren().add(rubyInstaller.init());
        
        clientWindow.setScene(new Scene(root, 300, 250));
        clientWindow.show();
    }
}
