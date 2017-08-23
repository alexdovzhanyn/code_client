package com.code_client.code_client.installers;


import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class NodejsInstaller {

    public static void init() {
        // setup
    }

    public static void install() {
        System.out.println("Install nodejs");

        // Get the os the user is running
        String os = System.getProperty("os.name").toLowerCase().replaceAll("\\s", "");

        // Depending on the os there are different ways to install node
        switch (os) {
            case "macosx":
                try {
                    downloadFile(new URL("https://nodejs.org/dist/v6.11.2/node-v6.11.2.pkg"), "node.pkg");
                } catch (MalformedURLException e) {
                    // Fix exception handling
                }

                break;

            case "windows":
                try {
                    downloadFile(new URL("https://nodejs.org/dist/v6.11.2/node-v6.11.2-x86.msi"), "node.msi");
                } catch (MalformedURLException e) {
                    // Fix exception handling
                }

                break;

            default:
                System.out.println("Unknown os");
                break;


        }




    }

    private static void downloadFile(URL url, String fileName) {
        try {
            InputStream in = url.openStream();
            FileOutputStream fos = new FileOutputStream(new File(fileName));

            int length = -1;
            byte[] buffer = new byte[1024];

            while ((length = in.read(buffer)) > -1) {
                fos.write(buffer, 0, length);
            }

            System.out.println("finished download");

            fos.close();
            in.close();

            Desktop.getDesktop().open(new File(fileName));
        } catch (IOException e) {
            // Fix exception handling
        }
    }




}
