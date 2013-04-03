package org.open.util;

import org.open.util.UnitUtil.MemUnit;

public class FormatUtil 
{
	public static String formatMeminfo(long mem)
	{
		return formatMeminfo(mem,MemUnit.M);
	}
	
	public static String formatMeminfo(long mem,MemUnit mu) 
	{
		switch(mu)
		{
			case b:
				return (mem)+"byte.";
			case K:
				return (mem)/1024+"K.";
			case M:
				return (mem)/1024/1024+"M.";
			case G:
				return (mem)/1024/1024/1024+"G.";
			default:
				return ""+mem;
		}
	}
}
