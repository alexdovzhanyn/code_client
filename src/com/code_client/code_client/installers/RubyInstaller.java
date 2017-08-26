package com.code_client.code_client.installers;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;
import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.layout.VBox;

/*	This Installer works both for Mac and for Linux
 *  but we have a separate way of doing it for Windows
 *  cause Microsoft is a special little snowflake :)
 * 
 * 	This installs RVM for the user, and from there we're able to easily
 *  manage ruby versions for them.
 */

public class RubyInstaller {
	
	// Create the install button for ruby and attach a command to the click handler
	public static Button init(VBox layout) {
		Button rubyInstallButton = new Button();
        rubyInstallButton.setText("Install Ruby");
        
        // Click action
        rubyInstallButton.setOnAction(e -> {
        	if(System.getProperty("os.name").toLowerCase().contains("windows")) {
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
	private static int unixInstall() {	
		List<String> command = Arrays.asList("/bin/bash", "-c", "\\curl -sSL https://get.rvm.io | bash -s stable");
		ProcessBuilder pb = new ProcessBuilder(command);
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
        String downloadUrl = getDownloadUrlForArchitecture();
        String[] fileName = downloadUrl.split("/");
        
       //Opens the file after completion
       if( download(downloadUrl, fileName[fileName.length - 1]) ) {
           Desktop.getDesktop().open(new File(System.getProperty("user.home")+"/Downloads/" + fileName[fileName.length - 1]));
       }

    }


    //Handles the Download for Windows
    public static boolean download(String fileURL, String fileName) {
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


    // Returns the correct download url for Windows based on which architecture is being used
    private static String getDownloadUrlForArchitecture() {
        // Checks for - 32bit or 64bit
        String arch = System.getenv("PROCESSOR_ARCHITECTURE");
        String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");

        String realArch = ( arch.endsWith("64") || wow64Arch != null && wow64Arch.endsWith("64") ) ? "64" : "86";

        String downloadUrl = "https://github.com/oneclick/rubyinstaller2/releases/download/2.4.1-2/rubyinstaller-2.4.1-2-x" + realArch + ".exe";

        return downloadUrl;
    }

}