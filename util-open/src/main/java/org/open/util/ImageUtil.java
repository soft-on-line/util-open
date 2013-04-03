package org.open.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 图片处理类
 *
 * @author moon
 * @version $Id: ImageUtil.java,v 1.4 2009/06/14 07:16:56 ibm Exp $
 */
public class ImageUtil {

    protected final static Log log = LogFactory.getLog(ImageUtil.class);

    /**
     * @see #resize(File, File, int, int)
     */
    public static void resize(String srcImage, String descImage, int w, int h) throws IOException {
        resize(srcImage, descImage, w, h, false);
    }

    /**
     * @see #resize(File, File, int, int)
     */
    public static void resize(String srcImage, String descImage, int w, int h, boolean zoomOutOnly) throws IOException {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(srcImage);
            out = new FileOutputStream(descImage);
            _resize(in, out, w, h, zoomOutOnly);
        } finally {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        }
    }

    /**
     * 强制压缩/放大图片到固定的大小
     *
     * @param srcImage 原始图片
     * @param descImage 缩放后的图片
     * @param w 新宽度
     * @param h 新高度
     * @throws IOException
     */
    public static void resize(File srcImage, File descImage, int w, int h) throws IOException {
        resize(srcImage, descImage, w, h, false);
    }

    /**
     * 强制压缩/放大图片到固定的大小
     *
     * @param srcImage 原始图片
     * @param descImage 缩放后的图片
     * @param w 新宽度
     * @param h 新高度
     * @throws IOException
     */
    public static void resize(File srcImage, File descImage, int w, int h, boolean zoomOutOnly) throws IOException {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(srcImage);
            out = new FileOutputStream(descImage);
            _resize(in, out, w, h, zoomOutOnly);
        } finally {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        }
    }

    /**
     * 强制压缩/放大图片到固定的大小
     *
     * @param srcImage 原始图片流
     * @param w 新宽度
     * @param h 新高度
     * @return 缩放后的图片流
     * @throws IOException
     */
    public static byte[] resize(byte[] srcImage, int w, int h) throws IOException {
        return resize(srcImage, w, h, false);
    }

    /**
     * 强制压缩/放大图片到固定的大小
     *
     * @param srcImage 原始图片流
     * @param w 新宽度
     * @param h 新高度
     * @return 缩放后的图片流
     * @throws IOException
     */
    public static byte[] resize(byte[] srcImage, int w, int h, boolean zoomOutOnly) throws IOException {
        ByteArrayOutputStream out = null;
        ByteArrayInputStream in = null;
        try {
            out = new ByteArrayOutputStream();
            in = new ByteArrayInputStream(srcImage);
            _resize(in, out, w, h, zoomOutOnly);
            return out.toByteArray();
        } finally {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        }
    }

    /**
     * 强制压缩/放大图片到固定的大小
     *
     * @param isImage 原始图片流
     * @param osImage 输出图片流
     * @param w 新宽度
     * @param h 新高度
     * @throws IOException
     */
    private static void _resize(InputStream isImage, OutputStream osImage, int w, int h, boolean zoomOutOnly)
                                                                                                             throws IOException {
        try {
            BufferedImage image = javax.imageio.ImageIO.read(isImage);
            // 得到图片的原始大小， 以便按比例压缩。
            int imageWidth = image.getWidth(null);
            int imageHeight = image.getHeight(null);

            // 只是缩小模式 且 所给长宽比均比原始大时 保持原图片尺寸
            if (zoomOutOnly && imageWidth < w && imageHeight < h) {
                h = imageHeight;
                w = imageWidth;
            } else {
                // 得到合适的压缩大小，按比例。
                if ((1.0 * imageWidth / imageHeight) > (1.0 * w / h)) {
                    h = (int) Math.round((imageHeight * w * 1.0 / imageWidth));
                } else {
                    w = (int) Math.round((imageWidth * h * 1.0 / imageHeight));
                }
            }

            // 构建图片对象
            BufferedImage _image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            // 绘制缩小后的图
            _image.getGraphics().drawImage(image, 0, 0, w, h, null);

            // JPEGCodec.createJPEGEncoder(osImage).encode(_image);
            ImageIO.write(_image, "JPEG", osImage);
        } finally {
            isImage.close();
            osImage.close();
        }
    }

    public static class ImageInfo {

        /**
         * 图片宽度,默认-1为出错值
         */
        public int  width  = -1;

        /**
         * 图片高度,默认-1为出错值
         */
        public int  height = -1;

        /**
         * 图片大小，单位：Byte,默认-1为出错值
         */
        public long size   = -1;
    }

    /**
     * 根据图片流取得图片的信息
     *
     * @param data
     * @return
     */
    public static ImageInfo getImageInfo(byte[] data) {
        ImageInfo imageInfo = new ImageInfo();
        BufferedImage img = null;
        try {
            img = ImageIO.read(new ByteArrayInputStream(data));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return imageInfo;
        }
        if (null == img) {
            throw new java.lang.IllegalArgumentException("无效的图片流！");
        }
        imageInfo.height = img.getHeight();
        imageInfo.width = img.getWidth();
        imageInfo.size = data.length;

        return imageInfo;
    }

    /**
     * 判断流是否为图片
     *
     * @param data
     * @return
     */
    public static boolean isImage(File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            return isImage(fis);
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            return false;
        } finally {
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    // do nothing.
                }
            }
        }
    }

    /**
     * 判断流是否为图片
     *
     * @param data
     * @return
     */
    public static boolean isImage(byte[] data) {
        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(data);
            return isImage(bais);
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            return false;
        } finally {
            if (null != bais) {
                try {
                    bais.close();
                } catch (IOException e) {
                    // do nothing.
                }
            }
        }
    }

    /**
     * 判断流是否为图片
     *
     * @param is
     * @return
     */
    public static boolean isImage(InputStream is) {
        try {
            BufferedImage image = ImageIO.read(is);
            if (null == image) {
                return false;
            } else {
                return !(image.getWidth() == 0 || image.getHeight() == 0);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            return false;
        }
    }

    /**
     * 获取图片文件实际类型,若不是图片则返回null
     *
     * @param file
     * @return
     */
    public static String getImageFileType(File file) {
        if (isImage(file)) {
            ImageInputStream iis = null;
            try {
                iis = ImageIO.createImageInputStream(file);
                Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
                if (!iter.hasNext()) {
                    return null;
                }
                ImageReader reader = iter.next();
                iis.close();
                return reader.getFormatName();
            } catch (Exception e) {
                log.error(e.getMessage(), e);

                return null;
            } finally {
                if (null != iis) {
                    try {
                        iis.close();
                    } catch (IOException e) {
                        // do nothing.
                    }
                }
            }
        } else {
            return null;
        }
    }
}
