package com.att.wifi.camera;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileCache {

    ImageFileDTO getFileCache(String tmpDirectory, String fileName)
	    throws FileNotFoundException, IOException;
}
