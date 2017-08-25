package com.code_client.code_client.installers;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;



public class RubyInstallerWindows extends Application  implements EventHandler<ActionEvent> {
Button button;
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception{

         button = new Button();
         button.setText("Install Ruby");

         button.setOnAction(this);
         StackPane layout = new StackPane();
         layout.getChildren().add(button);

         Scene scene = new Scene(layout,200,200);
         primaryStage.setScene(scene);
         primaryStage.show();

    }


    private static String[] fileName;
    private static boolean check;
    private static String fileN;

     public void handle(ActionEvent event) {

         if(event.getSource() == button)
         {
             try{
             System.out.println("Ruby Installing");

            //Checks the OS version and selects the URL

                String du = getOS();
                fileName = du.split("/");
                fileN = fileName[fileName.length-1];

                check = download(du); //runs the download process

                   if(check) {
                       open(fileN);
                       System.out.println("File Opening ");
                   }
                   else {
                       System.out.println("File can't be opened");
                   }



             } catch (Exception e) {
                 e.printStackTrace();
             }


         }


     }


     public static boolean download(String fileURL) {
         //Downloads the file
         //Saves the file in user home downloads directory

         try {
            String[] fileName = fileURL.split("/");

             URL website = new URL(fileURL);
             ReadableByteChannel rbc = Channels.newChannel(website.openStream());
             FileOutputStream fos = new FileOutputStream(System.getProperty("user.home")+"/Downloads/" + fileName[fileName.length-1]);
             fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

             fos.close();

             System.out.println("File Saved");

            return true;
         }catch (Exception ex) {
             System.out.println("File not Found");
             return false;
         }

     }

     public void open(String a) {
         try {
             Desktop.getDesktop().open(new File(System.getProperty("user.home")+"/Downloads/" + a));
         } catch (IOException e) {
             e.printStackTrace();
         }
     }

     public String getOS()
     {
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



