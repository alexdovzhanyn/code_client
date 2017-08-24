package code_client.code_client.installers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

import code_client.code_client.Main;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class WindowsJDKInstaller extends JDKInstaller {
	
	private static URL latestVersionLink;
	private static boolean _64Bit = true;
	
	public WindowsJDKInstaller(boolean _32Bit) { //pass the constructor true for 32 bit
		_64Bit = !_32Bit;
		isDownloadFinished = false;
		try {
			this.init();
		} catch(IOException e) {
			System.err.println("Error initiating JDKInstaller:");
			e.printStackTrace();
		}
	} 
	
	public static StackPane init() throws IOException {
		if(_64Bit) {
			latestVersionLink = latestJDKVersion("windows-x64.exe");
		} else {
			latestVersionLink = latestJDKVersion("windows-i586.exe");
		}
		
		StackPane sp = new StackPane();
		return sp;
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
			   /*                               windows                               */
			if(response.contains("is not recognized as an internal or external command")) {
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
    
    public String getFileName() {
		return _64Bit ? "jdk-installer-64bit.exe" : "jdk-installer-32bit.exe";
    }
    
}
