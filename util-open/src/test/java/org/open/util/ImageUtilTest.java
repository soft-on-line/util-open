package org.open.util;


import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.open.util.ImageUtil.ImageInfo;
import org.open.util.debug.DebugUtil;

public class ImageUtilTest {

	private static final Log log = LogFactory.getLog(ImageUtilTest.class);

	// @Test
	public void testGetImageInfo() throws IOException {
		byte[] data = ReaderUtil.readByte(new File("E:\\lucene\\pic\\2\\253595380799170659113646927963058280753_128_96.JPG"));
		ImageInfo imageInfo = ImageUtil.getImageInfo(data);

		log.info(imageInfo.height);
		log.info(imageInfo.width);
		log.info(imageInfo.size);
	}

	@Test
	public void testIsImage() throws IOException {
		DebugUtil.print(ImageUtil.isImage(ReaderUtil.readByte("d:/1.gif")));
		DebugUtil.print(ImageUtil.isImage(ReaderUtil.readByte("d:/proxy.txt")));
	}

	@Test
	public void testResize() throws IOException {
		File file = new File("d://tt.jpg");
		byte[] data = ReaderUtil.readByte(file);
		ImageInfo ii = ImageUtil.getImageInfo(data);
		System.out.println(ii);

		ImageUtil.resize(file, new File("d://gg.jpeg"), 381, 517);
		//		ImageUtil.resize(file, new File("d://gg.jpg"),"png", 351, 417);
		//		ImageUtil.resize(file, new File("d://gg.jpg"), "png", 381, 517);
	}

}
