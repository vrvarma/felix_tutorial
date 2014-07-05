package com.att.wifi.camera;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class StreamingThread implements Runnable {

    private static final Logger LOGGER = LogManager
	    .getLogger(StreamingThread.class);

    // @Inject
    private static FileCache fileCache = FileCacheImpl.getInstance();

    private String hostName;
    private String tmpDir;

    public StreamingThread(String hostName, String tmpDir) {

	this.hostName = hostName;
	this.tmpDir = tmpDir;

    }

    @Override
    public void run() {
	try {
	    generateFiles();
	} catch (Exception e) {

	    LOGGER.error("Exception Thrown --> ", e);
	}
    }

    private void generateFiles() throws Exception {

	while (true) {

	    CloseableHttpClient httpclient = getHttpClient();

	    try {
		HttpGet httpget = new HttpGet("https://" + hostName
			+ "/img/media.ts?channel=2");

		LOGGER.debug("Executing request " + httpget.getRequestLine());
		CloseableHttpResponse response = httpclient.execute(httpget);
		try {

		    fileCache.transferImage(hostName, tmpDir, response
			    .getEntity().getContent());

		} finally {
		    response.close();
		}
	    } catch (Exception e) {

		LOGGER.error("Exception Thrown --> ", e);
	    } finally {
		httpclient.close();
	    }

	}
    }

    private CloseableHttpClient getHttpClient()
	    throws NoSuchAlgorithmException, KeyManagementException {

	String[] loginInfo = System.getProperty("camera.login").split(":");

	CredentialsProvider credsProvider = new BasicCredentialsProvider();
	credsProvider.setCredentials(new AuthScope(AuthScope.ANY_HOST,
		AuthScope.ANY_PORT), new UsernamePasswordCredentials(
		loginInfo[0], loginInfo[1]));

	SSLContext sslContext = SSLContexts.custom().useTLS()
	// .loadTrustMaterial(ks)
		.build();

	CloseableHttpClient httpclient = HttpClients.custom()
		.setSslcontext(sslContext)
		.setDefaultCredentialsProvider(credsProvider)
		.setHostnameVerifier(new AllowAllHostnameVerifier()).build();
	return httpclient;
    }
}
