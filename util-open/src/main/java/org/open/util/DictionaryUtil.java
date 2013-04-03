package org.open.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 各种常量字典工具包类，包括世界国家列表、中国省份及其城市
 * 
 * @author ibm
 * @version $Id:$
 */
public final class DictionaryUtil {

    /**
     * @see org.apache.commons.logging.Log
     */
    private static final Log                   log              = LogFactory.getLog(DictionaryUtil.class);
    private static Map<Integer, CountryVO>     countryMaps      = new HashMap<Integer, CountryVO>();
    private static Map<Integer, String>        privinceMaps     = new HashMap<Integer, String>();
    private static Map<Integer, CityVO>        cityMaps         = new HashMap<Integer, CityVO>();
    private static Map<Integer, List<Integer>> privinceCityMaps = new HashMap<Integer, List<Integer>>();
    private static Map<String, MobileVO>       mobileMaps       = new HashMap<String, MobileVO>();
    private static Map<String, String>         phoneMaps        = new HashMap<String, String>();

    /**
     * 地区名 和 电话区号 键值库
     */
    private static Properties                  REGION_LIB       = new Properties();

    public static DictionaryUtil               instance         = new DictionaryUtil();

    private DictionaryUtil() {
        try {
            // loading countries.
            Document countyDoc = new SAXReader().read(this.getClass().getResourceAsStream("countries.xml"));

            List<?> countyList = countyDoc.getRootElement().elements("country");

            for (int i = 0; i < countyList.size(); i++) {
                Element countyEl = (Element) countyList.get(i);
                CountryVO vo = new CountryVO();
                Integer key = Integer.parseInt(countyEl.attributeValue("code"));
                vo.setCountryCode(key);
                vo.setCnName(countyEl.attributeValue("cn"));
                vo.setEnName(countyEl.attributeValue("en"));
                vo.setDomain(countyEl.attributeValue("domain"));
                countryMaps.put(key, vo);
            }

            log.info("加载国家名称数据成功!");

            // loading province and city.
            Document provinceCityDoc = new SAXReader().read(this.getClass().getResourceAsStream("province-city.xml"));
            Element root = provinceCityDoc.getRootElement();
            List<?> l = root.elements("province");
            for (int i = 0; i < l.size(); i++) {
                Element province = (Element) l.get(i);
                int key = Integer.parseInt(province.attributeValue("key"));
                String name = province.attributeValue("value");
                privinceMaps.put(key, name);
                List<?> cities = province.elements("city");
                List<Integer> list = new ArrayList<Integer>();
                for (int j = 0; j < cities.size(); j++) {
                    Element city = (Element) cities.get(j);
                    CityVO vo = new CityVO();
                    int cityKey = Integer.parseInt(city.attributeValue("key"));
                    String cityName = city.attributeValue("value");
                    vo.setCityName(cityName);
                    vo.setProvinceId(key);
                    cityMaps.put(cityKey, vo);
                    list.add(cityKey);
                }
                privinceCityMaps.put(key, list);
            }

            log.info("加载中国省份与城市数据成功!");

            // loading mobile data.
            Document mobileDoc = new SAXReader().read(this.getClass().getResourceAsStream("mobile.xml"));
            Element mobiles = mobileDoc.getRootElement();
            List<?> listMobile = mobiles.elements("mobile");
            for (int i = 0; i < listMobile.size(); i++) {
                MobileVO mobileVO = new MobileVO();

                Element mobile = (Element) listMobile.get(i);
                String mobileId = mobile.attributeValue("key");
                mobileVO.setMobileId(mobileId);

                Element area = mobile.element("area");
                String areaCode = area.attributeValue("code");
                mobileVO.setAreaCode(areaCode);

                Element city = mobile.element("city");
                Object cityData = city.getData();
                mobileVO.setCity(cityData.toString());

                Element gsm = mobile.element("gsm");
                Object gsmData = gsm.getData();
                mobileVO.setGsm(gsmData.toString());

                mobileMaps.put(mobileId, mobileVO);
            }

            log.info("加载移动通信及其座机区号数据成功!");

            // loading area code library.
            REGION_LIB.load(DictionaryUtil.class.getResourceAsStream("region.lib"));

            // loading phoneMaps.
            Iterator<?> keys = REGION_LIB.keySet().iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                String value = (String) REGION_LIB.get(key);
                phoneMaps.put(value, key);
            }

        } catch (Exception e) {
            log.error(e);
        }
    }

    public static class CountryVO {

        private Integer countryCode;
        private String  cnName;
        private String  enName;
        private String  domain;

        public Integer getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(Integer countryCode) {
            this.countryCode = countryCode;
        }

        public String getCnName() {
            return cnName;
        }

        public void setCnName(String cnName) {
            this.cnName = cnName;
        }

        public String getEnName() {
            return enName;
        }

        public void setEnName(String enName) {
            this.enName = enName;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }
    }

    public static class CityVO {

        private Integer provinceId;
        private String  cityName;

        public Integer getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(Integer provinceId) {
            this.provinceId = provinceId;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }
    }

    public static class MobileVO {

        private String mobileId;
        private String areaCode;
        private String city;
        private String gsm;

        public String getMobileId() {
            return mobileId;
        }

        public void setMobileId(String mobileId) {
            this.mobileId = mobileId;
        }

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getGsm() {
            return gsm;
        }

        public void setGsm(String gsm) {
            this.gsm = gsm;
        }
    }

    public Collection<CityVO> getCitys() {
        return cityMaps.values();
    }

    /**
     * @param subMobile
     * @return
     */
    public MobileVO getMobile(String subMobile) {
        return mobileMaps.get(subMobile);
    }

    /**
     * 把各种包含电话号码信息的字符串 转换 出 具有修正格式 和 补充区号的完整电话号码
     * 
     * @param phone 原始电话号码串
     * @param info 地区信息
     * @return 修正格式 和 电话号码区号的 电话号码串（如:0571-28970369）
     */
    public String phone(String phone, String info) {
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

    public String getPhone(String areaCode) {
        System.out.println(phoneMaps.keySet());
        System.out.println(phoneMaps.values());

        return phoneMaps.get(areaCode);
    }

    // /**
    // *
    // * @param countryId 国家
    // * @param provinceId 省份
    // * @param cityId 城市
    // * @return
    // */
    // public String[] getRegion(int countryId, int provinceId, int cityId)
    // {
    // String[] regions = new String[] { "", "", "" };
    // if (ParameterUtil.isInteger(countryId)) {
    // CountryVO cvo = getCountry(Integer.parseInt(countryId));
    // if (cvo != null) {
    // regions[0] = cvo.getCnName();
    // }
    // }
    // if ("86".equals(countryId)) {
    // if (provinceId != null) {
    // String p = getProvinceName(provinceId.intValue());
    // if (p != null) {
    // regions[1] = p;
    // if (cityId != null) {
    // String c = getCityName(cityId.intValue());
    // if (c != null)
    // regions[2] = c;
    // }
    // }
    // }
    // }
    // return regions;
    // }
    //
    // /**
    // * 获取国家数据
    // *
    // * @param countryCode
    // * 国家区号代码
    // * @return
    // */
    // public CountryVO getCountry(int countryCode) {
    // if (countryCode < 0)
    // return null;
    // return countryMaps.get(countryCode);
    // }
    //
    // /**
    // * 获取省份名称
    // *
    // * @param provinceId
    // * 省份ID
    // * @return
    // */
    // public String getProvinceName(int provinceId) {
    // if (provinceId < 0)
    // return null;
    // return privinceMaps.get(provinceId);
    // }
    //
    // /**
    // * 获取城市名称
    // *
    // * @param cityId
    // * 城市ID
    // * @return
    // */
    // public String getCityName(int cityId) {
    // if (cityId < 0)
    // return null;
    // CityVO vo = cityMaps.get(cityId);
    // String name = null;
    // if (vo != null) {
    // name = vo.getCityName();
    // }
    // return name;
    //    }
    //
    //    /**
    //     * 获取省份下的所有城市的ID列表
    //     * 
    //     * @param provinceId
    //     * @return
    //     */
    //    public  List<Integer> getCitiesByProvinceId(int provinceId) {
    //
    //        return privinceCityMaps.get(provinceId);
    //    }
    //
    //    /**
    //     * 获取城市所属省份ID
    //     * 
    //     * @param cityId
    //     * @return
    //     */
    //    public  Integer getProvinceIdByCityId(int cityId) {
    //        CityVO vo = cityMaps.get(cityId);
    //        Integer code = null;
    //        if (vo != null) {
    //            code = vo.getProvinceId();
    //        }
    //        return code;
    //    }
    //
    //    /**
    //     * 获取城市所属省份ID
    //     * 
    //     * @param cityId
    //     * @return
    //     */
    //    public  String getProvinceNameByCityId(int cityId) {
    //        if (cityId < 0)
    //            return null;
    //        Integer code = getProvinceIdByCityId(cityId);
    //        String provinceName = null;
    //        if (code != null) {
    //            provinceName = getProvinceName(code);
    //        }
    //        return provinceName;
    //    }

}
