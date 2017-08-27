package com.code_client.code_client;

import java.io.IOException;

import com.code_client.code_client.installers.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Main extends Application {
	
	static JDKInstaller jdkInstaller;
	static String os;
	static long progress = 0;
	public static boolean downloadFinished = false;
	static Label javaMsgLabel;
	static Thread installThread;
	public static String[] args;
	
    public static void main(String[] args) {
    	
    	System.out.println(java.util.Arrays.toString(args));
    	
    	System.out.println(System.getProperty("sun.arch.data.model"));
    	
    	os = System.getProperty("os.name").toLowerCase();

    	jdkInstaller = JDKInstaller.detemineBestInstaller();
    	//Application.launch(jdkInstaller.getClass(), new String[] {});
    	//jdkInstaller.launch(args);
    	
    	launch(args);
    }
    
    @Override
    public void start(Stage clientWindow) {
    	
    	clientWindow.show(); //just for testing purposes
    	
    	jdkInstaller.display(jdkInstaller);
    	
    }
}