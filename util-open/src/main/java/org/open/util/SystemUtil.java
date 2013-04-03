package org.open.util;

import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;

public class SystemUtil
{
	private static final Runtime runtime = Runtime.getRuntime();

	/**
	 * get File separator.
	 */
	public static String getFileSeparator()
	{
		return System.getProperty("file.separator");
	}

	/**
	 * get temporary directory of current system.
	 */
	public static String getTmpdir()
	{
		return System.getProperty("java.io.tmpdir");
	}
	
	public static long memoryFree()
	{
		//内存垃圾清理
		runGC();
		return runtime.freeMemory();
	}
	
	public static long memoryMax()
	{
		//内存垃圾清理
		runGC();
		return runtime.maxMemory();
	}
	
	public static long memoryTotal()
	{
		//内存垃圾清理
		runGC();
		return runtime.totalMemory();
	}
	
	public static long memoryUsed() 
	{
		return runtime.totalMemory() - runtime.freeMemory();
	}
	
	public static String physicalInfo() 
    {
    	OperatingSystemMXBean osmb = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
        return "系统物理内存总计：" + osmb.getTotalPhysicalMemorySize() / 1024/1024 + "MB"
         	+ "系统物理可用内存总计：" + osmb.getFreePhysicalMemorySize() / 1024/1024 + "MB";
    }
	
	public static void runGC() 
	{
		long usedMem1 = memoryUsed(), usedMem2 = Long.MAX_VALUE;
		for (int i = 0; (usedMem1 < usedMem2) && (i < 500); ++i) {
			runtime.runFinalization();
			runtime.gc();
			usedMem2 = usedMem1;
			usedMem1 = memoryUsed();
		}
	}
}
