package com.att.wifi.camera;

import java.nio.channels.FileChannel;

public class ImageFileDTO {

    private static final int TIMEOUT_MILLISECONDS = 30000;

    private long timeOut;

    private long position;

    private FileChannel fc;

    public ImageFileDTO() {

	this.timeOut = System.currentTimeMillis() + TIMEOUT_MILLISECONDS;
	this.position = 0;

    }

    public long getTimeOut() {
	return timeOut;
    }

    public void setTimeOut(long timeOut) {
	this.timeOut = timeOut;
    }

    public long getPosition() {
	return position;
    }

    public void setPosition(long position) {
	this.position = position;
    }

    public FileChannel getFc() {
	return fc;
    }

    public void setFc(FileChannel fc) {
	this.fc = fc;
    }

    public boolean isTimeOut() {

	return System.currentTimeMillis() > timeOut;

    }
}
