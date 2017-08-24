package code_client.code_client;

import java.io.File;
import java.io.IOException;
import code_client.code_client.installers.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Main extends Application {
	
	static JDKInstaller jdkInstaller;
	static String os;
	static long progress = 0;
	public static boolean downloadFinished = false;
	static Label javaMsgLabel;
	static Thread installThread;
	
    public static void main(String[] args) {
    	
    	System.out.println(System.getProperty("sun.arch.data.model"));
    	
    	jdkInstaller = JDKInstaller.detemineBestInstaller();
    	
    	os = System.getProperty("os.name").toLowerCase();
    	
        launch(args);
    }
    
    @Override
    public void start(Stage clientWindow) {
        StackPane root = new StackPane();
        
        clientWindow.setTitle("Code Client");
        
        javaMsgLabel = new Label("Downloading: " + System.lineSeparator() + jdkInstaller.getFileName() + System.lineSeparator() + System.lineSeparator() + "Your detected OS: " + System.lineSeparator() + JDKInstaller.getSystemProperties());
        javaMsgLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        javaMsgLabel.setTextAlignment(TextAlignment.CENTER);
        javaMsgLabel.setAlignment(Pos.TOP_CENTER);
        //TODO: change this ugly position^^^
        
        //ProgressBar downloadProgress = new ProgressBar();
        //downloadProgress.setProgress(0.0f);
        //TODO: progress bars are better than saying the number of bytes downloaded
        
        if(jdkInstaller.isJDKInstalled()) {
        	javaMsgLabel.setText(javaMsgLabel.getText() + System.lineSeparator() + System.lineSeparator() + "NOTE: jdk is already installed");
        }
        
        Button javaInstallButton = new Button();
        javaInstallButton.setText("Install the JDK (for Java)");
        
        
        Runnable installRunnable = ()-> {
   			try {
           		//TODO: if an error is thrown before the ui can update, the ui won't update with the error
				jdkInstaller.install();
   			} catch (IOException e) {
				System.err.println("Error installing JDK:");
				e.printStackTrace();
				System.out.println("changing ui");
				javaInstallButton.setVisible(true);
				javaMsgLabel.setText("Error installing:" + System.lineSeparator() + e.getLocalizedMessage().split(":")[1]);
				javaMsgLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
				root.setAlignment(javaMsgLabel, Pos.TOP_CENTER);
			}
   		};
        
        // Click action
        javaInstallButton.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
            	javaInstallButton.setVisible(false);
            	root.setAlignment(javaMsgLabel, Pos.CENTER);
            	javaMsgLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            	javaMsgLabel.setText("Press yes for administrative\npriveileges if prompted");
            	
            	installThread = new Thread(installRunnable);
           		installThread.start();
           		new Thread(new Runnable() {
					@Override
					public void run() {
						
						while(!jdkInstaller.isFinishedDownloading()) {
							try { Thread.currentThread().sleep(100); } catch(Exception e) { e.printStackTrace(); }

							long bytes = new File(jdkInstaller.getFileName()).length();
							
							System.out.println("updating ui in separate thread");
							Platform.runLater(new Runnable() {
								@Override						
								public void run() {
								  	   javaMsgLabel.setText(javaMsgLabel.getText().split(System.lineSeparator() + "Bytes downloaded:")[0] + System.lineSeparator() + "Bytes downloaded: " + System.lineSeparator() + bytes);
								}
							});
						}
						Platform.runLater(new Runnable() {
							@Override						
							public void run() {
								javaMsgLabel.setText(javaMsgLabel.getText().split(System.lineSeparator() + "Bytes downloaded:")[0] + System.lineSeparator() + "Finished downloading!");
							}
						});
					}
           		}).start();;
        }
    });
        
        root.getChildren().add(javaMsgLabel);
        root.setAlignment(javaMsgLabel, Pos.TOP_CENTER);
        javaInstallButton.setLayoutY(100);
        javaInstallButton.setTranslateY(100);
        root.getChildren().add(javaInstallButton);
        clientWindow.setScene(new Scene(root, 300, 250));
        clientWindow.show();
    }
}