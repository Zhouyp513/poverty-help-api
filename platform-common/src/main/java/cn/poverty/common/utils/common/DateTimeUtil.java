package cn.poverty.common.utils.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;


/**
 
 * @packageName cn.poverty.common.utils.common
 * @Description: 日期处理类
 * @date 2021-01-21
 */
@Slf4j
public class DateTimeUtil {


	public static String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public static String DAY_DATETIME_PATTERN = "yyyy-MM-dd";

	/**
	 * 指定时间的凌晨
	 
	 * @date 2018/12/3 16:30
	 * @param time
	 * @return
	 */
	public static Long getTimesMorningFromZoneDateTime(Long time) {
		LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(time), Clock.systemDefaultZone().getZone());
		Instant instant = LocalDateTime.of(localDateTime.getYear(),
				localDateTime.getMonthValue(), localDateTime.getDayOfMonth(),
				0, 0, 0).atZone(Clock.systemDefaultZone().getZone()).toInstant();

		return instant.getEpochSecond();
	}

	/**
	 * 当天23点59分59秒
	 * @return
	 */
	public static Long getTimesWeeHoursFromZoneDateTime(Long time) {
		LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(time), Clock.systemDefaultZone().getZone());
		Instant instant = LocalDateTime.of(localDateTime.getYear(),
				localDateTime.getMonthValue(), localDateTime.getDayOfMonth(),
				23, 59, 59).atZone(Clock.systemDefaultZone().getZone()).toInstant();

		return instant.getEpochSecond();
	}

	/**
	 * 指定时间的凌晨
	 
	 * @date 2018/12/3 16:30
	 * @param zonedDateTime
	 * @return
	 */
	public static Long getTimesMorningFromZoneDateTime(ZonedDateTime zonedDateTime) {
		Instant instant = LocalDateTime.of(zonedDateTime.getYear(),
				zonedDateTime.getMonthValue(), zonedDateTime.getDayOfMonth(),
				0, 0, 0).atZone(Clock.systemDefaultZone().getZone()).toInstant();

		return instant.getEpochSecond();
	}

	/**
	 * 当天23点59分59秒
	 * @return
	 */
	public static Long getTimesWeeHoursFromZoneDateTime(ZonedDateTime zonedDateTime) {
		Instant instant = LocalDateTime.of(zonedDateTime.getYear(),
				zonedDateTime.getMonthValue(), zonedDateTime.getDayOfMonth(),
				23, 59, 59).atZone(Clock.systemDefaultZone().getZone()).toInstant();

		return instant.getEpochSecond();
	}

	/**
	 * 将Long类型的时间戳转换成String 类型的时间格式，时间格式为：yyyy-MM-dd HH:mm:ss
	 */
	public static String convertTimeToString(Long time){
		Assert.notNull(time, "time is null");
		DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return ftf.format(LocalDateTime.ofInstant(Instant.ofEpochSecond(time), ZoneId.systemDefault()));
	}


	/**
	 * 把指定的时间LocalDateTime变成10位的Long型
	 * 
	 * @date 2019-08-19
	 * @param time 指定的时间
	 * @return java.lang.Long
	 */
	public static Long localDateTimeToLong(LocalDateTime time){
		Assert.notNull(time, "time is null");
		return time.toEpochSecond(ZoneOffset.of("+8"));
	}

	/**
	 * 把指定的时间LocalDateTime变成的String型
	 * 
	 * @date 2019-08-19
	 * @param time 指定的时间
	 * @return java.lang.Long
	 */
	public static String localDateTime(String pattern, LocalDateTime time){
		Assert.notNull(time, "time is null");
		if(CheckParam.isNull(pattern)){
			return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		}else{
			return time.format(DateTimeFormatter.ofPattern(pattern));
		}
	}

	/**
	 * 将Long类型的时间戳转换成String 类型的时间格式，时间格式为：yyyy-MM-dd HH:mm:ss
	 */
	public static String convertTimeToString(String pattern,Long time){
		Assert.notNull(time, "time is null");
		if(CheckParam.isNull(pattern)){
			return LocalDateTime.ofInstant(Instant.ofEpochSecond(time),Clock.systemDefaultZone().getZone()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		}else{
			return LocalDateTime.ofInstant(Instant.ofEpochSecond(time),Clock.systemDefaultZone().getZone()).format(DateTimeFormatter.ofPattern(pattern));
		}
	}

	/**
	 * 把指定的时间LocalDateTime变成字符串格式的时间格式
	 * 
	 * @date 2019-08-21
	 * @param pattern 时间格式
	 * @param time 指定的时间
	 * @return
	 */
	public static String convertTimeToString(String pattern,LocalDateTime time){
		Assert.notNull(time, "time is null");
		if(CheckParam.isNull(pattern)){
			return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		}else{
			return time.format(DateTimeFormatter.ofPattern(pattern));
		}
	}

	/**
	 * LocalDateTime变换成Long类型
	 
	 * @date 2019/1/22 14:59
	 * @param time 时间
	 * @return java.lang.Long
	 */
	public static Long convertDateTimeToLong(LocalDateTime time){
		Assert.notNull(time, "time is null");
		return time.atZone(Clock.systemDefaultZone().getZone()).toInstant().getEpochSecond();
	}

	/**
	 * 日期字符串换成Long类型
	 
	 * @date 2019/1/22 14:59
	 * @param time 时间
	 * @return java.lang.Long
	 */
	public static Long convertStringToLongTime(String time){
		Assert.notNull(time, "time is null");
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return LocalDateTime.parse(time,format).atZone(Clock.systemDefaultZone().getZone()).toInstant().getEpochSecond();
	}

	/**
	 * 日期字符串换成Long类型
	 
	 * @date 2019/1/22 14:59
	 * @param time 时间
	 * @return java.lang.Long
	 */
	public static LocalDateTime stringToLocalDateTime(String time){
		Assert.notNull(time, "time is null");
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return LocalDateTime.parse(time,format);
	}


	/**
	 * Long变换成LocalDateTime epoch 10位的Long
	 * @param time
	 * @return
	 */
	public static LocalDateTime localDateTime(Long time){
		return LocalDateTime.ofInstant(Instant.ofEpochSecond(time), Clock.systemDefaultZone().getZone());
	}

	/**
	 * Long类型变换Date类型
	 * @param time
	 * @return
	 */
	public static Date longToDate(Long time){
		return Date.from(Instant.ofEpochSecond(time));
	}

	/**
	 * Date类型成为Long类型
	 
	 * @date 2019/1/22 15:13
	 * @param date 时间
	 * @return java.lang.Long
	 */
	public static Long dateToLong(Date date){
		return date.toInstant().getEpochSecond();
	}

	/**
	 * 当前时间减去加上指定月份和减去指定天数
	 
	 * @date 2018/11/8 16:41
	 * @param time 当前时间
	 * @param plusMonth 需要增加的月份
	 * @param minusDay 需要减去的天数
	 * @return
	 */
	public static Long plusMonthAndMinusDay(Instant time,Long plusMonth,Long minusDay){
		//Instant -> LocalDateTime 方便使用时间矫正器
		return time.plus(plusMonth,ChronoUnit.MONTHS).with(TemporalAdjusters.lastDayOfMonth()).minus(minusDay,ChronoUnit.DAYS).atZone(Clock.systemDefaultZone().getZone()).toInstant().getEpochSecond();
	}

	/**
	 *  返回指定时间减去指定月份的时间的当天最晚时间
	 * 
	 * @date 2019-08-19
	 * @param time 指定的时间
	 * @param minusMonth 需要减去的月份数量
	 * @return Long
	 */
	public static Long minusMonthAndLastTimeOfDay(LocalDateTime time, Long minusMonth){
		return returnLatestTimeByTime(time.minus(minusMonth,ChronoUnit.MONTHS).with(TemporalAdjusters.lastDayOfMonth()).atZone(Clock.systemDefaultZone().getZone()).toInstant().getEpochSecond());
	}

	/**
	 *  返回指定时间减去指定月份的时间的当天最早(凌晨)时间
	 * 
	 * @date 2019-08-19
	 * @param time 指定的时间
	 * @param minusMonth 需要减去的月份数量
	 * @return Long
	 */
	public static Long minusMonthAndWeeHoursOfDay(LocalDateTime time, Long minusMonth){
		return returnDayWeeHoursByTime(time.minus(minusMonth,ChronoUnit.MONTHS).with(TemporalAdjusters.lastDayOfMonth()).atZone(Clock.systemDefaultZone().getZone()).toInstant().getEpochSecond());
	}


	/**
	 * 当前时间减去加上指定月份和减去指定天数
	 
	 * @date 2018/11/8 16:41
	 * @param time 当前时间
	 * @param plusMonth 需要增加的月份
	 * @param minusDay 需要减去的天数
	 * @return
	 */
	public static LocalDateTime plusMonthAndMinusDay(LocalDateTime time,Long plusMonth,Long minusDay){
		return time.plus(plusMonth,ChronoUnit.MONTHS).with(TemporalAdjusters.lastDayOfMonth()).minus(minusDay,ChronoUnit.DAYS);
	}

	/**
	 * 当前时间减减去指定天数
	 
	 * @date 2018/11/8 16:41
	 * @param time 当前时间
	 * @param minusDay 需要减去的天数
	 * @return
	 */
	public static LocalDateTime minusDay(LocalDateTime time,Long minusDay){
		return time.minus(minusDay,ChronoUnit.DAYS);
	}

	/**
	 * 当前时间减减去指定天数，返回Date对象
	 
	 * @date 2018/11/8 16:41
	 * @param time 当前时间
	 * @param minusDay 需要减去的天数
	 * @return
	 */
	public static Date plusDay(LocalDateTime time,Long minusDay){
		//Combines this date-time with a time-zone to create a  ZonedDateTime.
		ZonedDateTime zdt = time.plusDays(minusDay).atZone(ZoneId.systemDefault());
		return Date.from(zdt.toInstant());
	}

	/**
	 * 当前时间减减去指定月数
	 
	 * @date 2018/11/8 16:41
	 * @param time 当前时间
	 * @param minusMonth 需要减去的月数
	 * @return LocalDateTime
	 */
	public static LocalDateTime minusMonth(LocalDateTime time,Long minusMonth){
		return time.minus(minusMonth,ChronoUnit.MONTHS);
	}

	/**
	 * 当前时间减去对应天数
	 * 
	 * @date 2019-08-16
	 * @param minusMonth 需要减去的月数
	 * @return
	 */
	public static Long minusMonthFromNow(Long minusMonth){
		return minusMonth(LocalDateTime.of(LocalDate.now(), LocalTime.MIN),minusMonth).atZone(Clock.systemDefaultZone().getZone()).toInstant().getEpochSecond();
	}

	/**
	 *
	 * 指定的时间增加指定的天数
	 * 
	 * @date 2019-08-16
	 * @param minusDay 需要增加的天数
	 * @param time 指定的时间
	 * @return
	 */
	public static Long plusDay(Long time,Long minusDay){
		LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(time), Clock.systemDefaultZone().getZone());
		return localDateTime.plus(minusDay,ChronoUnit.DAYS).atZone(Clock.systemDefaultZone().getZone()).toInstant().getEpochSecond();
	}

	/**
	 * 当前时间减去对应天数
	 * 
	 * @date 2019-08-16
	 * @param minusDay 需要减去的对应天数
	 * @return
	 */
	public static Long minusDay(Long minusDay){
		return minusDay(LocalDateTime.of(LocalDate.now(), LocalTime.MIN),minusDay).atZone(Clock.systemDefaultZone().getZone()).toInstant().getEpochSecond();
	}

	/**
	 * 拿到yyyy-MM-dd HH:mm:ss格式字符串的下个月对应的时间
	 
	 * @date 2018/12/12 14:50
	 * @param time 指定时间格式的字符串
	 * @param subMonth 需要向前推进的时间
	 * @return
	 */
	public static String nextMonth(String time,Integer subMonth){
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return LocalDateTime.parse(time,format).plus(subMonth,ChronoUnit.MONTHS).toString();
	}

	/**
	 * 返回当前时间
	 
	 * @date 2019/2/21 17:48
	 * @return java.lang.Long
	 */
	public static Long currenLongTime(){
		return Instant.now().atZone(Clock.systemDefaultZone().getZone()).toEpochSecond();
	}


	/**
	 * 返回当前时间
	 
	 * @date 2019/2/21 17:48
	 * @return java.lang.String
	 */
	public static String currenTime(String pattern){
		LocalDateTime currentTime = LocalDateTime.ofInstant(Instant.now(), Clock.systemDefaultZone().getZone());
		if(CheckParam.isNull(pattern)){
			return currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		}else{
			return currentTime.format(DateTimeFormatter.ofPattern(pattern));
		}
	}

	/**
	 * 返回当天零晨时间
	 
	 * @date 2019/2/21 17:48
	 * @return java.lang.Long
	 */
	public static Long currentDayWeeHours(){
		return LocalDateTime.of(LocalDate.now(), LocalTime.MIN).atZone(Clock.systemDefaultZone().getZone()).toInstant().getEpochSecond();
	}

	/**
	 * 返回指定时间零晨时间
	 
	 * @date 2019/2/21 17:48
	 * @param time 根据指定时间拿到凌晨时间
	 * @return java.lang.Long
	 */
	public static Long returnDayWeeHoursByTime(Long time){
		LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(time), Clock.systemDefaultZone().getZone());
		return LocalDateTime.of(localDateTime.getYear(),localDateTime.getMonth(),localDateTime.getDayOfMonth(),0,0,0).atZone(Clock.systemDefaultZone().getZone()).toInstant().getEpochSecond();
	}

	/**
	 * 返回指定时间最晚时间
	 
	 * @date 2019/2/21 17:48
	 * @param time 根据指定时间拿到凌晨时间
	 * @return java.lang.Long
	 */
	public static Long returnLatestTimeByTime(Long time){
		LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(time), Clock.systemDefaultZone().getZone());
		return LocalDateTime.of(localDateTime.getYear(),localDateTime.getMonth(),localDateTime.getDayOfMonth(),23,59,59).atZone(Clock.systemDefaultZone().getZone()).toInstant().getEpochSecond();
	}

	/**
	 * 返回当天最晚时间
	 
	 * @date 2019/2/21 17:48
	 * @return java.lang.Long
	 */
	public static Long currentDayLatestTime(){
		return LocalDateTime.of(LocalDate.now(), LocalTime.MAX).atZone(Clock.systemDefaultZone().getZone()).toInstant().getEpochSecond();
	}

	/**
	 * 计算两个时间的月份差
	 
	 * @date 2019/1/18 16:39
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	public static Integer calMonthDuration(Long beginTime,Long endTime){
		LocalDate beginLocalDate = LocalDate.parse(convertTimeToString(beginTime), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		LocalDate endLocalDate = LocalDate.parse(convertTimeToString(endTime), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		Period periodTime = Period.between(beginLocalDate, endLocalDate);
		return periodTime.getMonths();
	}

	/**
	 * 返回指定天数后的日期
	 *
	 * @param day 指定天数
	 * @return Long 指定天数后的日期
	 */
	public static Long calAfterDayTime(Long day){
		LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
		return localDateTime.plus(day,ChronoUnit.DAYS).atZone(Clock.systemDefaultZone().getZone()).toInstant().getEpochSecond();
	}

	public static void main(String[] args) {


	}


}
