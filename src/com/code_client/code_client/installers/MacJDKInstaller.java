package code_client.code_client.installers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class MacJDKInstaller extends JDKInstaller {
	
	private static URL latestVersionLink;
	
	public MacJDKInstaller(/* no 32-bit for mac */) {
		isDownloadFinished = false;
		try {
			this.init();
		} catch(IOException e) {
			System.err.println("Error initiating JDKInstaller:");
			e.printStackTrace();
		}
	} 
	
	public static void init() throws IOException {
		latestVersionLink = latestJDKVersion("macosx-x64.dmg");
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
			   /*                mac               */
			if(response.contains("command not found")) {
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
		return "jdk-installer.dmg";
	}
}
