package tw.pers.dumars.taiwandate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class TaiwanCalendar {
	
	private Calendar calendar = Calendar.getInstance();
	
	public TaiwanCalendar() {
	}
	
	/**
	 * 輸入的日期字串需為民國日期，990101 or 0990101 or 01000101 or -010101
	 * @param taiwanDate 民國日期字串
	 */
	public TaiwanCalendar(String taiwanDate) {
		int[] sdate;
		switch(taiwanDate.length()) {
		case 5: // ex. date:90101
			sdate = splitDate(taiwanDate, 1);
			break;
		case 6: // ex. date:990101 or -10101
			sdate = splitDate(taiwanDate, 2);
			break;
		case 7: // ex. date:0990101 or -010101
			sdate = splitDate(taiwanDate, 3);
			break;
		case 8: // ex. date:00990101 or -0010101
			sdate = splitDate(taiwanDate, 4);
			break;
		case 9: // ex. date:-00010101
			sdate = splitDate(taiwanDate, 5);
			break;
		default:
			throw new RuntimeException("The date string format is not correct.");
		}
		
		this.set(Calendar.YEAR, sdate[0]);
		this.set(Calendar.MONTH, sdate[1]-1); // Calendar 的月份由0開始
		this.set(Calendar.DAY_OF_MONTH, sdate[2]);

		this.set(Calendar.HOUR_OF_DAY, 0);
		this.set(Calendar.MINUTE, 0);
		this.set(Calendar.SECOND, 0);
	}
	
	/**
	 * 需注意的地方，Calendar 月份是由0開始，1月=0，2月=1，以此類推
	 * @param year 民國年數字
	 * @param month 月份數字
	 * @param dayOfMonth 日期數字
	 */
	public TaiwanCalendar(int year, int month, int dayOfMonth) {
		this(year, month, dayOfMonth, 0, 0, 0, 0);
	}
	
	/**
	 * 需注意的地方，Calendar 月份是由0開始，1月=0，2月=1，以此類推
	 * @param year
	 * @param month
	 * @param dayOfMonth
	 * @param hourOfDay
	 * @param minute
	 */
	public TaiwanCalendar(int year, int month, int dayOfMonth, int hourOfDay,
			int minute) {
		this(year, month, dayOfMonth, hourOfDay, minute, 0, 0);
	}
	
	/**
	 * 需注意的地方，Calendar 月份是由0開始，1月=0，2月=1，以此類推
	 * @param year
	 * @param month
	 * @param dayOfMonth
	 * @param hourOfDay
	 * @param minute
	 * @param second
	 */
	public TaiwanCalendar(int year, int month, int dayOfMonth,
			int hourOfDay, int minute, int second) {
		this(year, month, dayOfMonth, hourOfDay, minute, second, 0);
	}
	
	/**
	 * 需注意的地方，Calendar 月份是由0開始，1月=0，2月=1，以此類推
	 * @param year
	 * @param month
	 * @param dayOfMonth
	 * @param hourOfDay
	 * @param minute
	 * @param second
	 * @param millis
	 */
	public TaiwanCalendar(int year, int month, int dayOfMonth, int hourOfDay,
			int minute, int second, int millis) {
		this.set(Calendar.YEAR, year);
		this.set(Calendar.MONTH, month);
		this.set(Calendar.DAY_OF_MONTH, dayOfMonth);

		this.set(Calendar.HOUR_OF_DAY, hourOfDay);
		this.set(Calendar.MINUTE, minute);
		this.set(Calendar.SECOND, second);
	}
	
	private void set(int field, int value) {
		switch(field) {
		case Calendar.YEAR:
			if(value >= 0) {
				value += 1911;
			} else {
				value = 1911 + 1 + value;
			}
			break;
		case Calendar.MONTH:
			if(value < 0) {
				value = 0;
			} else if(value > 11) {
				value = 11;
			}
			break;
		case Calendar.DAY_OF_MONTH:
			if(value > calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
				value = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			}
			break;
		default:
			//do nothing
		}
		calendar.set(field, value);
	}
	
	/**
	 * {@link java.util.GregorianCalendar#add(int, int)}
	 */
	public void add(int field, int amount) {
		calendar.add(field, amount);
	}
	
	/**
	 * {@link  java.util.Calendar#getTime()}
	 */
	public Date getTime() {
		return calendar.getTime();
	}
	
	/**
	 * {@link  java.util.Calendar#get(int)}
	 */
	public int get(int field) {
		return calendar.get(field);
	}
	
	/**
	 * {@link  java.util.Calendar#after(Object)}
	 */
	public boolean after(TaiwanCalendar when) {
		return calendar.after(when.getCalendar());
	}
	
	/**
	 * {@link  java.util.Calendar#after(Object)}
	 */
	public boolean after(Calendar when) {
		return calendar.after(when);
	}
	
	/**
	 * {@link  java.util.Calendar#before(Object)}
	 */
	public boolean before(TaiwanCalendar when) {
		return calendar.before(when.getCalendar());
	}
	
	/**
	 * {@link  java.util.Calendar#before(Object)}
	 */
	public boolean before(Calendar when) {
		return calendar.before(when);
	}
	
	/**
	 * {@link  java.util.Calendar#getTimeInMillis()}
	 */
	public long getTimeInMillis() {
		return calendar.getTimeInMillis();
	}
	
	/**
	 * {@link  java.util.Calendar#setTimeInMillis(long)}
	 */
	public void setTimeInMillis(long millis) {
		calendar.setTimeInMillis(millis);
	}
	
	/**
	 * {@link  java.util.Calendar}
	 * @return java.util.Calendar
	 */
	public Calendar getCalendar() {
		return this.calendar;
	}
	
	/**
	 * {@link  java.util.Calendar#compareTo(java.util.Calendar)}
	 */
	public int compareTo(Calendar anotherCalendar) {
		return calendar.compareTo(anotherCalendar);
	}
	
	/**
	 * @see java.util.Calendar#compareTo(java.util.Calendar)
	 */
	public int compareTo(TaiwanCalendar anotherCalendar) {
		return calendar.compareTo(anotherCalendar.getCalendar());
	}
	
	/**
	 * {@link  java.util.Calendar#getActualMaximum(int)}
	 */
	public int getActualMaximum(int field) {
		return calendar.getActualMaximum(field);
	}
	
	/**
	 * {@link  java.util.Calendar#getActualMinimum(int)}
	 */
	public int getActualMinimum(int field) {
		return calendar.getActualMinimum(field);
	}
	
	/**
	 * {@link  java.util.Calendar#getMaximum(int)}
	 */
	public int getMaximum(int field) {
		return calendar.getMaximum(field);
	}
	
	/**
	 * {@link  java.util.Calendar#getMinimum(int)}
	 */
	public int getMinimum(int field) {
		return calendar.getMinimum(field);
	}
	
	/**
	 * 將 Calendar 物件轉為西元日期字串，預設格式為 yyyyMMdd HH:mm:ss
	 */
    public String toString() {
		DateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		return format.format(calendar.getTime());
	}
	
	/**
	 * 將 Calendar 物件轉為西元日期字串
	 * @param pattern 日期格式
	 * @return 西元日期字串
	 */
	public String toString(String pattern) {
		DateFormat format = new SimpleDateFormat(pattern);
		return format.format(calendar.getTime());
	}
	
	/**
	 * 將 Calendar 物件轉為民國日期字串，預設格式為 yyyMMdd
	 * @return 民國日期字串
	 */
	public String toTaiwanDateString() {
		TaiwanDateFormat format = new TaiwanDateFormat();
		return format.formatDate(calendar.getTime());
	}
	
	/**
	 * 將 Calendar 物件轉為民國日期字串
	 * @param pattern 日期格式
	 * @return 民國日期字串
	 */
	public String toTaiwanDateString(String pattern) {
		TaiwanDateFormat format = new TaiwanDateFormat(pattern);
		return format.formatDate(calendar.getTime());
	}
	
	private int[] splitDate(String taiwanDate, int length) {
		int[] d = new int[3];
		d[0] = Integer.parseInt(StringUtils.substring(taiwanDate, 0, length));
		d[1] = Integer.parseInt(StringUtils.substring(taiwanDate, length, length+2));
		d[2] = Integer.parseInt(StringUtils.substring(taiwanDate, length+2, length+4));
		return d;
	}
}
