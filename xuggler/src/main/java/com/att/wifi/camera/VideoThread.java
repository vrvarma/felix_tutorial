package com.att.wifi.camera;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

class VideoThread implements Runnable {

    // @Inject
    FileCache fileCache = FileCacheImpl.getInstance();

    private String hostName;
    private String tmpDir;

    public VideoThread(String hostName, String tmpDir) {

	this.hostName = hostName;
	this.tmpDir = tmpDir;
    }

    @Override
    public void run() {

	try {

	    generateFiles(hostName, tmpDir);
	} catch (FileNotFoundException e) {

	    e.printStackTrace();
	} catch (IOException e) {

	    e.printStackTrace();
	}
    }

    private void generateFiles(String ipAddress, String tmpDirectory)
	    throws IOException, FileNotFoundException {

	String urlString = "https://" + ipAddress + "/img/media.ts";

	URLConnection urlConnection = getConnection(urlString);
	ReadableByteChannel rbc = Channels.newChannel(urlConnection
		.getInputStream());

	while (true) {

	    fileCache.transferImage(ipAddress, tmpDirectory, rbc);
	}
    }

    private URLConnection getConnection(String urlString) throws IOException {

	URL website = new URL(urlString);
	URLConnection urlConnection = website.openConnection();
	if (urlConnection instanceof HttpsURLConnection) {

	    
	    System.out.println("trustStore="
		    + System.getProperty("javax.net.ssl.trustStore"));
	    System.out.println("trustStorePassword="
		    + System.getProperty("javax.net.ssl.trustStorePassword"));
	    System.out.println("trustStoreType="
		    + System.getProperty("javax.net.ssl.trustStoreType"));
	    System.out.println("keyStore="
		    + System.getProperty("javax.net.ssl.keyStore"));
	    System.out.println("keyStorePassword="
		    + System.getProperty("javax.net.ssl.keyStorePassword"));
	    System.out.println("keyStoreType="
		    + System.getProperty("javax.net.ssl.keyStoreType"));

	    HttpsURLConnection sslConnection = (HttpsURLConnection) urlConnection;
	    sslConnection.setSSLSocketFactory(HttpsURLConnection
		    .getDefaultSSLSocketFactory());
	}
	return urlConnection;
    }

    static {
	HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
	    public boolean verify(String hostname, SSLSession session) {
		// ip address of the service URL(like.23.28.244.244)
		return true;
	    }
	});
    }
}