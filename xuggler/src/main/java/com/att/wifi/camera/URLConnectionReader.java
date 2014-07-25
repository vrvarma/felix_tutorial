package com.att.wifi.camera;

import java.io.File;

public class URLConnectionReader {

    public static void main(String[] args) throws Exception {

	String tmpDirectory = args[0];

	startCleanupThread(tmpDirectory);

	for (int i = 1; i < args.length; i++) {

	    new Thread(new VideoThread(args[i], tmpDirectory)).start();
	}

    }

    private static void startCleanupThread(String tmpDirectory) {
	
	new File(tmpDirectory).mkdir();
	Thread cleanupThread = new Thread(new FileCleanupThread(tmpDirectory));
	cleanupThread.setDaemon(true);
	cleanupThread.start();
    }

}
