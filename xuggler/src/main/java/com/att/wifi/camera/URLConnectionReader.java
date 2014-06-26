package com.att.wifi.camera;

import java.io.File;

public class URLConnectionReader {

    public static void main(String[] args) throws Exception {

	String tmpDirectory = args[0];

	try {
	    FileUtils.deleteDir(new File(tmpDirectory));
	} catch (Exception e) {
	    //Need to add a log
	}

	for (int i = 1; i < args.length; i++) {

	    new Thread(new VideoThread(args[i], tmpDirectory)).start();
	}

    }

}
