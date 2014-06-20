package com.att.wifi.camera;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

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

	URL website = new URL("http://" + ipAddress + "/img/media.ts");
	ReadableByteChannel rbc = Channels.newChannel(website.openStream());

	while (true) {

	    fileCache.transferImage(ipAddress, tmpDirectory, rbc);
	}
    }

}