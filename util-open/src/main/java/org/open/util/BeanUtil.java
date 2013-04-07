package org.open.util;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Qin Zhipeng
 * @date 2010-09-21 17:01
 */
public class BeanUtil {

	private static final Log log = LogFactory.getLog(BeanUtil.class);

	// /**
	// * 对象比较函数
	// *
	// * @param fromObj 被比较对象
	// * @param fromClazz 被比较对象类型，假如有超类，需要得到超类的类型
	// * @param toObj 比较对象
	// * @param toClazz 比较对象类型，假如有超类，需要得到超类的类型
	// * @return
	// */
	// public static HashMap compareFields(Object fromObj, Class fromClazz, Object toObj, Class toClazz) {
	// Field[] fields = (fromClazz != null) ? fromClazz.getDeclaredFields() : fromObj.getClass().getDeclaredFields();
	// HashMap map = new HashMap();
	// for (int i = 0; i < fields.length; i++) {
	// Object value1 = invokeGetMethod(fields[i], fromObj);
	// if (value1 != null) {
	// Object value2 = invokeGetMethod(fields[i].getName(), toObj);
	// if (value2 != null) {
	// if (value2.equals(value1) == false) {
	// map.put(fields[i].getName(), "比对不一致");
	// }
	// }
	// }
	// }
	// return map;
	// }
	//
	// /**
	// * 对象克隆函数，假如被复制对象某属性的值不为null将被复制，一般用于页面表单的全部提交
	// *
	// * @param fromObj 被复制对象
	// * @param fromClazz 被复制对象类型，假如有超类，需要得到超类的类型
	// * @param toObj 复制对象
	// * @param toClazz 被复制对象类型，假如有超类，需要得到超类的类型
	// */
	// public static void copyFields(Object fromObj, Class fromClass, Object toObj) {
	// Field[] fields = (fromClass != null) ? fromClass.getDeclaredFields() : fromObj.getClass().getDeclaredFields();
	// for (int i = 0; i < fields.length; i++) {
	// // fromObj中get方法得到的值
	// Object value = invokeGetMethod(fields[i], fromObj);
	// if (value != null)
	// // 将值传给toObj对象
	// invokeSetMethod(fields[i], toObj, value);
	// }
	// }
	//
	// /**
	// * 对象组装sql语句的函数，当对象的属性不为null或者不为""时，将按照对象属性的类型组织sql语句
	// *
	// * @param fromObj
	// * @param fromClazz
	// * @return
	// */
	// public static String copyFieldsToSql(Object fromObj, Class fromClazz) {
	// String Sql = "";
	// Field[] fields = (fromClazz != null) ? fromClazz.getDeclaredFields() : fromObj.getClass().getDeclaredFields();
	// for (int i = 0; i < fields.length; i++) {
	// Object value = invokeGetMethod(fields[i], fromObj);
	// if (value != null && !value.equals("") && !value.equals("0.00")) {
	// if ("java.lang.Integer".equals(fields[i].getType().getName())) {
	// Sql += " and " + fields[i].getName() + " = " + value;
	// } else if (((String) value).charAt(0) > 128) {
	// Sql += " and " + fields[i].getName() + " like '%" + value + "%'";
	// } else {
	// Sql += " and " + fields[i].getName() + " = '" + value + "'";
	// }
	//
	// }
	// }
	// return Sql;
	// }
	//
	// /**
	// * 对象克隆函数，假如被复制对象某属性的值不为null或者不为""将被复制，一般用于查询时对象拷贝
	// *
	// * @param fromObj 被复制对象
	// * @param fromClazz 被复制对象类型，假如有超类，需要得到超类的类型
	// * @param toObj 复制对象
	// * @param toClazz 被复制对象类型，假如有超类，需要得到超类的类型
	// */
	// public static void copyFieldsValues(Object fromObj, Class fromClazz, Object toObj, Class toClazz) {
	// Field[] fields = (fromClazz != null) ? fromClazz.getDeclaredFields() : fromObj.getClass().getDeclaredFields();
	// for (int i = 0; i < fields.length; i++) {
	// Object value = invokeGetMethod(fields[i], fromObj);
	// if (value != null && !value.equals("") && !value.equals("0.00")) invokeSetMethod(fields[i], toObj, value);
	// }
	// }
	//
	// public static Object invokeGetMethod(Field field, Object fromObj) {
	// Object[] in = new Object[1];
	// String fieldName = field.getName();
	// try {
	// StringBuffer buf = new StringBuffer();
	// buf.append("get");
	// buf.append(fieldName.substring(0, 1).toUpperCase());
	// buf.append(fieldName.substring(1));
	// String methodName = buf.toString();
	// // 返回由methodName指定的方法method
	// Method method = fromObj.getClass().getMethod(methodName, null);
	// // 执行fromObj的method方法
	// in[0] = method.invoke(fromObj, null);
	// } catch (Exception e1) {
	// log.error("调用" + fieldName + "属性对应的Get方法失败");
	// }
	// // 返回get方法的结果
	// return in[0];
	// }
	//
	// public static Object invokeGetMethod(String fieldName, Object fromObj) {
	// Object[] in = new Object[1];
	// try {
	// StringBuffer buf = new StringBuffer();
	// buf.append("get");
	// buf.append(fieldName.substring(0, 1).toUpperCase());
	// buf.append(fieldName.substring(1));
	// String methodName = buf.toString();
	// Method method = fromObj.getClass().getMethod(methodName, null);
	// in[0] = method.invoke(fromObj, null);
	// } catch (Exception e1) {
	// log.error("调用" + fieldName + "属性对应的Get方法失败");
	// }
	// return in[0];
	// }
	//
	// public static void invokeSetMethod(Field field, Object toObj, Object value) {
	// try {
	// String fieldName = field.getName();
	// StringBuffer buf = new StringBuffer();
	// buf.append("set");
	// buf.append(fieldName.substring(0, 1).toUpperCase());
	// buf.append(fieldName.substring(1));
	// String methodName = buf.toString();
	// Class[] pm = new Class[1];
	// pm[0] = field.getType();
	// Object[] in = new Object[1];
	// in[0] = value;
	// Method method = toObj.getClass().getMethod(methodName, pm);
	// method.invoke(toObj, in);
	// } catch (Exception e1) {
	// log.error("调用" + field.getName() + "属性对应的Set方法失败");
	// }
	// }
	//
	// /**
	// * 对象属性打印
	// *
	// * @param obj
	// * @param clazz
	// */
	// public static void printFields(Object obj, Class clazz) {
	// Field[] fields = (clazz != null) ? clazz.getDeclaredFields() : obj.getClass().getDeclaredFields();
	// log.debug("======打印" + obj.getClass().getName() + "信息======");
	// String logStr = "";
	// for (int i = 0; i < fields.length; i++) {
	// Object value = invokeGetMethod(fields[i], obj);
	// if (value != null) {
	// // log.debug(fields[i].getName().trim()+":"+value.toString());
	// logStr += fields[i].getName().trim() + ":" + value.toString();
	// }
	// }
	// log.debug(logStr);
	// log.debug("======打印完成======");
	// }
	//
	// /**
	// * 对象属性全部trim操作
	// *
	// * @param obj
	// * @param clazz
	// * @return
	// * @throws AppException
	// */
	// public static Object trimFields(Object obj, Class clazz) {
	// Field[] fields = (clazz != null) ? clazz.getDeclaredFields() : obj.getClass().getDeclaredFields();
	// for (int i = 0; i < fields.length; i++) {
	// Object value = invokeGetMethod(fields[i], obj);
	// if (value != null && fields[i].getType().getName().equals("java.lang.String")) {
	// Object val = value.toString().trim();
	// invokeSetMethod(fields[i], obj, val);
	// }
	// }
	// return obj;
	// }

	/**
	 * get method name.
	 * @param e
	 * @return method name.
	 */
	public static String getMethodName(Throwable e) {
		if (null == e) {
			return null;
		}
		StackTraceElement stack[] = e.getStackTrace();
		if (stack.length > 1) {
			return stack[0].getMethodName();
		} else {
			return null;
		}
	}

	/**
	 * @see PropertyUtilsBean#getProperty(Object, String)
	 * @param array
	 * @param name
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List getProperty(Collection<?> array, String name) {
		try {
			PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
			List data = new ArrayList(array.size());
			for (Object each : array) {
				data.add(propertyUtilsBean.getProperty(each, name));
			}
			return data;
		}
		catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);

			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e) {
			log.error(e.getMessage(), e);

			throw new RuntimeException(e);
		}
		catch (NoSuchMethodException e) {
			log.error(e.getMessage(), e);

			throw new RuntimeException(e);
		}
	}
}
