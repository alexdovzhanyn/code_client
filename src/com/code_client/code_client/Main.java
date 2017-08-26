package com.code_client.code_client;

import java.util.ArrayList;

import com.code_client.code_client.layouts.RubyInstallationLayout;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
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
        
        leftMenu.getChildren().addAll(leftMenuPaneActions(clientWindow));
        
        windowLayout.setLeft(leftMenu);
        
        clientWindow.setScene(new Scene(windowLayout, 1280, 720));
        clientWindow.show();
    }
    
    // Returns a list of buttons that need to be added into the Left Pane.
    private ArrayList<Button> leftMenuPaneActions(Stage window) {
    	Button rubySceneToggle = new Button("Go to Ruby Installer");
        rubySceneToggle.setOnAction(e -> window.setScene(new Scene(RubyInstallationLayout.display(), 1280, 720)));
        
        ArrayList<Button> actions = new ArrayList<Button>();
        actions.add( // Change to addAll when we merge other buttons in
        	rubySceneToggle
        );
        
        return actions;
    }
}
