/**
 * 
 */
package com.kingbase.db.core.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author duke
 *
 */
public class DateUtil {

	private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>();
	public static final String dateFormat = "yyyy/MM/dd HH:mm:ss";
	public static final String dateFormat1 = "yyyy_MM_dd_HH_mm_ss";
	
	public static String getDateTime() {
		threadLocal.set(new SimpleDateFormat(dateFormat));
		return threadLocal.get().format(new Date());
	}

	public static Date parse(String dateStr) throws ParseException {
		return threadLocal.get().parse(dateStr);
	}

	public static String getDateTime1() {
		threadLocal.set(new SimpleDateFormat(dateFormat1));
		return threadLocal.get().format(new Date());
	}

}
