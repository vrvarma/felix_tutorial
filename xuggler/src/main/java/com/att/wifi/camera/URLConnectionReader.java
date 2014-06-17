package com.att.wifi.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class URLConnectionReader {
    private static final int TIMEOUT_MILLISECONDS = 30000;
    private static final int MB = 10 * 1024;
    private static final int MAX_NUMBER_INDEX = 3;
    private static String POST_FIX = ".ts";

    public static void main(String[] args) throws Exception {

	String ipAddress = args[0];
	String tmpDirectory = args[1];

	URL website = new URL("http://" + ipAddress + "/img/media.ts");
	ReadableByteChannel rbc = Channels.newChannel(website.openStream());
	generateFiles(rbc, ipAddress, tmpDirectory);

    }

    private static void generateFiles(ReadableByteChannel rbc,
	    String ipAddress, String tmpDirectory) throws IOException,
	    FileNotFoundException {

	long timeOut = calculateTimeout();
	long now = 0;
	FileChannel fc = null;
	long position = 0;
	int index = 0;
	String fileName = ipAddress;

	while (true) {

	    if (now == 0) {

		fc = getFileChannel(tmpDirectory, fileName);
	    }

	    if (isTimeOut(now, timeOut)) {

		position = 0;
		timeOut = calculateTimeout();
		fc.close();
		if (index > 0) {

		    rollOver(tmpDirectory, fileName);
		}

		fc = getFileChannel(tmpDirectory, fileName);

	    }
	    index++;
	    position += fc.transferFrom(rbc, position, MB);
	    now = System.currentTimeMillis();

	}
    }

    @SuppressWarnings("resource")
    private static FileChannel getFileChannel(String tmpDirectory,
	    String fileName) throws FileNotFoundException {

	File file = getFile(tmpDirectory, fileName, POST_FIX);
	FileChannel fc = new FileOutputStream(file).getChannel();
	return fc;
    }

    private static long calculateTimeout() {

	long timeOut = System.currentTimeMillis() + TIMEOUT_MILLISECONDS;
	return timeOut;
    }

    private static void rollOver(String directory, String fileName) {

	File target;
	File file;

	boolean renameSucceeded = true;
	if (MAX_NUMBER_INDEX > 0) {
	    // Delete the oldest file, to keep Windows happy.
	    file = getFile(directory, fileName + "_" + MAX_NUMBER_INDEX,
		    POST_FIX);
	    if (file.exists()) {

		renameSucceeded = file.delete();
	    }
	    // Map {(MAX_NUMBER_INDEX - 1), ..., 2, 1} to {MAX_NUMBER_INDEX,
	    // ..., 3,
	    // 2}
	    for (int i = MAX_NUMBER_INDEX - 1; i >= 1 && renameSucceeded; i--) {
		file = getFile(directory, fileName + "_" + i, POST_FIX);
		if (file.exists()) {
		    target = getFile(directory, fileName + "_" + (i + 1),
			    POST_FIX);
		    System.out.println("Renaming file " + file + " to "
			    + target);
		    renameSucceeded = file.renameTo(target);
		}
	    }

	    if (renameSucceeded) {

		// Rename fileName to fileName_1.ts
		target = getFile(directory, fileName + "_" + 1, POST_FIX);
		file = getFile(directory, fileName, POST_FIX);
		System.out.println("Renaming file " + file + " to " + target);
		renameSucceeded = file.renameTo(target);

	    }
	    System.out.println("Rename " + renameSucceeded);
	}
    }

    private static File getFile(String directory, String fileName,
	    String postFix) {
	return new File(directory, fileName + postFix);
    }

    private static boolean isTimeOut(long now, long timeOut) {

	System.err.println(now > timeOut);
	return now > timeOut;
    }

}