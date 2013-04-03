package org.open.util;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.open.KeyValue;

/**
 * java语言操作符重载，数组相加，等与操作符相关的功能
 * @author 覃芝鹏
 * @version $Id: OperationUtil.java,v 1.16 2008/09/11 02:03:19 moon Exp $
 */
public class OperationUtil
{
	/**
	 * 2组<T>对象数组相加
	 * @param <T> 需要操作的底层类对象
	 * @param a 原始数组a
	 * @param b 原始数组b
	 * @return 2组 原始数组 相加后 得到的新的数组
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] add(T[] a,T[] b) 
	{
		T[] tmp  = (T[])Array.newInstance(a.getClass().getComponentType(), a.length+b.length);
		System.arraycopy(a, 0, tmp, 0, a.length);
		System.arraycopy(b, 0, tmp, a.length, b.length);
		return tmp;
	}
	
	/**
	 * 对象数组 与 对象相加
	 * @param <T> 需要操作的底层类对象
	 * @param a 原始数组
	 * @param b 相加对象
	 * @return 相加后的数组
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] addAfter(T[] a,T b)
	{
		if(a==null && b==null){
			return null;
		}
		if(a==null){
			T[] tmp  = (T[])Array.newInstance(b.getClass(), 1);
			tmp[0] = b;
			return tmp;
		}
		T[] tmp  = (T[])Array.newInstance(a.getClass().getComponentType(), a.length+1);
		System.arraycopy(a, 0, tmp, 0, a.length);
		tmp[a.length] = b;
		return tmp;
	}
	
	/**
	 * 对象数组 与 对象相加
	 * @param <T> 需要操作的底层类对象
	 * @param a 原始数组
	 * @param b 相加对象
	 * @return 相加后的数组
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] addBefore(T[] a,T b)
	{
		T[] tmp  = (T[])Array.newInstance(a.getClass().getComponentType(), a.length+1);
		tmp[0] = b;
		System.arraycopy(a, 0, tmp, 1, a.length);
		return tmp;
	}
	
	/**
	 * 去重 对象数组
	 * @param <T> 需要操作的底层类对象
	 * @param t T对象数组
	 * @return 去重后的T对象数组
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] deDuplication(T[] t)
	{
		Set<T> set = new HashSet<T>();
		for(int i=0;i<t.length;i++)
		{
			if(set.contains(t[i])){
				continue;
			}else{
				set.add(t[i]);
			}
		}
		T[] tmp  = (T[])Array.newInstance(t.getClass().getComponentType(), set.size());
		return set.toArray(tmp);
	}
	
	/**
	 * 对象数组删除指定index下标上的值。
	 * 如果index在正常范围内，则数组长度减1，删除成功；否则返回原数组。
	 * @param <T> 需要操作的底层类对象
	 * @param a 原始数组
	 * @param index 下标
	 * @return 删除指定index下标值后的数组
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] del(T[] a,int index)
	{
		//非法条件直接跳出，不做任何处理。
		if(a==null || a.length<1 || index<0 || index>=a.length){
			return a;
		}
		T[] tmp  = (T[])Array.newInstance(a.getClass().getComponentType(), a.length-1);
		System.arraycopy(a, 0, tmp, 0, index);
		System.arraycopy(a, index+1, tmp, index, a.length-index-1);
		return tmp;
	}
	
	/**
	 * 查看2个集合类是否含有交集
	 * @param <T> 需要操作的底层类对象
	 * @param c1 Collection<T>队列1
	 * @param c2 Collection<T>队列2
	 * @return true有交集，false没有交集
	 */
	public static <T> boolean isIntersect(Collection<T> c1,Collection<T> c2)
	{
		if(c1==null || c2==null){
			return false;
		}
		//取最小的做循环集合
		if(c1.size()>c2.size()){
			Collection<T> tmp = c1;
			c1 = c2;
			c2 = tmp;
		}
		Iterator<T> elements = c1.iterator();
		while(elements.hasNext()){
			T t = elements.next();
			if(c2.contains(t)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 查看2个集合类是否含有交集
	 * @param <T> 需要操作的底层类对象
	 * @param c1 Collection<T>队列1
	 * @param c2 Collection<T>队列2
	 * @return true有交集，false没有交集
	 */
	public static <T> boolean isIntersect(T[] c1,Collection<T> c2)
	{
		if(c1==null || c2==null){
			return false;
		}
		for(int i=0;i<c1.length;i++)
		{
			if(c2.contains(c1[i])){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 未经过测试方法体
	 * @param obj
	 * @param times
	 * @param methodName
	 * @param args_a
	 * @param args_n
	 * @return
	 * @deprecated
	 */
	public static Object methodInvoke(Object obj,int times,String methodName,Object args_a, Object... args_n)
	{
		try{
			Class<?>[] pm = new Class[args_n.length+1];
			pm[0] = args_a.getClass();
			for(int i=1;i<pm.length;i++){
				pm[i] = args_n[i-1].getClass();
			}
			
			Method method = obj.getClass().getMethod(methodName,pm);
			for(int i=0;i<times-1;i++){
				method.invoke(args_a, args_n);
			}
			return method.invoke(args_a, args_n);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 反转List
	 * @param list List
	 * @return 反转后的List
	 */
	public static List<?> reverse(List<?> list)
	{
		Collections.reverse(list);
		return list;
	}
	
	/**
	 * 把一组对象递减排序后得到新的下标队列
	 * @param <T> 需要操作的底层类对象
	 * @param data 需要排序对象
	 * @return 递减排序后下标队列
	 */
	public static <T> List<Integer> sort(T[] data)
	{
		KeyValue<Integer,T> kv = new KeyValue<Integer,T>();
		for(int i=0;i<data.length;i++)
		{
			kv.put(Integer.valueOf(i), data[i]);
		}
		return kv.getKeysOrderByValuesAsc();
	}
	
	/**
	 * 返回一个数组的子队列
	 * @param <T> 需要操作的底层类对象
	 * @param array T对象数组
	 * @param begin 开始点
	 * @param end 结束点
	 * @return 子数组队列
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] sub(T[] array,int fromIndex,int toIndex)
	{
		if(fromIndex<0 || toIndex<0 || fromIndex>array.length || toIndex>array.length){
			throw new IndexOutOfBoundsException("fromIndex = "+fromIndex+" toIndex = "+toIndex);
		}
		if(fromIndex > toIndex){
			throw new IllegalArgumentException("fromIndex("+fromIndex+") > toIndex("+toIndex+")");
		}
		T[] tmp = (T[])Array.newInstance(array.getClass().getComponentType(), toIndex-fromIndex);
		for(int i=0;i<tmp.length;i++){
			tmp[i] = array[fromIndex+i];
		}
		return tmp;
	}
	
	/**
	 * double对象数组转为List<Double>
	 * @param obj 原始double数组
	 * @return List<Double>
	 */
	public static List<Double> toList(double[] obj)
	{
		List<Double> list = new ArrayList<Double>();
		for(int i=0;i<obj.length;i++){
			list.add(i,obj[i]);
		}
		return list;
	}
	
	/**
	 * T对象数组转为List<T>
	 * @param <T> 需要操作的底层类对象
	 * @param obj 原始<T>数组
	 * @return List<T>
	 */
	public static <T> List<T> toList(T[] obj)
	{
		List<T> list = new ArrayList<T>();
		for(int i=0;i<obj.length;i++){
			list.add(i,obj[i]);
		}
		return list;
	}
	
	/**
	 * 截取的数组
	 * @param <T> 需要操作的底层类对象
	 * @param t T对象数组
	 * @param topAmount 前topAmount对象
	 * @return 返回截取的数组
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] top(T[] t,int topAmount)
	{
		if(topAmount>t.length){
			return t;
		}
		if(topAmount<0){
			topAmount = 0;
		}
		T[] tmp = (T[])Array.newInstance(t.getClass().getComponentType(), topAmount);
		for(int i=0;i<tmp.length;i++) {
			tmp[i] = t[i];
		}
		return tmp;
	}
	
	/**
	 * 整合后面的队列至第一个数组。
	 * @param <T>
	 * @param target
	 * @param source
	 * @return 整合成功个数
	 */
	public static <T> int merge(List<T> target,List<T>... source)
	{
		int count = 0;
		for(List<T> s : source)
		{
			for(T t : s)
			{
				if(!target.contains(t)){
					target.add(t);
					count ++;
				}
			}
		}
		return count;
	}
	
}
