package org.open.util;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.open.util.ImageUtil.ImageInfo;
import org.open.util.ReaderUtil;

public class ImageUtilTest extends org.open.util.debug.Test {

    private static final Log log = LogFactory.getLog(ImageUtilTest.class);

    // @Test
    public void testGetImageInfo() {
        byte[] data = ReaderUtil.readByte(new File(
                                                   "E:\\lucene\\pic\\2\\253595380799170659113646927963058280753_128_96.JPG"));
        ImageInfo imageInfo = ImageUtil.getImageInfo(data);

        log.info(imageInfo.height);
        log.info(imageInfo.width);
        log.info(imageInfo.size);
    }

    @Test
    public void testIsImage() {
        du.print(ImageUtil.isImage(ReaderUtil.readByte("d:/1.gif")));
        du.print(ImageUtil.isImage(ReaderUtil.readByte("d:/proxy.txt")));
    }

}
