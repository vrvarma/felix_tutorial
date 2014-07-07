package com.att.wifi.camera;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

public class FileCacheImpl implements FileCache {

    private static final long FILECACHE_TIMEOUT = 3 * 60000;

    private static final int MAX_FILE_SIZE = 10 * 1024;

    private static final Logger LOGGER = LogManager
	    .getLogger(FileCacheImpl.class);

    private static Map<String, ImageFileDTO> fileChannelMap = new HashMap<String, ImageFileDTO>();

    ListMultimap<String, ImageFileDTO> imageFileCache = LinkedListMultimap
	    .create();
    private static FileCache instance = new FileCacheImpl();

    private FileCacheImpl() {
    }

    public static FileCache getInstance() {

	return instance;
    }

    @Override
    public void transferImage(String fileName, String tmpDirectory,
	    InputStream inputStream) throws IOException {

	ReadableByteChannel rbc = Channels.newChannel(inputStream);
	while (true) {

	    ImageFileDTO dto = getFileCache(tmpDirectory, fileName);
	    try {
		FileChannel fc = dto.getFc();
		long position = dto.getPosition();

		// LOGGER.info("RBC channel "+rbc.isOpen()+ "  "+rbc);
		position += fc.transferFrom(rbc, position, MAX_FILE_SIZE);
		dto.setPosition(position);
	    } catch (Exception e) {

		LOGGER.debug("Reached Here", e);

		dto.getRaf().close();
		dto.setRaf(null);
		fileChannelMap.remove(fileName);
		if (rbc.isOpen()) {
		    rbc.close();
		}

	    }
	}

    }

    private synchronized ImageFileDTO getFileCache(String tmpDirectory,
	    String fileName) throws IOException {

	ImageFileDTO dto = fileChannelMap.get(fileName);
	if (dto == null) {

	    dto = populateFileCacheObject(tmpDirectory, fileName);
	    fileChannelMap.put(fileName, dto);

	} else if (dto.isTimeOut()) {

	    dto.getRaf().close();
	    dto.setRaf(null);
	    fileChannelMap.remove(fileName);

	    imageFileCache.put(fileName, dto);
	    cleanupTempFiles(fileName);
	    // FileUtils.rollOver(tmpDirectory, fileName);
	    dto = populateFileCacheObject(tmpDirectory, fileName);

	    fileChannelMap.put(fileName, dto);
	    LOGGER.debug("FileChannel Map -> " + fileChannelMap);

	}

	return dto;

    }

    private void cleanupTempFiles(String fileName) throws IOException {

	List<ImageFileDTO> fileList = new CopyOnWriteArrayList<ImageFileDTO>(
		imageFileCache.get(fileName));

	long currentTime = System.currentTimeMillis();

	for (ImageFileDTO image : fileList) {

	    if (currentTime > image.getTimeOut() + FILECACHE_TIMEOUT) {

		LOGGER.debug("Removing " + image.getFilePath());
		Files.deleteIfExists(new File(image.getFilePath()).toPath());
		imageFileCache.remove(fileName, image);
	    }

	}

	LOGGER.debug("ImageFile Cache -->> " + imageFileCache);

    }

    private ImageFileDTO populateFileCacheObject(String tmpDirectory,
	    String fileName) throws IOException {

	new File(tmpDirectory).mkdir();
	File file = FileUtils.getTemperoryFile(tmpDirectory, fileName);

	// File file = FileUtils.getFile(tmpDirectory, fileName);
	RandomAccessFile raf = new RandomAccessFile(file, "rw");

	ImageFileDTO dto = new ImageFileDTO();
	dto.setFilePath(file.getCanonicalPath());
	dto.setRaf(raf);
	return dto;
    }

}
