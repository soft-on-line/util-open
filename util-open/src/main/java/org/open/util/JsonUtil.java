package org.open.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.internal.StringMap;

/**
 * json工具包类
 * @author zhipeng.qzp
 */
public class JsonUtil {

	private static Gson gson = new Gson();

	/**
	 * 得到一个json串的所有键值对
	 * @param json
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Object> toMap(String json) {
		return _toMap((StringMap) gson.fromJson(json, Object.class));
	}

	@SuppressWarnings("rawtypes")
	private static Map<String, Object> _toMap(StringMap gsonStringMap) {
		Map<String, Object> pool = new HashMap<String, Object>();

		Iterator keys = gsonStringMap.keySet().iterator();
		while (keys.hasNext()) {
			Object key = keys.next();
			Object value = gsonStringMap.get(key);
			pool.put(key.toString(), value);
			if (value instanceof ArrayList) {
				ArrayList list = (ArrayList) value;
				for (Object each : list) {
					if (each instanceof StringMap) {
						pool.putAll(_toMap((StringMap) each));
					}
				}
			} else if (value instanceof StringMap) {
				pool.putAll(_toMap((StringMap) value));
			}
		}

		return pool;
	}

}
