package org.open.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ObjectUtil {

    protected final static Log log = LogFactory.getLog(ObjectUtil.class);

    public static String object2String(Object obj) {
        String objString = null;
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;

        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);

            oos.writeObject(obj);

            // objString = new String(baos.toByteArray());
            objString = CodeUtil.BASE64Encoder(baos.toByteArray());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (null != oos) {
                    oos.close();
                }
                if (null != baos) {
                    baos.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }

        return objString;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T string2object(String objString, Class<T> clazz) {
        ByteArrayInputStream bais = null;
        BufferedInputStream bis = null;
        ObjectInputStream ois = null;
        T obj = null;

        try {
            // bais = new ByteArrayInputStream(objString.getBytes());
            bais = new ByteArrayInputStream(CodeUtil.BASE64Decoder(objString));
            bis = new BufferedInputStream(bais);
            ois = new ObjectInputStream(bis);

            obj = (T) ois.readObject();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (null != ois) {
                    ois.close();
                }
                if (null != bis) {
                    bis.close();
                }
                if (null != bais) {
                    bais.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }

        return obj;
    }

}
