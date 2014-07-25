package com.att.wifi.camera;

import java.io.File;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class URLStreamer {

    private static final Logger LOGGER = LogManager
	    .getLogger(URLStreamer.class);

    public static void main(String[] args) {

	String tmpDirectory = args[0];

	startCleanupThread(tmpDirectory);

	for (int i = 1; i < args.length; i++) {

	    new Thread(new StreamingThread(args[i], tmpDirectory)).start();
	}

    }

    private static void startCleanupThread(String tmpDirectory) {

	new File(tmpDirectory).mkdir();
	Thread cleanupThread = new Thread(new FileCleanupThread(tmpDirectory));
	cleanupThread.setDaemon(true);
	cleanupThread.start();
    }

}