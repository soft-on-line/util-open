package org.open.util;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 读取文件内容的类
 * @author 覃芝鹏
 * @version $Id: ReaderUtil.java,v 1.10 2009/04/24 04:07:59 moon Exp $
 */
public class ReaderUtil {

	/**
	 * 读取一个目录下文件名称
	 * @param list 存储文件名的池子
	 * @param file File[]数组
	 * @param recursive 是否递归遍历其子目录，true是，false否。
	 */
	private static void _fileNames(List<String> list, File[] file, boolean recursive) {
		for (int i = 0; i < file.length; i++) {
			// 如果是文件
			if (file[i].isFile()) {
				list.add(file[i].toString());
			}

			// 如果是目录 && 如果需要递归
			if (file[i].isDirectory() && recursive) {
				_fileNames(list, file[i].listFiles(), recursive);
			}
		}
	}

	/**
	 * 递归遍历文件 或 一个目录下所有文件后读取到字符串库
	 * @param list 用于存储字符窜数组的库
	 * @param file 一个文件或者一个目录
	 * @return 递归遍历每个文件后读取到字符串数组库
	 * @throws IOException
	 */
	private static List<String> _read(List<String> list, File file) throws IOException {
		// 如果是文件
		if (file.isFile()) {
			// 读取文件然后 存入库里面
			list.add(read(file.toString(), null));
			return list;
		}

		// 如果是目录
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				// 递归调用自己反复读取文件内容
				_read(list, files[i]);
			}
		}

		return list;
	}

	/**
	 * 读取一个目录下文件名称。默认不递归子目录
	 * @param file 文件路径
	 * @return 路径下文件名数组
	 */
	public static String[] getFileNames(File file) {
		return getFileNames(file, false);
	}

	/**
	 * 读取一个目录下文件名称
	 * @param file 文件路径
	 * @param recursive 是否递归遍历其子目录，true是，false否。
	 * @return 路径下文件名数组
	 */
	public static String[] getFileNames(File file, boolean recursive) {
		if (file.isFile()) {
			return new String[] { file.toString() };
		} else {
			List<String> list = new ArrayList<String>();
			_fileNames(list, file.listFiles(), recursive);
			return list.toArray(new String[list.size()]);
		}
	}

	/**
	 * 返回 一个目录或一个文件 下面所有文件取得的字符串数组。 例如一个目录下3个文件则返回长度为3的数组 直接传入一个文件则返回1个数组
	 * @param file 一个目录 或 一个文件
	 * @return 每个文件的文本字符串数组
	 * @throws IOException
	 */
	public static String[] read(File file) throws IOException {
		List<String> list = new ArrayList<String>();

		list = _read(list, file);

		return (String[]) list.toArray(new String[list.size()]);
	}

	/**
	 * 从指定的文件中读取 文本内容
	 * @param file 指定的文件
	 * @return 读取到文本内容
	 * @throws IOException
	 */
	public static String read(File file, String charsetName) throws IOException {
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		InputStreamReader isr = null;
		try {
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			if (charsetName == null) {
				isr = new InputStreamReader(bis);
			} else {
				isr = new InputStreamReader(bis, charsetName);
			}

			StringBuffer tmp = new StringBuffer(10240);
			char data[] = new char[10240];
			int size = 0;
			while ((size = isr.read(data)) != -1) {
				tmp.append(new String(data, 0, size));
			}

			return tmp.toString();
		}
		finally {
			if (isr != null) {
				isr.close();
			}

			if (bis != null) {
				bis.close();
			}

			if (fis != null) {
				fis.close();
			}
		}
	}

	/**
	 * 从输入流中读取出字符串
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String read(InputStream in) throws IOException {
		return read(in, (String) null);
	}

	/**
	 * 从输入流中读取指定编码的字符串
	 * @param in
	 * @param charsetName
	 * @return
	 * @throws IOException
	 */
	public static String read(InputStream in, String charsetName) throws IOException {
		if (null == charsetName) {
			return new String(readByte(in));
		}

		return new String(readByte(in), charsetName);
	}

	/**
	 * 从指定的文件中读取 文本内容
	 * @param file 指定的文件
	 * @return 读取到文本内容
	 * @throws IOException
	 */
	public static String read(String fileName) throws IOException {
		return read(fileName, (String) null);
	}

	/**
	 * 从文件中读取指定编码的字符串
	 * @param fileName
	 * @param charsetName
	 * @return
	 * @throws IOException
	 */
	public static String read(String fileName, String charsetName) throws IOException {
		return read(new File(fileName), charsetName);
	}

	/**
	 * 从一个URL地址中读取字符串
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String read(URL url) throws IOException {
		InputStream is = url.openStream();
		try {
			return read(is);
		}
		finally {
			is.close();
		}
	}

	/**
	 * 从指定的文件中读取二进制流
	 * @param file 指定的文件
	 * @return byte[] 二进制流
	 * @throws IOException
	 */
	public static byte[] readByte(File file) throws IOException {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);

			List<byte[]> list = new ArrayList<byte[]>();
			int bufSize = 10240;
			byte[] buf;

			int count = 0;
			int size = 0;
			while (-1 != (size = fis.read((buf = new byte[bufSize])))) {
				list.add(buf);
				count += size;
			}

			byte[] data = new byte[count];
			int s = 0;
			for (byte[] tmp : list) {
				int c = (count - s);
				System.arraycopy(tmp, 0, data, s, ((c < bufSize) ? c : bufSize));
				s += bufSize;
			}

			return data;
		}
		finally {
			if (null != fis) {
				fis.close();
			}
		}
	}

	/**
	 * 从输入流中读取字节流
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static byte[] readByte(InputStream in) throws IOException {
		if (null == in) {
			throw new NullPointerException("input stream is null!");
		}

		ByteArrayOutputStream data = new ByteArrayOutputStream();
		try {
			int num = 0;
			byte[] buffer = new byte[1024];
			while ((num = in.read(buffer)) > 0) {
				data.write(buffer, 0, num);
			}
			return data.toByteArray();
		}
		finally {
			data.close();
		}
	}

	/**
	 * 从指定的文件中读取二进制流
	 * @param file 指定的文件
	 * @return byte[] 二进制流
	 * @throws IOException
	 */
	public static byte[] readByte(String file) throws IOException {
		return readByte(new File(file));
	}

}
