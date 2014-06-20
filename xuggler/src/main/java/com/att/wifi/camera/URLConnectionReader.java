package com.att.wifi.camera;

public class URLConnectionReader {

    public static void main(String[] args) throws Exception {

	String tmpDirectory = args[0];

	for (int i = 1; i < args.length; i++) {

	    new Thread(new VideoThread(args[i], tmpDirectory)).start();
	}

    }

}
