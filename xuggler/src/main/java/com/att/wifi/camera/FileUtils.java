package com.att.wifi.camera;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class FileUtils {

    private static final int MAX_NUMBER_INDEX = 3;
    private static final String POST_FIX = ".ts";

    public static File getFile(String directory, String fileName) {

	return new File(directory, fileName + POST_FIX);
    }

    public static File getTemperoryFile(String directory, String fileName)
	    throws IOException {

	File file = File
		.createTempFile(fileName, POST_FIX, new File(directory));
	//file.deleteOnExit();
	return file;

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

    // public static void removeRecursive(Path path) throws IOException {
    //
    // Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
    // @Override
    // public FileVisitResult visitFile(Path file,
    // BasicFileAttributes attrs) throws IOException {
    // Files.delete(file);
    // return FileVisitResult.CONTINUE;
    // }
    //
    // @Override
    // public FileVisitResult visitFileFailed(Path file, IOException exc)
    // throws IOException {
    //
    // // try to delete the file anyway, even if its attributes
    // // could not be read, since delete-only access is
    // // theoretically possible
    // Files.delete(file);
    // return FileVisitResult.CONTINUE;
    // }
    //
    // @Override
    // public FileVisitResult postVisitDirectory(Path dir, IOException exc)
    // throws IOException {
    //
    // if (exc == null) {
    // Files.delete(dir);
    // return FileVisitResult.CONTINUE;
    // } else {
    // // directory iteration failed; propagate exception
    // throw exc;
    // }
    // }
    // });
    //
    // }
    
    public static boolean deleteDir(File dir) throws IOException {

	if (dir.isDirectory()) {
	    String[] children = dir.list();
	    for (int i = 0; i < children.length; i++) {
		boolean success = deleteDir(new File(dir, children[i]));
		if (!success) {
		    return false;
		}
	    }
	}
	return Files.deleteIfExists(dir.toPath());

    }

}
