package com.code_client.code_client.installers;


import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class NodejsInstaller {

    public static void init() {
        // setup
    }

    public static void install() {
        System.out.println("Install nodejs");

        Desktop d = Desktop.getDesktop();

        try {
            d.browse(new URI("https://nodejs.org/en/download/"));
        } catch(URISyntaxException e) {
            // catch exception
        } catch(IOException e) {
            // catch exception
        }
    }

}
