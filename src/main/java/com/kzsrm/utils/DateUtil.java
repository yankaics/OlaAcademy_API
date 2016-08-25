package com.kzsrm.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期格式
 * 
 * @author Echo Wang
 * 
 *         2015年7月14日
 */
public class DateUtil {

	public static String y_M_dPattern = "yyyy-MM-dd";
	public static String yMdPattern = "yyyyMMdd";
	public static String y_M_d_HmsPattern = "yyyy-MM-dd HH:mm:ss";
	public static String y_M_d_HmPattern = "yyyy-MM-dd HH:mm";
	public static String yMdHmsPattern = "yyyyMMddHHmmss";
	public static String HmPattern = "HH.mm";

	/**
	 * 获取当前时间字符串
	 * 
	 * @param pattern
	 * @return
	 */
	public static String currentDateStr(String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(new Date());
	}

	public static String formatDate(Date date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/*
	 * 今天星期几
	 */
	public static String getWeek() {
		Date date = new Date();
		SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
		return dateFm.format(date);
	}

	/*
	 * 今天几号
	 */
	@SuppressWarnings("deprecation")
	public static int getDay() {
		Calendar c = Calendar.getInstance();
		return c.getTime().getDate();
	}

	/*
	 * 第二天几号
	 */
	@SuppressWarnings("deprecation")
	public static int getNextDay() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 1);
		return c.getTime().getDate();
	}

	/**
	 * 获取半小时后的时间段
	 * 
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static String nextHalfHour(String day, int x, int y) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(DateUtil.y_M_d_HmPattern);// 24小时制
		Date date = null;
		try {
			date = format.parse(day);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (date == null)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, y);// 24小时制
		date = cal.getTime();

		/** start **/
		Date date1 = null;
		try {
			date1 = format.parse(day);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (date1 == null)
			return null;
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		cal1.add(Calendar.MINUTE, x);// 24小时制
		date1 = cal1.getTime();
		/** end **/

		cal = null;
		String s = format.format(date);
		String s1 = format.format(date1);
		// System.out.println(s1 + " " + s);
		return s1 + " -- " + s;
	}

	/**
	 * 当天凌晨00:00:00的时间字符串
	 * 
	 * @param pattern
	 * @return
	 */
	public static String nextMorningDateStr(String pattern) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, 0);
		c.add(Calendar.DATE, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return new SimpleDateFormat(pattern).format(c.getTime());
	}

	/*
	 * 昨天日期
	 */
	public static String getYesterday() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		String str = new SimpleDateFormat(DateUtil.y_M_dPattern).format(c.getTime());
		return str;
	}

	/**
	 * 当前日期加一天
	 * 
	 * @return
	 */
	public static String getAddOneDay() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 1);
		String str = new SimpleDateFormat(DateUtil.y_M_dPattern).format(c.getTime());
		return str;
	}

	/*
	 * 两个时间段相差天数
	 */
	public static int getDifferSec(Date smdate, Date bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}

	/*
	 * 当前时间和第二天时间相差几个小时
	 */
	public static int getDifferHour(String str, String h) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.y_M_d_HmsPattern);
		Date date1 = sdf.parse(str);
		Date date2 = sdf.parse(h);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		long time1 = cal.getTimeInMillis();
		cal.setTime(date2);
		long time2 = cal.getTimeInMillis();
		long between_hours = (time2 - time1) / (1000 * 3600);
		return (int) between_hours;
	}

	/**
	 * 当前时间段处于哪个半小时
	 * 
	 * @return
	 */
	public static String showCurrentTime() {
		String date = DateUtil.currentDateStr(DateUtil.y_M_d_HmPattern);
		String[] str = date.split(" ");
		String[] newStr = str[1].split(":");
		String startTime = null, endTime = null;
		if (Integer.parseInt(newStr[1]) >= 0 && Integer.parseInt(newStr[1]) <= 30) {
			startTime = str[0] + " " + newStr[0] + ":00";
			endTime = str[0] + " " + newStr[0] + ":30";
		} else if (Integer.parseInt(newStr[1]) >= 30 && Integer.parseInt(newStr[1]) <= 60) {
			startTime = str[0] + " " + newStr[0] + ":30";
			endTime = str[0] + " " + (Integer.parseInt(newStr[0]) + 1) + ":00";
		}
		// System.out
		// .println("startTime+endTime " + startTime + " " + endTime);
		return startTime + " - " + endTime;
	}

	public static void main(String[] args) throws ParseException {
		/*
		 * Echo 测试开始
		 */

		// System.out.println(getNextDay());

		// nextHalfHourDateStr("09:00-21:00");
		// getDifferHour();
		// System.out.println(nextMorningDateStr(DateUtil.y_M_d_HmsPattern));
       

		/*
		 * String ss = showCurrentTime(); // 2015-07-27 10:30 - 2015-07-27 11:00
		 * String[] newStrArr = ss.split(" - "); String s = "11:00-21:00";
		 * List<String> total = new ArrayList<String>(); // 接受预定时间 String result
		 * = ""; String[] strs1 = s.split("-"); String s1 =
		 * newStrArr[0].trim().substring(0, newStrArr[0].length() - 5) .trim();
		 * s1 = s1 + " " + strs1[0]; for (int i = 2; i <= 50; i++) { result =
		 * DateUtil.nextHalfHour(s1, 30 * (i - 2), 30 * (i - 1)); String[] arr =
		 * result.split(" -- "); total.add(result); if (arr[0].trim().equals(
		 * arr[0].substring(0, 10).trim() + " " + strs1[1].trim())) { break; } }
		 * if (total.size() > 0) { total.remove(total.size() - 1); } String str1
		 * = DateUtil.getHour(); String[] str1Arr = str1.split(" "); String str2
		 * = str1Arr[0] + " " + strs1[0]; String[] str = str2.split(" ");
		 * String[] str3 = str[1].toString().split(":"); if
		 * (Integer.parseInt(str3[0]) < 12) { for (int i = 2; i <= 50; i++) {
		 * result = DateUtil .nextHalfHour(str2, 30 * (i - 2), 30 * (i - 1));
		 * String[] arr = result.split(" -- "); total.add(result); if
		 * (arr[1].equals(arr[1].toString().trim().substring(0, 10) + " 12:00"))
		 * { break; } } } System.out.println(total);
		 */
	}

	/*
	 * Echo 测试结束
	 */
}
