package com.att.wifi.camera;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.Map;

public class FileCacheImpl implements FileCache {

    private static final int MAX_FILE_SIZE = 10 * 1024;

    private static Map<String, ImageFileDTO> fileChannelMap = new HashMap<String, ImageFileDTO>();

//    ListMultimap<String, ImageFileDTO> imageFileCache = ArrayListMultimap
//     .create();
//    // Change this to a MultiMap

    private static FileCache instance = new FileCacheImpl();

    private FileCacheImpl() {
    }

    public static FileCache getInstance() {

	return instance;
    }

    @Override
    public void transferImage(String fileName, String tmpDirectory,
	    ReadableByteChannel rbc) throws IOException {

	ImageFileDTO dto = getFileCache(tmpDirectory, fileName);
	FileChannel fc = dto.getFc();
	long position = dto.getPosition();
	position += fc.transferFrom(rbc, position, MAX_FILE_SIZE);
	dto.setPosition(position);

    }

    private ImageFileDTO getFileCache(String tmpDirectory, String fileName)
	    throws IOException {

	ImageFileDTO dto = fileChannelMap.get(fileName);
	if (dto == null) {

	    dto = populateFileCacheObject(tmpDirectory, fileName);
	    fileChannelMap.put(fileName, dto);

	} else if (dto.isTimeOut()) {

	    dto.getFc().force(true);
	    dto.getFc().close();
	    dto.setFc(null);

	    // imageFileCache.put(fileName, dto);
	    // cleanupTempFiles(imageFileCache);

	    FileUtils.rollOver(tmpDirectory, fileName);
	    dto = populateFileCacheObject(tmpDirectory, fileName);
	    fileChannelMap.remove(fileName);
	    fileChannelMap.put(fileName, dto);

	}

	return dto;

    }

    @SuppressWarnings("resource")
    private ImageFileDTO populateFileCacheObject(String tmpDirectory,
	    String fileName) throws IOException {

	new File(tmpDirectory).mkdir();
	// File file = FileUtils.getTemperoryFile(tmpDirectory, fileName);

	File file = FileUtils.getFile(tmpDirectory, fileName);
	FileChannel fc = new FileOutputStream(file).getChannel();

	ImageFileDTO dto = new ImageFileDTO();
	dto.setFilePath(file.getCanonicalPath());
	dto.setFc(fc);
	return dto;
    }

}
