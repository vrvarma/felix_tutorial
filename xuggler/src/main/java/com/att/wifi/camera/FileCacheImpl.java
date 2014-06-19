package com.att.wifi.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

public class FileCacheImpl implements FileCache {

    private static FileCache instance = new FileCacheImpl();

    private static final int MAX_NUMBER_INDEX = 3;

    private FileCacheImpl() {
    }

    public static FileCache getInstance() {

	return instance;
    }

    private static Map<String, ImageFileDTO> fileDtoCache = new HashMap<String, ImageFileDTO>();

    private static String POST_FIX = ".ts";

    @Override
    @SuppressWarnings("resource")
    public ImageFileDTO getFileCache(String tmpDirectory, String fileName)
	    throws FileNotFoundException, IOException {

	ImageFileDTO dto = fileDtoCache.get(fileName);
	if (dto == null) {

	    dto = new ImageFileDTO();
	    File file = getFile(tmpDirectory, fileName);
	    FileChannel fc = new FileOutputStream(file).getChannel();
	    dto.setFc(fc);
	    fileDtoCache.put(fileName, dto);

	} else {

	    if (dto.isTimeOut()) {

		dto.getFc().close();
		rollOver(tmpDirectory, fileName);
		dto = new ImageFileDTO();
		File file = getFile(tmpDirectory, fileName);
		FileChannel fc = new FileOutputStream(file).getChannel();
		dto.setFc(fc);
		fileDtoCache.remove(fileName);
		fileDtoCache.put(fileName, dto);
	    }
	}

	return dto;

    }

    private static File getFile(String directory, String fileName) {

	return new File(directory, fileName + POST_FIX);
    }

    private void rollOver(String directory, String fileName) {

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
