package org.open.util;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * 写文件内容的类
 * @author 覃芝鹏
 * @version $Id: WriterUtil.java,v 1.13 2009/05/09 17:37:02 moon Exp $
 */
public class WriterUtil {

	/**
	 * @param fileName 文件名字
	 * @return 删除结果
	 */
	public static boolean delete(String fileName) {
		return new File(fileName).delete();
	}

	/**
	 * byte[]字节流写文件
	 * @param fileName 文件名
	 * @param bytes byte[]
	 * @throws IOException
	 */
	public static void write(File fileName, byte[] bytes) throws IOException {
		FileOutputStream fos = null;
		try {
			// build path.
			FileUtil.mkdirsOfFile(fileName);
			fos = new FileOutputStream(fileName);

			fos.write(bytes);

			fos.flush();
		}
		finally {
			if (fos != null) {
				fos.close();
			}
		}
	}

	/**
	 * byte[]字节流写文件
	 * @param fileName 文件名
	 * @param bytes byte[]
	 * @throws IOException
	 */
	public static void write(String fileName, byte[] bytes) throws IOException {
		FileOutputStream fos = null;
		try {
			FileUtil.mkdirsOfFile(fileName);
			fos = new FileOutputStream(fileName);

			fos.write(bytes);

			fos.flush();
		}
		finally {
			if (fos != null) {
				fos.close();
			}
		}
	}

	/**
	 * @param file
	 * @param content
	 * @param charsetName
	 * @throws IOException
	 */
	public static void writeFile(File file, String content, String charsetName) throws IOException {
		writeFile(file, content, charsetName, false);
	}

	/**
	 * 写一个 自己指定 后缀名的 文件
	 * @param file 文件名
	 * @param content 文件内容
	 * @param charset 写文件内容编码
	 * @param append true当文件存在则追加，false全新写文件
	 * @throws IOException
	 */
	public static void writeFile(File file, String content, String charsetName, boolean append) throws IOException {
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter out = null;
		try {
			// 先创建父级目录
			FileUtil.mkdirsOfFile(file);

			fos = new FileOutputStream(file, append);

			if (null == charsetName) {
				osw = new OutputStreamWriter(fos);
			} else {
				osw = new OutputStreamWriter(fos, charsetName);
			}

			out = new BufferedWriter(osw);

			out.write(content);

			out.flush();
		}
		finally {
			if (out != null) {
				out.close();
			}
			if (null != osw) {
				osw.close();
			}
			if (null != fos) {
				fos.close();
			}
		}
	}

	/**
	 * 强制写一个 自己指定 后缀名的 文件
	 * @param file 文件名
	 * @param content 文件内容
	 * @throws IOException
	 */
	public static void writeFile(String file, String content) throws IOException {
		writeFile(file, content, false);
	}

	/**
	 * 写一个 自己指定 后缀名的 文件
	 * @param file 文件名
	 * @param content 文件内容
	 * @param append true当文件存在则追加，false全新写文件
	 * @throws IOException
	 */
	public static void writeFile(String file, String content, boolean append) throws IOException {
		writeFile(new File(file), content, (String) null, append);
	}

	/**
	 * 强制写一个 自己指定 后缀名的 文件
	 * @param file 文件名
	 * @param content 文件内容
	 * @param charsetName 字符编码
	 * @throws IOException
	 */
	public static void writeFile(String file, String content, String charsetName) throws IOException {
		writeFile(new File(file), content, charsetName, false);
	}

}
