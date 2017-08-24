package code_client.code_client.installers;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Scanner;

public abstract class JDKInstaller {
	
	public abstract boolean isJDKInstalled();
	public abstract void install() throws IOException;
	public abstract String getFileName();
	protected static boolean isDownloadFinished = false;
	
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
	
	public static InputStream getDownloadInputStream(URL url) throws IOException {
		
		//this link redirects to another link
		HttpURLConnection urlconn = (HttpURLConnection) url.openConnection();
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
		System.out.println("CONTENT LENGTH: " + urlconn3.getContentLength());
		
		return redirectLink2.openStream();
	}
	
	public boolean isFinishedDownloading() {
		return isDownloadFinished;
	}
	
	public JDKInstaller getJDKInstaller() {
		return null;
	}
	
	public static JDKInstaller detemineBestInstaller() {
		//determines real processor architecture
		String arch = System.getenv("PROCESSOR_ARCHITECTURE");
		String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");

		String realArch = arch.endsWith("64") || wow64Arch != null && wow64Arch.endsWith("64") ? "64" : "32"; //thanks SO
		
		String os = System.getProperty("os.name").toLowerCase();
    	
    	//just initiate
    	if(os.contains("windows")) {
			return new WindowsJDKInstaller(realArch.equals("32"));
		} else if(os.contains("linux")) {
			//check for rpm
			try {
				Process process = Runtime.getRuntime().exec("rpm");
				Scanner scanner = new Scanner(process.getInputStream()); //scanners are easy
				String response = "";
				while(scanner.hasNext()) { response += scanner.nextLine(); }
				scanner.close();
				return new LinuxJDKInstaller(realArch.equals("32"), response.contains("not found"));
			} catch (IOException e) {
				System.out.println("can't test if jdk is installed:");
				e.printStackTrace();
				return new LinuxJDKInstaller(realArch.equals("32"), true);
			}
		} else if(os.contains("mac") || os.contains("osx")) { //idk what mac returns, i can checks 404response's code, but im too lazy
			return new MacJDKInstaller();
		} else {
			return null;
		}
	}
	
	public static String getSystemProperties() {
		//determines real processor architecture
				String arch = System.getenv("PROCESSOR_ARCHITECTURE");
				String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");

				String realArch = arch.endsWith("64") || wow64Arch != null && wow64Arch.endsWith("64") ? "64" : "32"; //thanks SO
				
				String os = System.getProperty("os.name").toLowerCase();
		    	
		    	//just initiate
		    	if(os.contains("windows")) {
					return realArch.equals("32") ? "32 bit Windows" : "64 bit Windows";
				} else if(os.contains("linux")) {
					//check for rpm
					try {
						Process process = Runtime.getRuntime().exec("rpm");
						Scanner scanner = new Scanner(process.getInputStream()); //scanners are easy
						String response = "";
						while(scanner.hasNext()) { response += scanner.nextLine(); }
						scanner.close();
						if(response.contains("not found")) {
							return realArch.equals("32") ? "32 bit Linux without rpm" : "64 bit Linux without rpm";
						}
						return realArch.equals("32") ? "32 bit Linux with rpm" : "64 bit Linux with rpm";
					} catch (IOException e) {
						System.out.println("can't test if jdk is installed:");
						e.printStackTrace();
						return realArch.equals("32") ? "32 bit Linux" : "64 bit Linux";
					}
				} else if(os.contains("mac") || os.contains("osx")) { //idk what mac returns, i can checks 404response's code, but im too lazy
					return "Mac OSX";
				} else {
					return null;
				}
	}
}
