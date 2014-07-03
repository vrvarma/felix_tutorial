package com.att.wifi.camera;

import java.io.File;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class URLStreamer {

    private static final Logger LOGGER = LogManager.getLogger(URLStreamer.class);

    public static void main(String[] args) {

	String tmpDirectory = args[0];

	try {
	    FileUtils.deleteDir(new File(tmpDirectory));
	} catch (Exception e) {
	    LOGGER.error("Exception deleting the directory");
	}

	for (int i = 1; i < args.length; i++) {

	    new Thread(new StreamingThread(args[i], tmpDirectory)).start();
	}

    }

}