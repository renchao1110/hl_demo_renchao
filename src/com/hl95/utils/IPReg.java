package com.hl95.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPReg {
	public static boolean isboolIP(String ipAddr) {

		String ip = "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\."
				+ "(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})";
		Pattern pattern = Pattern.compile(ip);
		Matcher matcher = pattern.matcher(ipAddr);
		return matcher.matches();
	}

	public static void main(String[] args) {
		/*
		 * String ipAddr= "120.120.120.100";
		 * System.out.println(isboolIP(ipAddr));
		 */
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String time = "2015-2";
		try {
			Calendar cal = Calendar.getInstance();
			Date d = sdf.parse(time);
			cal.setTime(d);
			cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
			cal.set(Calendar.HOUR, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime()));
			cal.set(Calendar.DATE, 1);
			cal.set(Calendar.HOUR, -12);
			cal.set(Calendar.MINUTE, 00);
			cal.set(Calendar.SECOND, 00);
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime()));

		} catch (ParseException e) {
			e.printStackTrace();
		}

	}
}
