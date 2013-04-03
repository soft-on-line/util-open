package org.open.tools;

import java.io.File;
import java.text.MessageFormat;

import org.open.util.CodeUtil;
import org.open.util.DateUtil;
import org.open.util.ReaderUtil;
import org.open.util.WriterUtil;

public class BatchGenerateSerialVersionUID {

    private static String SerialVersionUID_TEMPLATE = "\t/**\r\n\t * generate by www.soft-on-line.com {0}\r\n\t */\r\n\tprivate static final long serialVersionUID = {1}L;";

    public static void addSerialVersionUID(String file) {

        if (-1 == file.lastIndexOf(".java")) {
            return;
        }
        
        String javaCode = ReaderUtil.read(file);
        int index = javaCode.indexOf("java.io.Serializable");
        //if ((-1==index) || (javaCode.matches("[.|\\s]*private[.|\\s]*static[.|\\s]*final[.|\\s]*long[.|\\s]*serialVersionUID[.|\\s]*"))) {
        if ((-1==index) || (javaCode.contains("private static final long serialVersionUID"))) {
            return;
        } else {
            WriterUtil.writeFile(file, javaCode.replaceFirst("java\\.io\\.Serializable\\s*?\\{",
                                                                       "java.io.Serializable {\r\n\r\n"
                                                                               + MessageFormat.format(SerialVersionUID_TEMPLATE, DateUtil.getDefault(),String.valueOf(CodeUtil.md5string2long(javaCode)))));
        }
    }
    
    public static void addSerialVersionUID(File file)
    {
        if(file.isFile()){
            addSerialVersionUID(file.toString());
        }else{
            File[] files = file.listFiles();
            for(File f : files){
                addSerialVersionUID(f);
            }
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        addSerialVersionUID(new File("D:/workspace/eclipse/Moon/logodict/src/main/java/com/logodict/hibernate3/dao"));
        
    }

}
