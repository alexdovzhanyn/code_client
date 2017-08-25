package com.code_client.code_client.installers;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NodejsInstaller {

    private static final String WINDOWS_SUFFIX = ".msi";
    private static final String MACOS_SUFFIX = ".pkg";

    private static String currentSuffix = "";

    // Installs and opens the nodejs installer
    public static void install() {
        System.out.println("Install nodejs");

        // Get the os the user is running
        String os = System.getProperty("os.name").toLowerCase().replaceAll("\\s", "");
        // Remove the version number from windows: e.g. windows10 -> windows
        if (os.contains("windows")) {
            os = "windows";
        }

        // Get the bit type of the os <3 Stackoverflow
        String arch = System.getenv("PROCESSOR_ARCHITECTURE");
        String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");

        boolean is64Bit = arch != null && arch.endsWith("64")
                || wow64Arch != null && wow64Arch.endsWith("64")
                ? true : false;

        try {
            URL downloadURL = new URL(getInstallerLink(os, is64Bit));
            downloadFile(downloadURL, currentSuffix);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    // Downloads the file from the given url and stores it as "node" + given suffix
    private static void downloadFile(URL url, String suffix) {
        InputStream in = null;
        FileOutputStream fos = null;

        try {
            in = url.openStream();
            fos = new FileOutputStream(new File("node" + suffix));

            int length;
            byte[] buffer = new byte[1024];

            while ((length = in.read(buffer)) > -1) {
                fos.write(buffer, 0, length);
            }

            System.out.println("finished download");

            Desktop.getDesktop().open(new File("node" + suffix));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Returns the install link for the most recent (stable) version of nodejs
    private static String getInstallerLink(String os, boolean is64Bit) {
        String downloadLink = "";

        Scanner scanner = null;
        InputStream in = null;

        try {
            URL downloadPageUrl = new URL("https://nodejs.org/en/download/");

            in = downloadPageUrl.openStream();
            scanner = new Scanner(in);

            StringBuilder website = new StringBuilder();

            while(scanner.hasNext()) {
                website.append(scanner.next());
            }

            // Get the current version number - YOU DO NOT WANT TO DO THIS IN PRODUCTION, I HAVE TO FIX THIS
            String version = website.substring(website.indexOf("<strong>v") + 8, website.indexOf("</strong>", website.indexOf("<strong>v")));

            switch (os) {
                case "windows":
                    currentSuffix = WINDOWS_SUFFIX;

                    if (is64Bit) {
                        downloadLink = "https://nodejs.org/dist/" + version + "/node-" + version + "-x86" + WINDOWS_SUFFIX;
                    } else {
                        downloadLink = "https://nodejs.org/dist/" + version + "/node-" + version + "-x64" + WINDOWS_SUFFIX;
                    }

                    break;

                case "macosx":
                    currentSuffix = MACOS_SUFFIX;

                    downloadLink = "https://nodejs.org/dist/" + version + "/node-" + version + MACOS_SUFFIX;

                    break;

                default:

                    System.out.println("Unknown os, probably linux or a bug");

                    break;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }

            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return downloadLink;
    }




}
