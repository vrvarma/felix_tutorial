package com.att.wifi.camera;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

public class FileCacheImpl implements FileCache {

    private static Map<String, ImageFileDTO> fileDtoCache = new HashMap<String, ImageFileDTO>();

    private static FileCache instance = new FileCacheImpl();

    private FileCacheImpl() {
    }

    public static FileCache getInstance() {

	return instance;
    }

    @Override
    public ImageFileDTO getFileCache(String tmpDirectory, String fileName)
	    throws IOException {

	ImageFileDTO dto = fileDtoCache.get(fileName);
	if (dto == null) {

	    dto = populateFileCacheObject(tmpDirectory, fileName);
	    fileDtoCache.put(fileName, dto);

	} else {

	    if (dto.isTimeOut()) {

		dto.getFc().close();
		FileUtils.rollOver(tmpDirectory, fileName);
		dto = populateFileCacheObject(tmpDirectory, fileName);
		fileDtoCache.remove(fileName);
		fileDtoCache.put(fileName, dto);
	    }
	}

	return dto;

    }

    @SuppressWarnings("resource")
    private ImageFileDTO populateFileCacheObject(String tmpDirectory,
	    String fileName) throws IOException {

	File file = FileUtils.getFile(tmpDirectory, fileName);
	FileChannel fc = new FileOutputStream(file).getChannel();

	ImageFileDTO dto = new ImageFileDTO();
	dto.setFilePath(file.getCanonicalPath());
	dto.setFc(fc);
	return dto;
    }

}
