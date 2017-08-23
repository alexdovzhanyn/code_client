package code_client.code_client.installers;

import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class JDKInstaller {
	
	private static URL latestVersionLink;
	private static boolean _64Bit = true;
	
	public JDKInstaller(boolean _32Bit) { //pass the constructor true for 32 bit
		_64Bit = !_32Bit;
		try {
			this.init();
		} catch(IOException e) {
			System.err.println("Error initiating JDKInstaller:");
			e.printStackTrace();
		}
	} 
	
	public static void init() throws IOException {
		if(_64Bit) {
			latestVersionLink = latestJDKVersion("windows-x64.exe");
		} else {
			latestVersionLink = latestJDKVersion("windows-i586.exe");
		}
	}
	
	public void install() throws IOException {
			System.out.println("installing java!");
			
			//this link redirects to another link
			HttpURLConnection urlconn = (HttpURLConnection) latestVersionLink.openConnection();
			urlconn.addRequestProperty("Cookie", "oraclelicense=accept-securebackup-cookie");
			urlconn.setInstanceFollowRedirects(true);
			urlconn.connect();
			System.out.println("Response code: " + urlconn.getResponseCode());
			System.out.println(urlconn.getResponseMessage());
			System.out.println(urlconn.getHeaderField("Location"));
			
			//and the link it redirects to redirects to yet another link
			URL redirectLink1 = new URL(urlconn.getHeaderField("Location"));
			System.out.println("going to first redirect link");
			HttpURLConnection urlconn2 = (HttpURLConnection) redirectLink1.openConnection();
			urlconn2.addRequestProperty("Cookie", "oraclelicense=accept-securebackup-cookie");
			urlconn2.setInstanceFollowRedirects(true);
			urlconn2.connect();
			System.out.println("Response code: " + urlconn2.getResponseCode());
			System.out.println(urlconn2.getResponseMessage());
			System.out.println(urlconn2.getHeaderField("Location"));
			
			
			//and finally, the actual download link
			URL redirectLink2 = new URL(urlconn2.getHeaderField("Location"));
			System.out.println("going to second redirect link");
			HttpURLConnection urlconn3 = (HttpURLConnection) redirectLink1.openConnection();
			urlconn3.addRequestProperty("Cookie", "oraclelicense=accept-securebackup-cookie");
			urlconn3.setInstanceFollowRedirects(true);
			urlconn3.connect();
			System.out.println("Response code: " + urlconn3.getResponseCode());
			System.out.println(urlconn3.getResponseMessage());
			System.out.println(urlconn3.getHeaderField("Location"));
			
			String filename = _64Bit ? "jdk-installer-64bit.exe" : "jdk-installer-32bit.exe";
			
			//saving said file
			//TODO: reminder - uncomment this in release, i don't wanna download it again every time i test
			InputStream in = redirectLink2.openStream();
			Files.copy(in, new File(filename).toPath(), StandardCopyOption.REPLACE_EXISTING);
			System.out.println("installer saved to file: " + filename);
			System.out.println("saved file!");
			
			Desktop.getDesktop().open(new File(filename));
	}
	
	public static URL latestJDKVersion(String platform) throws UnknownHostException, IOException {
		URL url = new URL("http://www.oracle.com/technetwork/java/javase/downloads/index.html");
		URLConnection urlConnection = url.openConnection();
		
		Scanner scanner = new Scanner(urlConnection.getInputStream());
		String webpage = "";
		while(scanner.hasNext()) {
			webpage += scanner.next();
		}
		scanner.close();
		//fragile, won't work if they change the layout of their page, but it's not like oracle to have modern websites :}
		String webpage_url = webpage.substring(webpage.indexOf("/technetwork/java/javase/downloads/jdk", webpage.indexOf("h3 align=\"center\">JDK</h3>")), webpage.indexOf("\"", webpage.indexOf("/technetwork/java/javase/downloads/jdk", webpage.indexOf("h3 align=\"center\">JDK</h3>"))));
		System.out.println(webpage_url);
		
		url = new URL("http://www.oracle.com" + webpage_url);
		urlConnection = url.openConnection();
		
		scanner = new Scanner(urlConnection.getInputStream());
		webpage = "";
		while(scanner.hasNext()) {
			webpage += scanner.next();
		}
		scanner.close();
		
		webpage_url = "shit";
		
		for(int i = webpage.indexOf(platform + "\""); i > 0; i--) {
			if(webpage.charAt(i) == '"') {
				webpage_url = webpage.substring(++i, webpage.indexOf(platform + "\"") + platform.length());
				break;
			}
		}
		
		System.out.println("url: " + webpage_url);
		
		return new URL(webpage_url);
	}
	
	public void saveURL(final String filename, final String urlString) throws IOException {
	    BufferedInputStream in = null;
	    FileOutputStream fout = null;
	    try {
	        in = new BufferedInputStream(new URL(urlString).openStream());
	        fout = new FileOutputStream(filename);

	        final byte data[] = new byte[1024];
	        int count;
	        while ((count = in.read(data, 0, 1024)) != -1) {
	            fout.write(data, 0, count);
	        }
	    } finally {
	        if (in != null) {
	            in.close();
	        }
	        if (fout != null) {
	            fout.close();
	        }
	    }
	}
}
