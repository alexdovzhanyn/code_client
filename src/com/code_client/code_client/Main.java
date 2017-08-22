package com.code_client.code_client;

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
        Button rubyInstallButton = new Button();
        rubyInstallButton.setText("Install Ruby");
        
        // Click action
        rubyInstallButton.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                rubyInstaller.install();
            }
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(rubyInstallButton);
        clientWindow.setScene(new Scene(root, 300, 250));
        clientWindow.show();
    }
}
