package com.code_client.code_client.installers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class RubyInstaller {
	
	// Create the install button for ruby and attach a command to the click handler
	public static Button init() {
		Button rubyInstallButton = new Button();
        rubyInstallButton.setText("Install Ruby");
        
        // Click action
        rubyInstallButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	// As of right now there's no good way to prompt the user for sudo password
            	// without the gksu library on linux. When exporting this app for actual use, we
            	// will have to find a way to bundle this library.
            	List<String> command = Arrays.asList("/bin/bash", "-c", "gksudo -S \"apt-get --yes --force-yes install ruby-full\"");
                install(command);
            }
        });
        
        return rubyInstallButton;
	}

	// Use the command we passed in to the handler here and actually execute it
	public static void install(List<String> command) {	
		ProcessBuilder pb = new ProcessBuilder(command);
		
		try {			
			Process commandProcess = pb.start();
			int exitCode = commandProcess.waitFor();
			
			System.out.println("Ruby Installation exited with exit code: " + exitCode);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

}
