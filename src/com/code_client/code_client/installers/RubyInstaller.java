package com.code_client.code_client.installers;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.layout.VBox;

/*	This Installer works both for Mac and for Linux
 *  but we have a separate way of doing it for Windows
 *  cause Microsoft is a special little snowflake :)
 * 
 * 	This installs RVM for the user, and from there we're able to easily
 *  manage ruby versions for them.
 *  
 *  NOTE: RVM is only available on UNIX based systems (A.K,A not Windows)
 */

public class RubyInstaller extends Installer implements InstallerInterface {
	
	// Create the install button for ruby and attach a command to the click handler
	public Button generateButton(VBox layout) {
		Button rubyInstallButton = new Button();
        rubyInstallButton.setText("Install Ruby");
        
        // Click action
        rubyInstallButton.setOnAction(e -> {
        	if(getOperatingSystem() == "Windows") {
                try {
                    wInstall();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            } else {
            	int installationExitCode = unixInstall();
               	
               	// Exit code of 0 usually means success
               	if (installationExitCode == 0) {
               		((Labeled) layout.lookup(".label")).setText("Ruby has successfully been installed!");
               	}
            }
        });
        
        return rubyInstallButton;
	}

	// Use the command we passed in to the handler here and actually execute it
	// This method contains the installation code for both Mac and Linux
	private int unixInstall() {
		ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", "\\curl -sSL https://get.rvm.io | bash -s stable --ruby");
		int exitCode = 0;
		
		try {			
			Process commandProcess = pb.start();
			exitCode = commandProcess.waitFor();
			
			System.out.println("Ruby Installation exited with exit code: " + exitCode);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
		return exitCode;
	}

	// This is the installation code for Windows
	private static void wInstall() throws IOException {
        String downloadUrl = "https://github.com/oneclick/rubyinstaller2/releases/download/2.4.1-2/rubyinstaller-2.4.1-2-x" + getSystemArchitecture() + ".exe";
        String[] fileName = downloadUrl.split("/");
        
       //Opens the file after completion
       if( download(downloadUrl, fileName[fileName.length - 1]) ) {
           Desktop.getDesktop().open(new File(System.getProperty("user.home")+"/Downloads/" + fileName[fileName.length - 1]));
       }

    }


    //Handles the Download for Windows
    private static boolean download(String fileURL, String fileName) {
        //Downloads the file and saves it in user home downloads directory
    	//TODO: We should figure out how to download it into a hidden directory and then delete the file when we're done with it
    	
        try {
            URL website = new URL(fileURL);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(System.getProperty("user.home")+"/Downloads/" + fileName);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            fos.close();

            System.out.println("File Saved");

            return true;
        } catch (Exception ex) {
            System.out.println("File not Found");
            return false;
        }
    }

	@Override
	public boolean canUseProgressBar() {
		return false;
	}

	// Get installed version of ruby (if any)
	@Override
	public String getLanguageVersion() {
		String version = "";
		ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", "ruby -v");
			
		try {			
			Process commandProcess = pb.start();
			commandProcess.waitFor();
				
			version = new BufferedReader(new InputStreamReader(commandProcess.getInputStream())).readLine();
			
			if (version.contains("not recognized") || version.contains("not installed") || version.contains("not found")) {
				return "None";
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
		return version;
	}
    
}