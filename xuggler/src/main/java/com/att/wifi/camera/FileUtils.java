package com.att.wifi.camera;

import java.io.File;
import java.io.IOException;

public final class FileUtils {

    private static final int MAX_NUMBER_INDEX = 3;
    private static final String POST_FIX = ".ts";

    public static File getFile(String directory, String fileName) {

	return new File(directory, fileName + POST_FIX);
    }

    public static File getTemperoryFile(String directory, String fileName) throws IOException {

	return File.createTempFile(fileName, POST_FIX, new File(directory));

    }

    public static void rollOver(String directory, String fileName) {

	File target;
	File file;

	boolean renameSucceeded = true;
	if (MAX_NUMBER_INDEX > 0) {

	    // Delete the oldest file, to keep Windows happy.
	    file = getFile(directory, fileName + "_" + MAX_NUMBER_INDEX);
	    if (file.exists()) {

		renameSucceeded = file.delete();
	    }

	    // Map {(MAX_NUMBER_INDEX - 1), ..., 2, 1} to {MAX_NUMBER_INDEX,
	    // ..., 3, 2}
	    for (int i = MAX_NUMBER_INDEX - 1; i >= 1 && renameSucceeded; i--) {

		file = getFile(directory, fileName + "_" + i);
		if (file.exists()) {

		    target = getFile(directory, fileName + "_" + (i + 1));
		    System.out.println("Renaming file " + file + " to "
			    + target);
		    renameSucceeded = file.renameTo(target);
		}
	    }

	    if (renameSucceeded) {

		// Rename fileName to fileName_1.ts
		target = getFile(directory, fileName + "_" + 1);
		file = getFile(directory, fileName);
		System.out.println("Renaming file " + file + " to " + target);
		renameSucceeded = file.renameTo(target);

	    }
	    System.out.println("Rename " + renameSucceeded);
	}
    }

}
