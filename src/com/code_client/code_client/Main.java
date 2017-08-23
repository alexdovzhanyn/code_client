package code_client.code_client;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import code_client.code_client.installers.*;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
	
	JDKInstaller jdkInstaller; //pass the constructor "true" to install 32-bit
	
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage clientWindow) {
        StackPane root = new StackPane();
        
        clientWindow.setTitle("Code Client");
        Label javaMsgLabel = new Label("Press yes for administrative\npriveileges when prompted");
        javaMsgLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        javaMsgLabel.setTextAlignment(TextAlignment.CENTER);
        javaMsgLabel.setAlignment(Pos.TOP_CENTER);
        //TODO: change this ugly position^^^
        
        if(isJDKInstalled()) {
        	javaMsgLabel.setText(javaMsgLabel.getText() + System.lineSeparator() + "NOTE: jdk is already installed");
        }
        
        StackPane bitInstallPane = new StackPane();
        
        ToggleSwitch bitInstall = new ToggleSwitch();
        /*bitInstall.setSize(0.5, 0.5);
        bitInstall.setMaxHeight(0.5);
        bitInstall.setMaxWidth(0.5);*/
        
        bitInstallPane.getChildren().add(bitInstall);
        //bitInstallPane.setTranslateX(0.5);
        //bitInstallPane.setTranslateY(0.5);
        bitInstallPane.setMaxSize(100, 40);
        bitInstallPane.setMinSize(100, 40);
        
        Button javaInstallButton = new Button();
        javaInstallButton.setText("Install the JDK (for Java)");
        
        // Click action
        javaInstallButton.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
            	bitInstallPane.setVisible(false);
            	javaInstallButton.setVisible(false);
            	root.setAlignment(javaMsgLabel, Pos.CENTER);
            	javaMsgLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            	javaMsgLabel.setText("Press yes for administrative\npriveileges when prompted");
            	
            	try {
            		//TODO: if an error is thrown before the ui can update, the ui won't update with the error
            		jdkInstaller = new JDKInstaller(bitInstall.switchOnProperty().get());
					jdkInstaller.install();
				} catch (IOException e) {
					System.err.println("Error installing JDK:");
					e.printStackTrace();
					System.out.println("changing ui");
					bitInstallPane.setVisible(true);
					javaInstallButton.setVisible(true);
					javaMsgLabel.setText("Error installing:" + System.lineSeparator() + e.getLocalizedMessage().split(":")[1]);
					javaMsgLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
					root.setAlignment(javaMsgLabel, Pos.TOP_CENTER);
				}
            }
        });
        
        root.getChildren().add(javaMsgLabel);
        root.setAlignment(javaMsgLabel, Pos.TOP_CENTER);
        root.getChildren().add(bitInstallPane);
        root.setAlignment(bitInstallPane, Pos.BOTTOM_CENTER);
        root.getChildren().add(javaInstallButton);
        clientWindow.setScene(new Scene(root, 300, 250));
        clientWindow.show();
    }
    
    private class ToggleSwitch extends HBox {
    	
    	/* Thanks to TheItachiUchiha on GitHub */
    	/* This was modified from one of his gists */
    	
    	public boolean _64bit = false;
    	
    	private final Label label = new Label();
    	private final Button button = new Button();
    	
    	private SimpleBooleanProperty switchedOn = new SimpleBooleanProperty(false);
    	public SimpleBooleanProperty switchOnProperty() { return switchedOn; }
    	
    	public void setSize(double d, double e) {
    		this.setHeight(d);
    		this.setWidth(e);
    	}
    	
    	private void init() {
    		
    		label.setText("64 bit");
    		label.setFont(Font.font(label.getFont().getName(), FontWeight.EXTRA_BOLD, label.getFont().getSize()));
    		
    		getChildren().addAll(label, button);	
    		button.setOnAction((e) -> {
    			switchedOn.set(!switchedOn.get());
    		});
    		label.setOnMouseClicked((e) -> {
    			switchedOn.set(!switchedOn.get());
    			System.out.println(switchedOn.toString());
    		});
    		setStyle();
    		bindProperties();
    	}
    	
    	private void setStyle() {
    		//Default Width
    		setWidth(100);
    		label.setAlignment(Pos.CENTER);
    		setStyle("-fx-background-color: blue; -fx-text-fill:black; -fx-background-radius: 4;");
    		setAlignment(Pos.CENTER_LEFT);
    	}
    	
    	private void bindProperties() {
    		label.prefWidthProperty().bind(widthProperty().divide(2));
    		label.prefHeightProperty().bind(heightProperty());
    		button.prefWidthProperty().bind(widthProperty().divide(2));
    		button.prefHeightProperty().bind(heightProperty());
    	}
    	
    	public ToggleSwitch() {
    		init();
    		switchedOn.addListener((a,b,c) -> {
    			if (c) {
                    		label.setText("32 bit");
                    		this._64bit = false;
                    		setStyle("-fx-background-color: orange; -fx-background-radius: 4;");
                    		label.toFront();
                		}
                		else {
                			label.setText("64 bit");
                			this._64bit = true;
                			setStyle("-fx-background-color: blue; -fx-background-radius: 4;");
                    		button.toFront();
                		}
    		});
    	}
    }
    
    public boolean isJDKInstalled() {
    	//can return false if it's installed but not in PATH
    	try {
			Process process = Runtime.getRuntime().exec("javac");
			Scanner scanner = new Scanner(process.getInputStream()); //scanners are easy
			String response = "";
			while(scanner.hasNext()) { response += scanner.nextLine();	}
			scanner.close();
			   /*          	   linux               */    /*                               windows                               */    /*                mac               */
			if(response.contains("Command not found") || response.contains("is not recognized as an internal or external command") || response.contains("command not found")) {
				return false;
			}
			return true;
		} catch (IOException e) {
			System.out.println("can't test if jdk is installed:");
			e.printStackTrace();
			return false;
		}
    }
    
    public boolean isJREInstalled() {
    	//useful
    	return true;
    }
}
