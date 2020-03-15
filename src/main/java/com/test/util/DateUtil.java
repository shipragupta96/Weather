package com.test.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {

	public static int getDaysBetweenTwoDates(String startDate, String endDate, String format) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(formatter.parse(startDate));
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(formatter.parse(endDate));
		calendar2.add(Calendar.HOUR, 1);
		long diffInMillis = calendar2.getTimeInMillis() - calendar1.getTimeInMillis();
		int days = (int) (diffInMillis / (24 * 1000 * 60 * 60));
		return days;
	}

	public static int getHoursBetweenTwoDates(String startDate, String endDate, String format) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(formatter.parse(startDate));
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(formatter.parse(endDate));
		long diffInMillis = calendar2.getTimeInMillis() - calendar1.getTimeInMillis();
		int hours = (int) (diffInMillis / (1000 * 60 * 60));
		return hours;
	}

}
