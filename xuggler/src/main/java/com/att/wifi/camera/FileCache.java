package com.att.wifi.camera;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;

public interface FileCache {

    void transferImage(String ipAddress, String tmpDirectory,
	    ReadableByteChannel rbc) throws IOException;
}
