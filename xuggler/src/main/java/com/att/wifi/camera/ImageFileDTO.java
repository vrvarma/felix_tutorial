package com.att.wifi.camera;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class ImageFileDTO {

    private static final int TIMEOUT_MILLISECONDS = 30000;

    private long createdTime;

    private long timeOut;

    private long position;

    private String filePath;

    private RandomAccessFile raf;

    public ImageFileDTO() {

	this.createdTime = System.currentTimeMillis();
	this.timeOut = this.createdTime + TIMEOUT_MILLISECONDS;
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

	return raf.getChannel();
    }

    public boolean isTimeOut() {

	return System.currentTimeMillis() >= timeOut;

    }

    public String getFilePath() {

	return filePath;
    }

    public void setFilePath(String filePath) {

	this.filePath = filePath;
    }

    public long getCreatedTime() {
	return createdTime;
    }

    public void setCreatedTime(long createdTime) {
	this.createdTime = createdTime;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (int) (createdTime ^ (createdTime >>> 32));
	result = prime * result + ((filePath == null) ? 0 : filePath.hashCode());
	result = prime * result + (int) (position ^ (position >>> 32));
	result = prime * result + (int) (timeOut ^ (timeOut >>> 32));
	return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (!(obj instanceof ImageFileDTO)) {
	    return false;
	}
	ImageFileDTO other = (ImageFileDTO) obj;
	if (createdTime != other.createdTime) {
	    return false;
	}
	if (filePath == null) {
	    if (other.filePath != null) {
		return false;
	    }
	} else if (!filePath.equals(other.filePath)) {
	    return false;
	}
	if (position != other.position) {
	    return false;
	}
	if (timeOut != other.timeOut) {
	    return false;
	}
	return true;
    }

    public RandomAccessFile getRaf() {
	return raf;
    }

    public void setRaf(RandomAccessFile raf) {
	this.raf = raf;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "ImageFileDTO [createdTime=" + createdTime + ", timeOut=" + timeOut + ", position=" + position
		+ ", filePath=" + filePath + "]";
    }
}
