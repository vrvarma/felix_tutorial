package com.att.wifi.camera;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

class VideoThread implements Runnable {

    private static final int MAX_FILE_SIZE = 10 * 1024;

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

	    FileCache fileCache = FileCacheImpl.getInstance();
	    ImageFileDTO dto = fileCache.getFileCache(tmpDirectory, ipAddress);
	    FileChannel fc = dto.getFc();
	    long position = dto.getPosition();
	    position += fc.transferFrom(rbc, position, MAX_FILE_SIZE);
	    dto.setPosition(position);

	}
    }

}