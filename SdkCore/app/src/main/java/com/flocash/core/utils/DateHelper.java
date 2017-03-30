package com.flocash.core.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 
 * @author HieuLT
 *
 */
public class DateHelper {
	
	public static String getFormattedDate(Date date, String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		return sdf.format(date);
	}

	public static String getJsonDate(Date date){
		return "/Date(" + date.getTime() +""+getUTCOffset(date)+")/";
	}
	
	/**
	 * get utc offset from date
	 * @return timezone just like +0700, +1300
	 */
	public static String getUTCOffset(Date date)
	{
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());
		calendar.clear();
		calendar.setTime(date);
		Date currentLocalTime = calendar.getTime();
		DateFormat dateFormat = new SimpleDateFormat("Z", Locale.getDefault());
		String localTime = dateFormat.format(currentLocalTime);
		return localTime;
	}
	

	public static String dateToISO8601(Date date){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		return formatter.format(date);
	}
	public static Date getDateFromUnixTimestamp(String unixTimestamp){
		Long timeInLong = Long.parseLong(unixTimestamp);
		return new Date(timeInLong);
	}
}
