package org.open.util;


import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
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

/**
 * 图片处理工具包类（备注：另外也有一个图片处理类很强大，后期完善可以参考；网址：http://code.google.com/p/thumbnailator/）
 * @author zhipeng.qzp
 */
public class ImageUtil {

	/**
	 * 封装了的一张图片信息，包括宽度、高度、图片大小
	 * @author zhipeng.qzp
	 */
	public static class ImageInfo {

		/** 图片宽度,默认-1为出错值 */
		public int  width  = -1;

		/** 图片高度,默认-1为出错值 */
		public int  height = -1;

		/** 图片大小，单位：Byte,默认-1为出错值 */
		public long size   = -1;

		@Override
		public String toString() {
			return "ImageInfo [width=" + width + ", height=" + height + ", size=" + size + "]";
		}
	}

	/**
	 * 图片拼装辅助类，可以让一张图片一次性修改其图片信息，例如可以合并另一张图片，原图片插入文字等
	 * @author zhipeng.qzp
	 */
	public static class ImageMerging {

		private BufferedImage srcImage;

		private BufferedImage dstImage;

		private Graphics2D    g2d;

		public ImageMerging(BufferedImage srcImage) {
			this.srcImage = srcImage;
			this.dstImage = new BufferedImage(srcImage.getWidth(), srcImage.getHeight(), BufferedImage.TYPE_INT_RGB);
			g2d = dstImage.createGraphics();
			g2d.drawImage(srcImage, null, 0, 0);
		}

		/**
		 * 计算文字的高度，宽度，让你居中绘画文字
		 * @param startX
		 * @param startY
		 * @param width
		 * @param height
		 * @param str
		 */
		private void drawStringCenter(int startX, int startY, int width, int height, String str) {
			FontMetrics fm = g2d.getFontMetrics();

			int x = startX;
			int y = startY;

			if (width > 0) {
				int stringWidth = fm.stringWidth(str);

				x += width / 2 - stringWidth / 2;
			}

			if (height > 0) {
				int stringAscent = fm.getAscent();
				int stringDescent = fm.getDescent();

				y += height / 2 + (stringAscent - stringDescent) / 2;
			}

			g2d.drawString(str, x, y);
		}

		/**
		 * 初始化字体和字体颜色
		 * @param font
		 * @param fontColor
		 */
		private void drawStringInit(Font font, Color fontColor) {
			//设置背景色
			//            g2d.setBackground(Color.WHITE);

			//设置字体
			g2d.setFont(font);

			//设置字体颜色
			g2d.setColor(fontColor);

			//字体边缘平滑化
			//            //1.抗锯齿关闭。
			//            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
			//            //2.抗锯齿开启。
			//            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			//            //3.使用TEXT_ANTIALIAS_GASP提示。
			//            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
			//4.使用TEXT_ANTIALIAS_LCD_HRGB提示
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		}

		/**
		 * @param startPoint
		 * @param mergingImage
		 * @return
		 * @see #mergeImage(Point, BufferedImage, boolean)
		 */
		public BufferedImage mergeImage(Point startPoint, BufferedImage mergingImage) {
			return this.mergeImage(startPoint, mergingImage, true);
		}

		/**
		 * 合并图片
		 * @param startPoint 原图片起始坐标
		 * @param mergingImage 需要合并的图片
		 * @param 是否覆盖重叠部分
		 * @return
		 */
		public BufferedImage mergeImage(Point startPoint, BufferedImage mergingImage, boolean ifCover) {
			int w2 = mergingImage.getWidth();
			int h2 = mergingImage.getHeight();

			for (int i = 0; i < w2; i++) {
				for (int j = 0; j < h2; j++) {
					int _rgb2 = mergingImage.getRGB(i, j);
					//不是覆盖模式，就图片重叠部分&操作
					if (!ifCover) {
						int _rgb1 = srcImage.getRGB(i + startPoint.x, j + startPoint.y);
						if (_rgb1 != _rgb2) {
							_rgb2 = _rgb1 & _rgb2;
						}
					}
					dstImage.setRGB(i + startPoint.x, j + startPoint.y, _rgb2);
				}
			}

			return dstImage;
		}

		/**
		 * 合并图片
		 * @param startPoint
		 * @param width
		 * @param height
		 * @param mergingImage
		 * @return
		 * @see #mergeImage(Point, int, int, BufferedImage, boolean)
		 */
		public BufferedImage mergeImage(Point startPoint, int width, int height, BufferedImage mergingImage) {
			return this.mergeImage(startPoint, width, height, mergingImage, true);
		}

		/**
		 * 合并图片
		 * @param startPoint 原图片起始坐标
		 * @param weight 原图片起始坐标后的宽度
		 * @param height 原图片起始坐标后的高度
		 * @param mergingImage 需要合并的图片
		 * @param ifCover 是否覆盖重叠部分
		 * @return
		 */
		public BufferedImage mergeImage(Point startPoint, int width, int height, BufferedImage mergingImage, boolean ifCover) {
			int w2 = mergingImage.getWidth();
			int h2 = mergingImage.getHeight();

			//当合并的图片尺寸比原始图片小时，需要校正起始点，让合并的图片居中
			int adjustX = w2 < width ? (width - w2) / 2 : 0;
			int adjustY = h2 < height ? (height - h2) / 2 : 0;

			int minWidth = Math.min(w2, width);
			int minHeight = Math.min(h2, height);
			for (int i = 0; i < minWidth; i++) {
				for (int j = 0; j < minHeight; j++) {
					int _rgb2 = mergingImage.getRGB(i, j);
					//不是覆盖模式，就图片重叠部分&操作
					if (!ifCover) {
						int _rgb1 = srcImage.getRGB(i + startPoint.x, j + startPoint.y);
						if (_rgb1 != _rgb2) {
							_rgb2 = _rgb1 & _rgb2;
						}
					}
					dstImage.setRGB(i + startPoint.x + adjustX, j + startPoint.y + adjustY, _rgb2);
				}
			}

			return dstImage;
		}

		/**
		 * 原始图片中写入文字信息
		 * @param startPoint
		 * @param font
		 * @param fontColor
		 * @param content
		 * @return
		 */
		public BufferedImage mergeStringCenter(Point startPoint, int width, int height, Font font, Color fontColor, int rowHeight, String... content) {
			//初始化字体和字体颜色
			drawStringInit(font, fontColor);

			//写入文字信息
			int count = 1;
			for (String str : content) {
				if (1 == count) {
					//第一行不加行高
					drawStringCenter(startPoint.x, startPoint.y + font.getSize() * (count++), width, height, str);
				} else {
					//第二行起需要加行高
					drawStringCenter(startPoint.x, startPoint.y + (rowHeight) * (count - 1) + font.getSize() * (count++), width, height, str);
				}
			}

			return this.dstImage;
		}

		public BufferedImage mergeStringLeft(Point startPoint, Font font, Color fontColor, int rowHeight, String... content) {
			//初始化字体和字体颜色			
			drawStringInit(font, fontColor);

			//写入文字信息
			int count = 1;
			for (String str : content) {
				if (1 == count) {
					//第一行不加行高
					g2d.drawString(str, startPoint.x, startPoint.y + font.getSize() * (count++));
				} else {
					//第二行起需要加行高
					g2d.drawString(str, startPoint.x, startPoint.y + (rowHeight) * (count - 1) + font.getSize() * (count++));
				}
			}

			return this.dstImage;
		}

		public BufferedImage outImage() {
			return this.dstImage;
		}
	}

	/** 默认图片输出文件格式 */
	public final static String OUT_FORMAT_NAME = "JPEG";

	/**
	 * 强制压缩/放大图片到固定的大小
	 * @param isImage 原始图片流
	 * @param osImage 输出图片流
	 * @param w 新宽度
	 * @param h 新高度
	 * @throws IOException
	 */
	private static void _resize(InputStream isImage, OutputStream osImage, String outFormatName, int w, int h, boolean zoomOutOnly) throws IOException {
		BufferedImage image = ImageIO.read(isImage);
		//得到图片的原始大小， 以便按比例压缩。
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);

		//只是缩小模式 且 所给长宽比均比原始大时 保持原图片尺寸
		if (zoomOutOnly && imageWidth < w && imageHeight < h) {
			h = imageHeight;
			w = imageWidth;
		} else {
			//得到合适的压缩大小，按比例。
			if ((1.0 * imageWidth / imageHeight) > (1.0 * w / h)) {
				h = (int) Math.round((imageHeight * w * 1.0 / imageWidth));
			} else {
				w = (int) Math.round((imageWidth * h * 1.0 / imageHeight));
			}
		}

		//构建图片对象
		BufferedImage _image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

		//绘制缩小后的图（平滑处理优先）
		_image.getGraphics().drawImage(image.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, w, h, null);
		//		image.SCALE_SMOOTH //平滑优先
		//		image.SCALE_FAST//速度优先
		//		image.SCALE_AREA_AVERAGING //区域均值
		//		image.SCALE_REPLICATE //像素复制型缩放
		//		image.SCALE_DEFAULT //默认缩放模式

		//		JPEGCodec.createJPEGEncoder(osImage).encode(_image);
		ImageIO.write(_image, outFormatName, osImage);
	}

	/**
	 * 获取图片文件实际类型,若不是图片则返回null
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String getImageFileType(File file) throws IOException {
		if (!isImage(file)) {
			return null;
		}

		ImageInputStream iis = null;
		try {
			iis = ImageIO.createImageInputStream(file);
			Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
			if (!iter.hasNext()) {
				return null;
			}
			ImageReader reader = iter.next();
			return reader.getFormatName();
		}
		finally {
			if (null != iis) {
				iis.close();
			}
		}
	}

	/**
	 * 根据图片流取得图片的信息
	 * @param data
	 * @return
	 */
	public static ImageInfo getImageInfo(byte[] data) {
		ImageInfo imageInfo = new ImageInfo();
		BufferedImage img = null;
		try {
			img = ImageIO.read(new ByteArrayInputStream(data));
		}
		catch (IOException e) {
			throw new java.lang.IllegalArgumentException("invalid image stream!");
		}
		if (null == img) {
			throw new java.lang.IllegalArgumentException("invalid image stream!");
		}
		imageInfo.height = img.getHeight();
		imageInfo.width = img.getWidth();
		imageInfo.size = data.length;

		return imageInfo;
	}

	/**
	 * 判断流是否为图片
	 * @param data
	 * @return
	 */
	public static boolean isImage(byte[] data) {
		ByteArrayInputStream bais = null;
		try {
			bais = new ByteArrayInputStream(data);
			return isImage(bais);
		}
		catch (Exception e) {
			return false;
		}
		finally {
			if (null != bais) {
				try {
					bais.close();
				}
				catch (IOException e) {
					//do nothing.
				}
			}
		}
	}

	/**
	 * 判断流是否为图片
	 * @param data
	 * @return
	 */
	public static boolean isImage(File file) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			return isImage(fis);
		}
		catch (Exception e) {
			return false;
		}
		finally {
			if (null != fis) {
				try {
					fis.close();
				}
				catch (IOException e) {
					//do nothing.
				}
			}
		}
	}

	/**
	 * 判断流是否为图片
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static boolean isImage(InputStream is) throws IOException {
		BufferedImage image = ImageIO.read(is);
		if (null == image) {
			return false;
		} else {
			return !(image.getWidth() == 0 || image.getHeight() == 0);
		}
	}

	/**
	 * @see #resize(byte[], String, int, int)
	 */
	public static byte[] resize(byte[] srcImage, int w, int h) throws IOException {
		String outFormatName = FileUtil.getFileType(srcImage);
		if (null == outFormatName) {
			outFormatName = OUT_FORMAT_NAME;
		}
		return resize(srcImage, outFormatName, w, h, false);
	}

	/**
	 * @see #resize(byte[], String, int, int, boolean)
	 */
	public static byte[] resize(byte[] srcImage, String outFormatName, int w, int h) throws IOException {
		return resize(srcImage, outFormatName, w, h, false);
	}

	/**
	 * 强制压缩/放大图片到固定的大小
	 * @param srcImage 原始图片流
	 * @param w 新宽度
	 * @param h 新高度
	 * @return 缩放后的图片流
	 * @throws IOException
	 */
	public static byte[] resize(byte[] srcImage, String outFormatName, int w, int h, boolean zoomOutOnly) throws IOException {
		ByteArrayOutputStream out = null;
		ByteArrayInputStream in = null;
		try {
			out = new ByteArrayOutputStream();
			in = new ByteArrayInputStream(srcImage);
			_resize(in, out, outFormatName, w, h, zoomOutOnly);
			return out.toByteArray();
		}
		finally {
			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}
		}
	}

	/**
	 * @see #resize(File, File, String, int, int, boolean)
	 */
	public static void resize(File srcImage, File descImage, int w, int h) throws IOException {
		ImageUtil.resize(srcImage, descImage, FileUtil.getSuffix(descImage), w, h, false);
	}

	/**
	 * @see #resize(File, File, String, int, int, boolean)
	 */
	public static void resize(File srcImage, File descImage, int w, int h, boolean zoomOutOnly) throws IOException {
		ImageUtil.resize(srcImage, descImage, FileUtil.getSuffix(descImage), w, h, zoomOutOnly);
	}

	/**
	 * @see #resize(File, File, String, int, int, boolean)
	 */
	public static void resize(File srcImage, File descImage, String outFormatName, int w, int h) throws IOException {
		resize(srcImage, descImage, outFormatName, w, h, false);
	}

	/**
	 * 强制压缩/放大图片到固定的大小
	 * @param srcImage 原始图片
	 * @param descImage 缩放后的图片
	 * @param w 新宽度
	 * @param h 新高度
	 * @throws IOException
	 */
	public static void resize(File srcImage, File descImage, String outFormatName, int w, int h, boolean zoomOutOnly) throws IOException {
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(srcImage);
			out = new FileOutputStream(descImage);
			_resize(in, out, outFormatName, w, h, zoomOutOnly);
		}
		finally {
			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}
		}
	}

	/**
	 * @see #resize(File, File, int, int)
	 */
	public static void resize(String srcImage, String descImage, String outFormatName, int w, int h) throws IOException {
		resize(srcImage, descImage, outFormatName, w, h, false);
	}

	/**
	 * @see #resize(File, File, int, int)
	 */
	public static void resize(String srcImage, String descImage, String outFormatName, int w, int h, boolean zoomOutOnly) throws IOException {
		resize(new File(srcImage), new File(descImage), outFormatName, w, h, zoomOutOnly);
	}

}
