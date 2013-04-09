package org.open.util;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomUtil {

	private final static String ASC_II        = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";

	private static Random       Global_Random = new Random();

	/**
	 * 得到一个0-9的随机数字
	 * @return
	 */
	public static int nextDigit() {
		return Global_Random.nextInt(10);
	}

	/**
	 * 得到number个数字组成的串
	 * @param number
	 * @return
	 */
	public static String nextDigit(int number) {
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < number; j++) {
			buf.append(nextDigit());
		}
		return buf.toString();
	}

	/**
	 * 得到一个a-z、A-Z、0-9的字符
	 * @return
	 */
	public static char nextChar() {
		return ASC_II.charAt(Global_Random.nextInt(ASC_II.length()));
	}

	/**
	 * 得到一个a-z、A-Z、0-9组成的字符串
	 * @param number
	 * @return
	 */
	public static String nextString(int number) {
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < number; j++) {
			buf.append(nextChar());
		}
		return buf.toString();
	}

	/**
	 * 得到一个随机正整数
	 * @return
	 */
	public static int nextInt() {
		return Global_Random.nextInt();
	}

	/**
	 * 得到一个小于n的随机正整数
	 * @param n
	 * @return
	 */
	public static int nextInt(int n) {
		return Global_Random.nextInt(n);
	}

	/**
	 * 得到一个大于等于min且小于等于max的随机数
	 * @param min
	 * @param max
	 * @return
	 */
	public static int nextInt(int min, int max) {
		int data;
		if (max < min) {
			data = min;
			min = max;
			max = data;
		}
		do {
			data = (Global_Random.nextInt()) % max;
		} while (data < min || data > max);
		return data;
	}

	/**
	 * 得到一个length长度的小于m的正整数数组
	 * @param m
	 * @param n
	 * @return
	 */
	public static Integer[] nextCombine(int m, int length) {
		Set<Integer> buf = new HashSet<Integer>();
		do {
			if (buf.size() > length) {
				break;
			} else {
				buf.add(new Random().nextInt(m));
			}
		} while (true);

		return buf.toArray(new Integer[buf.size()]);
	}

	/**
	 * 给定范围获得随机颜色
	 * @param ac
	 *            range 0-255
	 * @param bc
	 *            range 0-255
	 * @see Color#Color(int, int, int)
	 * @return Color
	 */
	public static Color nextColor(int ac, int bc) {
		if (ac > bc) {
			int tmp = ac;
			ac = bc;
			bc = tmp;
		}

		if (bc > 256) {
			bc = 256;
		}
		if (ac < 0) {
			ac = 0;
		}

		return new Color(nextInt(ac, bc), nextInt(ac, bc), nextInt(ac, bc));
	}

	/**
	 * 得到一个包含text的随机的图片
	 * @param text
	 * @return
	 */
	public static BufferedImage nextImage(String text) {
		int width = 60, height = 20;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// 获取图形上下文
		Graphics g = image.getGraphics();
		// 设定背景色
		g.setColor(nextColor(200, 250));
		g.fillRect(0, 0, width, height);
		// 设定字体
		g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		// 画边框
		// g.setColor(new Color());
		// g.drawRect(0,0,width-1,height-1);

		// 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
		g.setColor(nextColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = Global_Random.nextInt(width);
			int y = Global_Random.nextInt(height);
			int xl = Global_Random.nextInt(12);
			int yl = Global_Random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}
		g.setColor(new Color(20 + Global_Random.nextInt(110), 20 + Global_Random.nextInt(110), 20 + Global_Random.nextInt(110)));
		for (int i = 0; i < text.length(); i++) {

			g.drawString(text.substring(i, i + 1), 13 * i + 6, 16);
		}
		g.dispose();
		return image;
	}

}
