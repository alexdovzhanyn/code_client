package com.code_client.code_client.installers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BasicInstaller {
	public static void init() {
		// setup here
	}

	public void install(List<String> command) {
		List<String> executedCommand = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("Please enter administrator password:");

		try {
			String sudo_password = br.readLine();
			
			List<String> prependCommand = Arrays.asList("echo", sudo_password, "|", "sudo",  "-S");
			
			executedCommand.addAll(prependCommand);
			
			executedCommand.addAll(command);
			
			System.out.println(executedCommand);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		ProcessBuilder pb = new ProcessBuilder(executedCommand);
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
