package com.code_client.code_client.installers;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

        String os = System.getProperty("os.name").toLowerCase();
        System.out.println(os);

        if(os.equals("windows 10") || os.equals("windows 8") || os.equals("windows 8.1") || os.equals("windows 7") || os.equals("Windows XP"))
        {

            try {
                wInstall();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        rubyInstallButton.setOnAction(e -> {
           	List<String> command = Arrays.asList("/bin/bash", "-c", "\\curl -sSL https://get.rvm.io | bash -s stable");
           	int installationExitCode = install(command);
           	
           	// Exit code of 0 usually means success
           	if (installationExitCode == 0) {
           		((Labeled) layout.lookup(".label")).setText("Ruby has successfully been installed!");
           	}
        });
        
        return rubyInstallButton;
	}

	// Use the command we passed in to the handler here and actually execute it
	private static int install(List<String> command) {	
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

	private static void wInstall() throws IOException {
        String du = getOS();
        String[] fileName = du.split("/");

        boolean check = download(du,fileName[fileName.length-1]);

       //Opens the file after completion
       if(check)
       {
           Desktop.getDesktop().open(new File(System.getProperty("user.home")+"/Downloads/" + fileName[fileName.length-1]));
       }

    }


    //Handles the Download

    public static boolean download(String fileURL,String fileName) {
        //Downloads the file
        //Saves the file in user home downloads directory

        try {


            URL website = new URL(fileURL);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(System.getProperty("user.home")+"/Downloads/" + fileName);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            fos.close();

            System.out.println("File Saved");

            return true;
        }catch (Exception ex) {
            System.out.println("File not Found");
            return false;
        }

    }


    private static String getOS()
    {
        //Checks for - 32bit or 64bit

        String arch = System.getenv("PROCESSOR_ARCHITECTURE");
        String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");

        String du = ""; //DU means - download URL

        String realArch = arch.endsWith("64")
                || wow64Arch != null && wow64Arch.endsWith("64")
                ? "64" : "32";

        System.out.println(realArch);
        if(realArch.equals("32")){
            du = "https://github.com/oneclick/rubyinstaller2/releases/download/2.4.1-2/rubyinstaller-2.4.1-2-x86.exe";
        }
        else if(realArch.equals("64")) {
            du = "https://github.com/oneclick/rubyinstaller2/releases/download/2.4.1-2/rubyinstaller-2.4.1-2-x64.exe";
        }

        return du;
    }



}
