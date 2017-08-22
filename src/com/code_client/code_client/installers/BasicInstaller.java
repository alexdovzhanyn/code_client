package com.code_client.code_client.installers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class BasicInstaller {
	public static void init() {
		// setup here
	}

	public void install(List<String> command) {
		ProcessBuilder pb = new ProcessBuilder(command);
		Process commandProcess;
		
		try {
			commandProcess = pb.start();
			int errCode = commandProcess.waitFor();
			
			System.out.println("Command Output:\n" + output(commandProcess.getInputStream()));
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static String output(InputStream inputStream) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new InputStreamReader(inputStream));
		    String line = null;
		    
		    while ((line = br.readLine()) != null) {
		    	sb.append(line + System.getProperty("line.separator"));
		    }
		} finally {
			br.close();
		}
		
		return sb.toString();
	}

}
