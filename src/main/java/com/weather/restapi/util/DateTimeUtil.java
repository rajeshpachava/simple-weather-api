package com.weather.restapi.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeUtil {

	public static Date getDateWithStartOfTheDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
}
