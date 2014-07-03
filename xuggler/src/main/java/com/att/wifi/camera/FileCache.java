package com.att.wifi.camera;

import java.io.IOException;
import java.io.InputStream;

public interface FileCache {

    void transferImage(String ipAddress, String tmpDirectory, InputStream inputStream) throws IOException;
}
