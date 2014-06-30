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

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class VideoThread implements Runnable {

    private static final Logger LOGGER = LogManager
	    .getLogger(VideoThread.class);

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
	} catch (IOException e) {

	    LOGGER.error("Exception Thrown --> ", e);
	}

    }

    private void generateFiles(String ipAddress, String tmpDirectory)
	    throws IOException {

	String urlString = "http://" + ipAddress + "/img/media.ts";

	while (true) {
	    try {
		URLConnection urlConnection = getConnection(urlString);
		ReadableByteChannel rbc = Channels.newChannel(urlConnection
			.getInputStream());

		while (true) {

		    fileCache.transferImage(ipAddress, tmpDirectory, rbc);
		}

	    } catch (Exception e) {

		LOGGER.error("Exception Thrown --> ", e);
	    }
	}
    }

    private URLConnection getConnection(String urlString) throws IOException {

	URL website = new URL(urlString);
	URLConnection urlConnection = website.openConnection();
	urlConnection.setAllowUserInteraction(false);
	urlConnection.setRequestProperty("Connection", "Keep-Alive");

	// LOGGER.info("Expiration  " + urlConnection.getExpiration());

	if (urlConnection instanceof HttpsURLConnection) {

	    LOGGER.debug("trustStore="
		    + System.getProperty("javax.net.ssl.trustStore"));
	    LOGGER.debug("trustStorePassword="
		    + System.getProperty("javax.net.ssl.trustStorePassword"));
	    LOGGER.debug("trustStoreType="
		    + System.getProperty("javax.net.ssl.trustStoreType"));
	    LOGGER.debug("keyStore="
		    + System.getProperty("javax.net.ssl.keyStore"));
	    LOGGER.debug("keyStorePassword="
		    + System.getProperty("javax.net.ssl.keyStorePassword"));
	    LOGGER.debug("keyStoreType="
		    + System.getProperty("javax.net.ssl.keyStoreType"));

	    HttpsURLConnection sslConnection = (HttpsURLConnection) urlConnection;
	    sslConnection.setSSLSocketFactory(HttpsURLConnection
		    .getDefaultSSLSocketFactory());
	}
	urlConnection.connect();
	for (int j = 1;; j++) {
	    String header = urlConnection.getHeaderField(j);
	    if (header == null) {
		break;
	    }
	    LOGGER.info("Header " + urlConnection.getHeaderFieldKey(j) + " "
		    + header);
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