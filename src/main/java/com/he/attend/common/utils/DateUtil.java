/**
 * DateUtil工具类API速查表:
 * 1.得到当前时间 getCurrentDate()
 * 2.得到当前年份字符串 getCurrentYear()
 * 3.得到当前月份字符串 getCurrentMonth()
 * 4.得到当天字符串 getCurrentDay()
 * 5.得到当前星期字符串(星期几) getCurrentWeek()
 * 6.Date转化为String formatDate()
 * 7.String转化为Date parseDate()
 * 8.比较时间大小 compareToDate()
 * 9.得到给定时间的给定天数后的日期 getAppointDate()
 * 10.获取两个日期之间的天数 getDistanceOfTwoDate()
 * 11.获取过去的天数 pastDays()
 * 12.获取过去的小时 pastHour()
 * 13.获取过去的分钟  pastMinutes()
 * 14.得到本周的第一天  getFirstDayOfWeek()
 * 15.得到当月第一天 getFirstDayOfMonth()
 * 16.得到下月的第一天 getFirstDayOfNextMonth()
 * 17.根据生日获取年龄 getAgeByBirthDate()
 */
package com.he.attend.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期时间工具类
 * @author wangfan
 * @date 2017-4-1 下午2:37:33
 */
public class DateUtil {

	/**
	 * 得到当前时间(yyyy-MM-dd HH:mm:ss)
	 * @return
	 */
	public static String getCurrentDate(){
		return formatDate(new Date());
	}
	
	/**
	 * 得到当前时间
	 * @param formate 格式
	 * @return
	 */
	public static String getCurrentDate(String formate){
		return formatDate(new Date(),formate);
	}
	
	/**
	 * 得到当前年份字符串
	 */
	public static String getCurrentYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串
	 */
	public static String getCurrentMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串
	 */
	public static String getCurrentDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串(星期几)
	 */
	public static String getCurrentWeek() {
		return formatDate(new Date(), "E");
	}
	
	/**
	 * Date转化为String
	 * @param date
	 * @param formate 格式
	 * @return
	 */
	public static String formatDate(Date date, String formate){
		SimpleDateFormat sdf = new SimpleDateFormat(formate);
		return sdf.format(date);
	}
	
	/**
	 * Date转化为String(yyyy-MM-dd HH:mm:ss)
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date){
		return formatDate(date,"yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * String转化为Date
	 * @param date
	 * @param formate
	 * @return
	 */
	public static Date parseDate(String date, String formate){
		SimpleDateFormat sdf = new SimpleDateFormat(formate);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * String转化为Date
	 * @param date
	 * @return
	 */
	public static Date parseDate(String date){
		return parseDate(date, "yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 比较时间大小
	 * @param first
	 * @param second
	 * @return 返回0 first等于second, 返回-1 first小于second,, 返回1 first大于second
	 */
	public static int compareToDate(String first, String second, String pattern) {
		DateFormat df = new SimpleDateFormat(pattern);
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		try {
			cal1.setTime(df.parse(first));
			cal2.setTime(df.parse(second));
		} catch (ParseException e) {
			e.printStackTrace();
			System.out.println("比较时间错误");
		}
		int result = cal1.compareTo(cal2);
		if (result < 0) {
			return -1;
		} else if (result > 0) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 比较时间大小
	 * @param first
	 * @param second
	 * @return 返回0 first等于second, 返回-1 first小于second,, 返回1 first大于second
	 */
	public static int compareToDate(Date first, Date second) {
		int result = first.compareTo(second);
		if (result < 0) {
			return -1;
		} else if (result > 0) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 得到给定时间的给定天数后的日期
	 * @return
	 */
	public static Date getAppointDate(Date date, int day){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, day);
		return calendar.getTime();
	}
	
	/**
	 * 获取两个日期之间的天数
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}
	
	/**
	 * 获取过去的天数
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (24 * 60 * 60 * 1000);
	}

	/**
	 * 获取过去的小时
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (60 * 60 * 1000);
	}

	/**
	 * 获取过去的分钟
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (60 * 1000);
	}
	
	/**
	 * 得到本周的第一天
	 * @return
	 */
	public static Date getFirstDayOfWeek(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return cal.getTime();
	}
	
	/**
	 * 得到当月第一天
	 * @return
	 */
	public static Date getFirstDayOfMonth() {
		Calendar cal = Calendar.getInstance();
		int firstDay = cal.getMinimum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, firstDay);
		return cal.getTime();
	}
	
	/**
	 * 得到下月的第一天
	 * @return
	 */
	public static Date getFirstDayOfNextMonth() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, +1);
		int firstDay = cal.getMinimum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, firstDay);
		return cal.getTime();
	}
	
	/**
	 * 根据生日获取年龄
	 * @param birtnDay
	 * @return
	 */
	public static int getAgeByBirthDate(Date birtnDay) {
		Calendar cal = Calendar.getInstance();
		if (cal.before(birtnDay)) {
			return 0;
		}
		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH);
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(birtnDay);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
		int age = yearNow - yearBirth;
		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				}
			} else {
				age--;
			}
		}
		return age;
	}

	/**
	 * 将unix时间戳 转换为 String格式
	 * @return
	 */
	public static String getUnixToString(String unixTime,String pattrn){
		SimpleDateFormat sim=new SimpleDateFormat(pattrn, Locale.CHINA);
		Long l=Long.parseLong(unixTime)*1000;
		return sim.format(new Date(l));
	}

	/**
	 *传入开始时间和结束时间，返回之间的所有日期的集合
	 * @param startTime  开始时间
	 * @param endTime    结束时间
	 * @return
	 */
	public static List<String> getDays(String startTime, String endTime){
		// 返回的日期集合
		List<String> days = new ArrayList<>();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date start = dateFormat.parse(startTime);
			Date end = dateFormat.parse(endTime);

			Calendar tempStart = Calendar.getInstance();
			tempStart.setTime(start);

			Calendar tempEnd = Calendar.getInstance();
			tempEnd.setTime(end);
			tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
			while (tempStart.before(tempEnd)) {
				days.add(dateFormat.format(tempStart.getTime()));
				tempStart.add(Calendar.DAY_OF_YEAR, 1);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return days;
	}

	public static String getLastDayOfMonth(int year,int month) {

		Calendar cal = Calendar.getInstance();

		//设置年份

		cal.set(Calendar.YEAR,year);

		//设置月份

		cal.set(Calendar.MONTH, month-1);

		//获取某月最大天数

		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

		//设置日历中月份的最大天数

		cal.set(Calendar.DAY_OF_MONTH, lastDay);

		//格式化日期

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String lastDayOfMonth = sdf.format(cal.getTime());

		return lastDayOfMonth;

	}

    public static String getFitstDayOfMonth(int year,int month) {

        Calendar cal = Calendar.getInstance();

        //设置年份

        cal.set(Calendar.YEAR,year);

        //设置月份

        cal.set(Calendar.MONTH, month-1);

        //获取某月最小天数

        //int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        int fitstDay=cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数

        cal.set(Calendar.DAY_OF_MONTH, fitstDay);

        //格式化日期

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String lastDayOfMonth = sdf.format(cal.getTime());

        return lastDayOfMonth;

    }

    public static Long dateDiff(String startTime, String endTime,
								String format, String str) {
		// 按照传入的格式生成一个simpledateformate对象
		SimpleDateFormat sd = new SimpleDateFormat(format);
		long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
		long nh = 1000 * 60 * 60;// 一小时的毫秒数
		long nm = 1000 * 60;// 一分钟的毫秒数
		long ns = 1000;// 一秒钟的毫秒数
		long diff;
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		// 获得两个时间的毫秒时间差异
		try {
			diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
			day = diff / nd;// 计算差多少天
			hour = diff % nd / nh + day * 24;// 计算差多少小时
			min = diff % nd % nh / nm + day * 24 * 60;// 计算差多少分钟
			sec = diff % nd % nh % nm / ns;// 计算差多少秒
			if (str.equalsIgnoreCase("h")) {
				return hour;
			} else {
				return min;
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (str.equalsIgnoreCase("h")) {
			return hour;
		} else {
			return min;
		}
	}

	/**
	 * 判断是否是周六周日
	 */
	public static boolean isStraOrSunDay(String day){
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");

		try {
			Date date=simpleDateFormat.parse(day);
			Calendar cal=Calendar.getInstance();
			cal.setTime(date);
			if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
				return true;
			}else {
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 判断日期是否在公休日
	 * @param day
	 * @return
	 */
	public static boolean isHoliday(String day){
		//维护2019年 节假日
		String[] holidayArray={"2019-01-01","2019-02-04","2019-02-05","2019-02-06","2019-02-07","2019-02-08","2019-02-09","2019-02-10",
				"2019-04-05","2019-04-06","2019-04-07","2019-05-01","2019-05-02","2019-05-03","2019-05-04","2019-06-07","2019-06-08",
				"2019-06-09","2019-09-13","2019-09-14","2019-09-15","2019-10-01","2019-10-02","2019-10-03","2019-10-04","2019-10-05",
				"2019-10-06","2019-10-07"};
		List<String> holidayList=Arrays.asList(holidayArray);
		if(holidayList.contains(day)){
			return true;
		}else {
			return false;
		}

	}

	/**
	 * 判断日期是否在公休日
	 * @param day
	 * @return 正常工作日对应结果为 0, 法定节假日对应结果为 1, 节假日调休补班对应的结果为 2，休息日对应结果为 3
	 */
	public static Integer isHolidayByInterface(String day){
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		String dayreplace=day.replaceAll("-","");
		String httpUrl="http://api.goseek.cn/Tools/holiday?date="+dayreplace;

		try {
			URL url = new URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			// 填入apikey到HTTP header
			connection.setRequestProperty("apikey", "abfa5282a89706affd2e4ad6651c9648");
			connection.connect();
			InputStream is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sbf.append(strRead);
				sbf.append("\r\n");
			}
			reader.close();
			result = sbf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//正常工作日对应结果为 0, 法定节假日对应结果为 1, 节假日调休补班对应的结果为 2，休息日对应结果为 3
		JSONObject jsonObject= JSONObject.parseObject(result);

		return (Integer) jsonObject.get("data");

	}
}
