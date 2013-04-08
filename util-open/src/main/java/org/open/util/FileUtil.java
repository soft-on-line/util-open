package org.open.util;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

/**
 * 文件处理工具包类
 * @author moon
 * @version $Id: FileUtil.java,v 1.4 2009/05/09 17:37:02 moon Exp $
 */
public class FileUtil {

	/**
	 * 文件类型枚举定义
	 */
	public static enum FileType {

		/** JPEG "jpg", "FFD8FF" */
		JPG("jpg", "FFD8FF"),

		/** PNG("png", "89504E47") */
		PNG("png", "89504E47"),

		/** "gif", "47494638" */
		GIF("gif", "47494638"),

		/** "tif", "49492A00" */
		TIF("tif", "49492A00"),

		/** Windows Bitmap "bmp", "424D" */
		BMP("bmp", "424D"),

		/** CAD "dwg", "41433130" */
		DWG("dwg", "41433130"),

		/** "html", "68746D6C3E" */
		HTML("html", "68746D6C3E"),

		/** Rich Text Format "rtf", "7B5C727466" */
		RTF("rtf", "7B5C727466"),

		/** "xml", "3C3F786D6C" */
		XML("xml", "3C3F786D6C"),

		/** "zip", "504B0304" */
		ZIP("zip", "504B0304"),

		/** "rar", "52617221" */
		RAR("rar", "52617221"),

		/** Photoshop "psd", "38425053" */
		PSD("psd", "38425053"),

		/** Email [thorough only] "eml", "44656C69766572792D646174653A" */
		EML("eml", "44656C69766572792D646174653A"),

		/** Outlook Express "dbx", "CFAD12FEC5FD746F" */
		DBX("dbx", "CFAD12FEC5FD746F"),

		/** Outlook "pst", "2142444E" */
		PST("pst", "2142444E"),

		/** MS Excel "xls", "D0CF11E0" MS Excel 注意：word 和 excel的文件头一样 */
		XLS("xls", "D0CF11E0"),

		/** MS Word "doc", "D0CF11E0" MS Word 注意：word 和 excel的文件头一样 */
		DOC("doc", "D0CF11E0"),

		/** MS Access "mdb", "5374616E64617264204A" */
		MDB("mdb", "5374616E64617264204A"),

		/** WordPerfect "wpd", "FF575043" */
		WPD("wpd", "FF575043"),

		/** "eps", "252150532D41646F6265" */
		EPS("eps", "252150532D41646F6265"),

		/** "ps", "252150532D41646F6265" */
		PS("ps", "252150532D41646F6265"),

		/** Adobe Acrobat "pdf", "255044462D312E" */
		PDF("pdf", "255044462D312E"),

		/** Quicken "qdf", "AC9EBD8F" */
		QDF("qdf", "AC9EBD8F"),

		/** Windows Password "pwl", "E3828596" */
		PWL("pwl", "E3828596"),

		/** Wave "wav", "57415645" */
		WAV("wav", "57415645"),

		/** "avi", "41564920" */
		AVI("avi", "41564920"),

		/** Real Audio "ram", "2E7261FD" */
		RAM("ram", "2E7261FD"),

		/** Real Media "rm", "2E524D46" */
		RM("rm", "2E524D46"),

		/** "mpg", "000001BA" */
		MPG("mpg", "000001BA"),

		/** Quicktime "mov", "6D6F6F76" */
		MOV("mov", "6D6F6F76"),

		/** Windows Media "asf", "3026B2758E66CF11" */
		ASF("asf", "3026B2758E66CF11"),

		/** MIDI (mid) */
		MID("mid", "4D546864");

		private String                       name;
		private String                       hexCode;
		private static Map<String, FileType> pool = new HashMap<String, FileType>();
		static {
			for (FileType ft : FileType.values()) {
				pool.put(ft.name, ft);
				FILE_TYPE_MAP.put(ft.name, ft.hexCode);
			}
		}

		private FileType(String name, String hexCode) {
			this.name = name;
			this.hexCode = hexCode;
		}

		public String getHexCode() {
			return hexCode;
		}

		public String getName() {
			return name;
		}
	}

	public final static Map<String, String> FILE_TYPE_MAP = new HashMap<String, String>();

	static final int                        BUFFER        = 2048;

	public static int batchCopy(List<String> sourceList, String dstdir, boolean overwrite) throws IOException {
		File dir = new File(dstdir);
		if (!dir.exists()) {
			FileUtil.mkdirsOfDirectory(dir);
		}
		int copySum = 0;
		for (String sourceFile : sourceList) {
			File file = new File(sourceFile);
			File dstFile = new File(dstdir + "/" + FileUtil.getName(file));
			if (!file.exists()) {
				continue;
			}
			if (dstFile.exists() && !overwrite) {
				continue;
			}
			if (FileUtil.copyFile(file, dstFile)) {
				copySum++;
			}
		}

		return copySum;
	}

	public static int batchCopy(List<String> fileList, String sourceDir, String dstDir, boolean overwrite) throws IOException {
		int copySum = 0;
		for (String file : fileList) {
			File sourceFile = new File(sourceDir + file);
			File dstFile = new File(dstDir + file);
			if (!sourceFile.exists()) {
				continue;
			}
			if (dstFile.exists() && !overwrite) {
				continue;
			}
			FileUtil.mkdirsOfFile(dstFile);
			if (FileUtil.copyFile(sourceFile, dstFile)) {
				copySum++;
			}
		}
		return copySum;
	}

	public static boolean copyFile(File sourceFile, File dstFile) throws IOException {
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			int length = 2097152;
			in = new FileInputStream(sourceFile);
			out = new FileOutputStream(dstFile);
			FileChannel inC = in.getChannel();
			FileChannel outC = out.getChannel();
			ByteBuffer b = null;
			while (true) {
				if (inC.position() == inC.size()) {
					inC.close();
					outC.close();
					return true;
				}
				if ((inC.size() - inC.position()) < length) {
					length = (int) (inC.size() - inC.position());
				} else {
					length = 2097152;
				}
				b = ByteBuffer.allocateDirect(length);
				inC.read(b);
				b.flip();
				outC.write(b);
				outC.force(false);
			}
		}
		finally {
			if (null != in) {
				in.close();
			}
			if (null != out) {
				out.close();
			}
		}
	}

	/**
	 * @see #createZipFile(File, new File(directory.toString()+".zip"))
	 */
	public static File createZipFile(File directory) throws IOException {
		return createZipFile(directory, new File(directory.toString() + ".zip"));
	}

	/**
	 * create ZIP compressed file.
	 * @param directory Be want to be compressed directory.
	 * @param zipFile Be appointed file which ZIP compressed.
	 * @return from parameter of zipFile.
	 * @throws IOException
	 */
	public static File createZipFile(File directory, File zipFile) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(zipFile));
		ZipOutputStream out = new ZipOutputStream(bos);

		put(directory, out, "");
		out.close();

		return zipFile;
	}

	/**
	 * @see #createZipFile(File, File)
	 */
	public static File createZipFile(String directory) throws IOException {
		return createZipFile(new File(directory));
	}

	/**
	 * @see #createZipFile(new File(directory),new File(zipFileNameNoSuffix + ".zip"))
	 */
	public static File createZipFile(String directory, String zipFileNameNoSuffix) throws IOException {
		return createZipFile(new File(directory), new File(zipFileNameNoSuffix + ".zip"));
	}

	/**
	 * 得到文件类型
	 * @param b
	 * @return
	 */
	public static String getFileType(byte[] b) {
		String filetypeHex = String.valueOf(ByteUtil.toHexString(b));
		Iterator<Entry<String, String>> entryiterator = FILE_TYPE_MAP.entrySet().iterator();
		while (entryiterator.hasNext()) {
			Entry<String, String> entry = entryiterator.next();
			String fileTypeHexValue = entry.getValue();
			if (filetypeHex.toUpperCase().startsWith(fileTypeHexValue)) {
				return entry.getKey();
			}
		}
		return null;
	}

	/**
	 * 获取文件类型,包括图片,若格式不是已配置的,则返回null
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String getFileType(File file) throws IOException {
		String filetype = null;
		byte[] b = new byte[50];
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			is.read(b);

			filetype = getFileType(b);
		}
		finally {
			if (null != is) {
				is.close();
			}
		}
		return filetype;
	}

	/**
	 * 得到文件名（包含后缀名）
	 * @param file File
	 * @return 文件名
	 */
	public static String getName(File file) {
		return file.getName();
	}

	/**
	 * 得到文件名
	 * @param file File
	 * @return 文件名
	 */
	public static String getNameNoSuffix(File file) {
		String fileName = getName(file);
		int indexSuffixFlag = fileName.lastIndexOf(".");
		return (indexSuffixFlag == -1) ? fileName : fileName.substring(0, indexSuffixFlag);
	}

	/**
	 * get file's step count parent(folder).
	 * @param file The giving-file.
	 * @param step step count.
	 * @return folder name.
	 */
	public static String getParent(File file, int step) {
		String folder = null;
		for (int i = 0; i < step; i++) {
			file = file.getParentFile();
		}
		folder = file.toString();
		String parent = file.getParent();
		if (parent == null) {
			return null;
		}
		char c = parent.charAt(parent.length() - 1);
		if (c == '\\' || c == '/') {
			return folder.substring(folder.indexOf(parent) + parent.length());
		} else {
			return folder.substring(folder.indexOf(parent) + parent.length() + 1);
		}
	}

	/**
	 * @see #getSuffix(String)
	 */
	public static String getSuffix(File file) {
		String fileName = getName(file);
		int lastIndex = fileName.lastIndexOf('.');
		if (-1 == lastIndex) {
			return null;
		} else {
			return fileName.substring(lastIndex);
		}
	}

	/**
	 * get file's suffix.
	 * @param file The file.
	 * @return suffix
	 */
	public static String getSuffix(String file) {
		if (null == file) {
			return null;
		}
		return getSuffix(new File(file));
	}

	/**
	 * 判断文件是否存在
	 * @param fileName
	 * @return
	 */
	public static boolean isFileExists(String fileName) {
		return (new File(fileName).exists());
	}

	/**
	 * Create a directory path recursive.
	 * @param directory The File's path.
	 */
	public static void mkdirsOfDirectory(File directory) {
		directory.mkdirs();
	}

	/**
	 * @see #mkdirsOfDirectory(File)
	 */
	public static void mkdirsOfDirectory(String directory) {
		mkdirsOfDirectory(new File(directory));
	}

	/**
	 * Create a file's directory path recursive.
	 * @param file The File.
	 */
	public static void mkdirsOfFile(File file) {
		file.getParentFile().mkdirs();
	}

	/**
	 * @see #mkdirsOfFile(File)
	 */
	public static void mkdirsOfFile(String file) {
		mkdirsOfFile(new File(file));
	}

	private static void put(File f, ZipOutputStream out, String dir) throws IOException {
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			dir = dir + (dir.length() == 0 ? "" : "/") + f.getName();
			for (File file : files) {
				put(file, out, dir);
			}
		} else {
			byte[] data = new byte[BUFFER];
			FileInputStream fi = new FileInputStream(f);
			BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);
			dir = dir.length() == 0 ? "" : dir + "/" + f.getName();
			ZipEntry entry = new ZipEntry(dir);
			out.putNextEntry(entry);
			int count;
			while ((count = origin.read(data, 0, BUFFER)) != -1) {
				out.write(data, 0, count);
			}
			origin.close();
		}
	}

}
