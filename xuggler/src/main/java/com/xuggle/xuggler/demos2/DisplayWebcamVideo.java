/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.xuggle.xuggler.demos2;

import java.awt.image.BufferedImage;

import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IContainerFormat;
import com.xuggle.xuggler.IError;
import com.xuggle.xuggler.IMetaData;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;
import com.xuggle.xuggler.Utils;
import com.xuggle.xuggler.demos.VideoImage;

public class DisplayWebcamVideo {
    private static VideoImage mScreen = null;

    public static void main(String[] args) {
	if (args.length != 2) {
	    throw new IllegalArgumentException("must pass in driver and device name");
	}
	String driverName = args[0];
	String deviceName = args[1];

	if (!(IVideoResampler.isSupported(IVideoResampler.Feature.FEATURE_COLORSPACECONVERSION))) {
	    throw new RuntimeException(
		    "you must install the GPL version of Xuggler (with IVideoResampler support) for this demo to work");
	}

	IContainer container = IContainer.make();

	IContainerFormat format = IContainerFormat.make();
	if (format.setInputFormat(driverName) < 0) {
	    throw new IllegalArgumentException("couldn't open webcam device: " + driverName);
	}

	IMetaData params = IMetaData.make();

	params.setValue("framerate", "30/1");
	params.setValue("video_size", "320x240");

	int retval = container.open(deviceName, IContainer.Type.READ, format, false, true, params, null);

	if (retval < 0) {
	    IError error = IError.make(retval);
	    throw new IllegalArgumentException("could not open file: " + deviceName + "; Error: "
		    + error.getDescription());
	}

	int numStreams = container.getNumStreams();

	int videoStreamId = -1;
	IStreamCoder videoCoder = null;
	for (int i = 0; i < numStreams; ++i) {
	    IStream stream = container.getStream(i);

	    IStreamCoder coder = stream.getStreamCoder();

	    if (coder.getCodecType() != ICodec.Type.CODEC_TYPE_VIDEO)
		continue;
	    videoStreamId = i;
	    videoCoder = coder;
	    break;
	}

	if (videoStreamId == -1) {
	    throw new RuntimeException("could not find video stream in container: " + deviceName);
	}

	if (videoCoder.open() < 0) {
	    throw new RuntimeException("could not open video decoder for container: " + deviceName);
	}
	IVideoResampler resampler = null;
	if (videoCoder.getPixelType() != IPixelFormat.Type.BGR24) {
	    resampler = IVideoResampler.make(videoCoder.getWidth(), videoCoder.getHeight(), IPixelFormat.Type.BGR24,
		    videoCoder.getWidth(), videoCoder.getHeight(), videoCoder.getPixelType());

	    if (resampler == null) {
		throw new RuntimeException("could not create color space resampler for: " + deviceName);
	    }

	}

	openJavaWindow();

	IPacket packet = IPacket.make();
	while (container.readNextPacket(packet) >= 0) {
	    if (packet.getStreamIndex() == videoStreamId) {
		IVideoPicture picture = IVideoPicture.make(videoCoder.getPixelType(), videoCoder.getWidth(),
			videoCoder.getHeight());

		int offset = 0;
		while (offset < packet.getSize()) {
		    int bytesDecoded = videoCoder.decodeVideo(picture, packet, offset);
		    if (bytesDecoded < 0)
			throw new RuntimeException("got error decoding video in: " + deviceName);
		    offset += bytesDecoded;

		    if (picture.isComplete()) {
			IVideoPicture newPic = picture;

			if (resampler != null) {
			    newPic = IVideoPicture.make(resampler.getOutputPixelFormat(), picture.getWidth(),
				    picture.getHeight());
			    if (resampler.resample(newPic, picture) < 0)
				throw new RuntimeException("could not resample video from: " + deviceName);
			}
			if (newPic.getPixelType() != IPixelFormat.Type.BGR24) {
			    throw new RuntimeException("could not decode video as BGR 24 bit data in: " + deviceName);
			}

			BufferedImage javaImage = Utils.videoPictureToImage(newPic);

			updateJavaWindow(javaImage);
		    }

		}

	    }

	}

	if (videoCoder != null) {
	    videoCoder.close();
	    videoCoder = null;
	}
	if (container != null) {
	    container.close();
	    container = null;
	}
	closeJavaWindow();
    }

    private static void updateJavaWindow(BufferedImage javaImage) {
	mScreen.setImage(javaImage);
    }

    private static void openJavaWindow() {
	mScreen = new VideoImage();
    }

    private static void closeJavaWindow() {
	System.exit(0);
    }
}