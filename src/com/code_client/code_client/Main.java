package com.code_client.code_client;

import java.util.Arrays;
import java.util.List;

import com.code_client.code_client.installers.BasicInstaller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	BasicInstaller rubyInstaller = new BasicInstaller();
	
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
            	List<String> command = Arrays.asList("echo", "installing ruby");;
                rubyInstaller.install(command);
            }
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(rubyInstallButton);
        clientWindow.setScene(new Scene(root, 300, 250));
        clientWindow.show();
    }
}
