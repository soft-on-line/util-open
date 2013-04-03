package org.open.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.open.util.exception.OpenUtilException;

/**
 * 写文件内容的类
 *
 * @author 覃芝鹏
 * @version $Id: WriterUtil.java,v 1.13 2009/05/09 17:37:02 moon Exp $
 */
public class WriterUtil {

    // 写日志.
    private static final Log log = LogFactory.getLog(WriterUtil.class);

    /**
     * @param fileName 文件名字
     * @return 删除结果
     */
    public static boolean delete(String fileName) {
        return new File(fileName).delete();
    }

    /**
     * 创建File路径
     *
     * @param file File
     * @deprecated
     */
    // public static void mkdirs(File file) {
    // if (file.getParent() != null) {
    // file = new File(file.getParent());
    // mkdirs(file);
    // file.mkdir();
    // }
    // }

    /**
     * byte[]字节流写文件
     *
     * @param fileName 文件名
     * @param bytes byte[]
     */
    public static void write(File fileName, byte[] bytes) {
        FileOutputStream fos = null;
        try {
            // build path.
            FileUtil.mkdirsOfFile(fileName);
            fos = new FileOutputStream(fileName);

            fos.write(bytes);
        } catch (Exception e) {
            log.error("Method write(" + fileName + ",byte[] ...", e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    log.error("Method write(" + fileName + ",byte[] ...", e);
                }
            }
        }
    }

    /**
     * byte[]字节流写文件
     *
     * @param fileName 文件名
     * @param bytes byte[]
     */
    public static void write(String fileName, byte[] bytes) {
        FileOutputStream fos = null;
        try {
            FileUtil.mkdirsOfFile(fileName);
            fos = new FileOutputStream(fileName);

            fos.write(bytes);
        } catch (Exception e) {
            log.error("Method write(" + fileName + ",byte[] ...", e);

            throw new RuntimeException(e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    log.error("Method write(" + fileName + ",byte[] ...", e);
                }
            }
        }
    }

    public static void writeFile(File file, String content, String charsetName) {
        try {
            writeFile(file, content, charsetName, false);
        } catch (Exception e) {
            throw new OpenUtilException(e);
        }
    }

    /**
     * 写一个 自己指定 后缀名的 文件
     *
     * @param file 文件名
     * @param content 文件内容
     * @param charset 写文件内容编码
     * @param append true当文件存在则追加，false全新写文件
     */
    public static void writeFile(File file, String content, String charsetName, boolean append) {
        BufferedWriter out = null;
        try {
            // 先创建父级目录
            FileUtil.mkdirsOfFile(file);

            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), charsetName));
            out.write(content);
        } catch (Exception e) {
            log.error("WriterFile writeFile(" + file + ") error!", e);
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (Exception e) {
                log.error("WriterFile writeHtml(" + file + ") error!", e);
            }
        }
    }

    /**
     * 强制写一个 自己指定 后缀名的 文件
     *
     * @param file 文件名
     * @param content 文件内容
     */
    public static void writeFile(String file, String content) {
        writeFile(file, content, false);
    }

    /**
     * 写一个 自己指定 后缀名的 文件
     *
     * @param file 文件名
     * @param content 文件内容
     * @param append true当文件存在则追加，false全新写文件
     */
    public static void writeFile(String file, String content, boolean append) {
        writeFile(new File(file), content, "gbk", append);
    }

    // public static void writeFile(String file,String content,boolean append)
    // {
    // FileWriter fw = null;
    // BufferedWriter bw = null;
    //
    // try {
    // mkdirs(new File(file));
    //
    // fw = new FileWriter(file, append);
    // bw = new BufferedWriter(fw);
    //
    // bw.write(content);
    //
    // log.debug("WriterFile writeFile("+file+","+content+")");
    //
    // } catch (Exception e) {
    // log.error("WriterFile writeHtml("+file+") error!",e);
    // }finally{
    // try{
    // if(bw != null){
    // bw.close();
    // }
    //
    // if(fw != null){
    // fw.close();
    // }
    // }catch(Exception e){
    // log.error("WriterFile writeHtml("+file+") error!",e);
    // }
    // }
    // }

    /**
     * 强制写一个 自己指定 后缀名的 文件
     *
     * @param file 文件名
     * @param content 文件内容
     * @param charsetName 字符编码
     */
    public static void writeFile(String file, String content, String charsetName) {
        try {
            writeFile(new File(file), content, charsetName, false);
        } catch (Exception e) {
            throw new OpenUtilException(e);
        }
    }

    // /**
    // * 写一个 html 后缀名的 文件
    // * @param file 文件名
    // * @param content 文件内容
    // * @deprecated
    // */
    // public static void writeHtml(String file, String content) {
    // writeFile(file + ".html", content);
    // }

    // /**
    // * 全新强制写一个'.js'后缀名的文件,默认编码UTF-8.
    // * @param fileName 文件名
    // * @param content 文件内容
    // * @deprecated
    // */
    // public static void writeJavaScript(String fileName, String content) {
    // writeFile(fileName + ".js", content, "utf-8");
    // }

}
