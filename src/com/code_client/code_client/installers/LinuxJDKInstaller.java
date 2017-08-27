package com.code_client.code_client.installers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class LinuxJDKInstaller extends JDKInstaller {
	
	private static URL latestVersionLink;
	private static boolean _64Bit = true;
	private static boolean targz = true;
	private static Thread installThread;
	
	public LinuxJDKInstaller(boolean _32Bit, boolean targz) { //first param: true for 32 bit, false is 64 bit, second param: true to download tar.gz, false to download rpm
		_64Bit = !_32Bit;
		isDownloadFinished = false;
		this.targz = targz;
		try {
			this.start();
		} catch(IOException e) {
			System.err.println("Error initiating JDKInstaller:");
			e.printStackTrace();
		}
	} 
	
	private void start() throws IOException {
		if(targz) {
			if(_64Bit) {
				latestVersionLink = latestJDKVersion("linux-x64.tar.gz");
			} else {
				latestVersionLink = latestJDKVersion("linux-i586.tar.gz");
			}
		} else {
			if(_64Bit) {
				latestVersionLink = latestJDKVersion("linux-x64.rpm");
			} else {
				latestVersionLink = latestJDKVersion("linux-i586.rpm");
			}
		}
	}
	
	public void install() throws IOException {
			System.out.println("installing java!");
			
			String filename = getFileName();
			
			//saving said file
			//TODO: reminder - uncomment this in release, i don't wanna download it again every time i test
			InputStream in = JDKInstaller.getDownloadInputStream(latestVersionLink);
			Files.copy(in, new File(filename).toPath(), StandardCopyOption.REPLACE_EXISTING);
			System.out.println("installer saved to file: " + filename);
			System.out.println("saved file!");
			
			isDownloadFinished = true;
			Desktop.getDesktop().open(new File(filename));
	}    
    public boolean isJDKInstalled() {
    	//can return false if it's installed but not in PATH
    	try {
			Process process = Runtime.getRuntime().exec("javac");
			Scanner scanner = new Scanner(process.getInputStream()); //scanners are easy
			String response = "";
			while(scanner.hasNext()) { response += scanner.nextLine();	}
			scanner.close();
			   /*          	   linux               */
			if(response.contains("Command not found")) {
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

	@Override
	public String getFileName() {
		if(targz) {
			return _64Bit ? "jdk-installer-64bit.tar.gz" : "jdk-installer-64bit.tar.gz";
		} else {
			return _64Bit ? "jdk-installer-64bit.rpm" : "jdk-installer-32bit.rpm";
		}
	}

	public void start(Stage installerWindow) throws Exception {
		StackPane root = new StackPane();
        
        installerWindow.setTitle("Code Client");
        
        Label javaMsgLabel = new Label("Downloading: " + System.lineSeparator() + this.getFileName() + System.lineSeparator() + System.lineSeparator() + "Your detected OS: " + System.lineSeparator() + JDKInstaller.getSystemProperties());
        javaMsgLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        javaMsgLabel.setTextAlignment(TextAlignment.CENTER);
        javaMsgLabel.setAlignment(Pos.TOP_CENTER);
        //TODO: change this ugly position^^^
        
        //ProgressBar downloadProgress = new ProgressBar();
        //downloadProgress.setProgress(0.0f);
        //TODO: progress bars are better than saying the number of bytes downloaded
        
        if(this.isJDKInstalled()) {
        	javaMsgLabel.setText(javaMsgLabel.getText() + System.lineSeparator() + System.lineSeparator() + "NOTE: jdk is already installed");
        }
        
        Button javaInstallButton = new Button();
        javaInstallButton.setText("Install the JDK (for Java)");
        
        
        Runnable installRunnable = ()-> {
   			try {
           		//TODO: if an error is thrown before the ui can update, the ui won't update with the error
				this.install();
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
						
						while(isDownloadFinished) {
							try { Thread.currentThread().sleep(100); } catch(Exception e) { e.printStackTrace(); }

							long bytes = new File(getFileName()).length();
							
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
        installerWindow.setScene(new Scene(root, 300, 250));
        installerWindow.show();
	}
}
