package org.open.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 电话号码匹配和处理 工具包类
 * 
 * @author 覃芝鹏
 * @version $Id: PhoneUtil.java,v 1.2 2008/05/28 01:49:31 moon Exp $
 */
public class PhoneUtil {

    private static final Log  log        = LogFactory.getLog(PhoneUtil.class);

    /**
     * 地区名 和 电话区号 键值库
     */
    private static Properties REGION_LIB = new Properties();

    static {
        new PhoneUtil();
    }

    private PhoneUtil() {
        try {
            REGION_LIB.load(PhoneUtil.class.getResourceAsStream("region.lib"));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 把各种包含电话号码信息的字符串 转换 出 具有修正格式 和 补充区号的完整电话号码
     * 
     * @param phone 原始电话号码串
     * @param info 地区信息
     * @return 修正格式 和 电话号码区号的 电话号码串（如:0571-28970369）
     */
    public static String phone(String phone, String info) {
        if (info == null) {
            return RegexpUtil.phoneMatch(phone);
        }

        Iterator<?> e = REGION_LIB.keySet().iterator();

        String _phone = RegexpUtil.phoneMatch(phone);

        if (_phone == null) {
            return null;
        }

        if (-1 == _phone.indexOf('-')) {
            while (e.hasNext()) {
                String regionCNName = (String) e.next();

                if (info.contains(regionCNName)) {
                    String regionNo = (String) REGION_LIB.get(regionCNName);
                    return regionNo.trim() + "-" + _phone;
                }
            }
        }

        return _phone;
    }

}
