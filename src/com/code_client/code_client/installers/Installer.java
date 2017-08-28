package com.code_client.code_client.installers;

/* 
 * This is our main Installer class, every installer should inherit from here
 */

public class Installer {

	public static String getOperatingSystem() {
		String rawOS = System.getProperty("os.name").toLowerCase();
		String operatingSystem = "";
		
		if (rawOS.contains("windows")) {
			operatingSystem = "Windows";
		} else if (rawOS.contains("mac")) {
			operatingSystem = "OSX";
		} else if (rawOS.contains("nix") || rawOS.contains("nux") || rawOS.contains("aix")) {
			operatingSystem = "Linux";
		} else if (rawOS.contains("sunos")) {
			operatingSystem = "Solaris";
		} else {
			try {
				throw new Exception("Unsupported Operating System!");
			} catch (Exception e) {
				// If the operating system is unsupported, just let it crash
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		return operatingSystem;
	}
	
	// Returns 32 or 64 depending on system architecture
	public static int getSystemArchitecture() {
		boolean is64bit = false;
		if (System.getProperty("os.name").contains("Windows")) {
		    is64bit = (System.getenv("ProgramFiles(x86)") != null);
		} else {
		    is64bit = (System.getProperty("os.arch").indexOf("64") != -1);
		}
		
		return is64bit ? 64 : 86;
	}
}
