package com.att.wifi.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class URLConnectionReader {
	private static final int MB = 10*1024;

	public static void main(String[] args) throws Exception {

		URL website = new URL("http://192.168.1.90/img/media.ts");
		ReadableByteChannel rbc = Channels.newChannel(website.openStream());
		generateFiles(rbc);

	}

	@SuppressWarnings("resource")
	private static void generateFiles(ReadableByteChannel rbc)
			throws IOException, FileNotFoundException {
		long timeOut = System.currentTimeMillis() + 30000;
		long now = 0;
		FileChannel fc = null;
		long position = 0;

		while (true) {

			if (isTimeOut(now, timeOut)) {

				File file = File.createTempFile("192.168.1.90", ".ts", new File(
						"d:/image"));
				file.deleteOnExit();
				fc = new FileOutputStream(file).getChannel();
				position = 0;
				timeOut = System.currentTimeMillis() + 30000;

			}
			position += fc.transferFrom(rbc, position, MB);
			now = System.currentTimeMillis();

		}
	}

	private static boolean isTimeOut(long now, long timeOut) {

		System.err.println(now > timeOut);
		if (now == 0) {
			return true;
		}

		return now > timeOut;
	}

}