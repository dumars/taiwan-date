package tw.pers.dumars.taiwandate.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import tw.pers.dumars.taiwandate.TaiwanCalendar;
import tw.pers.dumars.taiwandate.TaiwanDateFormat;

/**
 * @author Dumars
 */
public class TaiwanDateUtils {
	
	private TaiwanDateUtils() {
	}
	
	/**
	 * 西元日期轉換為民國日期，需注意的是，如果日期是錯誤的，
	 * 例如：20100231(無2月31日這樣的日期)，則會被轉換為3月3日
	 * @param date 西元日期字串
	 * @param sourcePattern 輸入的西元日期格式(example: yyyyMMdd or yyyyMM ...etc)
	 * @param distPattern 要輸出的民國日期格式(example: yyy-MM-dd or yyy-MM ...etc)
	 * @return 民國日期字串
	 * @throws ParseException
	 */
	public static String ad2tw(String source, String sourcePattern,
			String distPattern) throws ParseException {
		DateFormat format = new SimpleDateFormat(sourcePattern);
		TaiwanCalendar tcal = new TaiwanCalendar();
		tcal.setTimeInMillis(format.parse(source).getTime());
		return tcal.toTaiwanDateString(distPattern);
	}

	/**
	 * 修正錯誤日期，僅限民國日期格式<br>
	 * 日期格式範例：990101, 0990101, 01000101, -010101, -0010101,
	 * 99/01/01, 099/01/01, 0100/01/01<br><br>
	 * 亦可包含時間，但包含時間的日期格式僅限3碼長度的年份格式，時間與日期需有空白分隔：<br>
	 * 0990101 23:59:59, 099/01/01 23:59:59
	 * @param source 民國日期字串
	 * @return
	 */
	public static String correct(String source) {
		String[] fields;
		String suffix = "";
		if(StringUtils.indexOf(source, "/") > 0) {
			fields = StringUtils.split(source, "/");
			String day = fields[fields.length - 1].trim();
			if(day.length() > 2) {
				fields[fields.length - 1] = day.substring(0, day.indexOf(" "));
				suffix = StringUtils.substring(day, day.indexOf(" "));
			}
		} else {
			fields = new String[3];
			switch(source.length()) {
			case 6:
				fields = split(source, new int[]{2,2,2});
				break;
			case 7:
				fields = split(source, new int[]{3,2,2});
				break;
			case 8:
				fields = split(source, new int[]{4,2,2});
				break;
			default:
				fields = split(source, new int[]{3,2,2});
				suffix = StringUtils.substring(source, 8);
			}
		}
		
		// Year
		if(Integer.parseInt(fields[0]) == 0) {
			fields[0] = "-01";
		} else {
			if(Integer.parseInt(fields[0]) < 0)
				fields[0] = (new DecimalFormat("00")).format(Integer.parseInt(fields[0]));
			else
				fields[0] = (new DecimalFormat("000")).format(Integer.parseInt(fields[0]));
		}
		
		// Month
		if(Integer.parseInt(fields[1]) < 1) {
			fields[1] = "01";
		} else if(Integer.parseInt(fields[1]) > 12) {
			fields[1] = "12";
		} else {
			fields[1] = (new DecimalFormat("00")).format(Integer.parseInt(fields[1]));
		}
		
		//Day
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(fields[0]));
		cal.set(Calendar.MONTH, Integer.parseInt(fields[1])-1);
		String day = fields[2].trim();
		if(day.length() > 2) {
			fields[2] = day.substring(0, day.indexOf(" "));
			suffix = StringUtils.substring(day, day.indexOf(" "));
		}
		if(Integer.parseInt(fields[2]) < 1) {
			fields[2] = "01";
		} else if(Integer.parseInt(fields[2]) > cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
			fields[2] = String.valueOf(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		} else {
			fields[2] = (new DecimalFormat("00")).format(Integer.parseInt(fields[2]));
		}
		
		return (fields[0] + fields[1] + fields[2] + " " + suffix).trim();
	}
	
	private static String[] split(String source, int[] lengths) {
		String[] values = new String[lengths.length];
		int start = 0;
		for(int i = 0; i < lengths.length; i++) {
			values[i] = StringUtils.substring(source, start, start+lengths[i]);
			start += lengths[i];
		}
		return values;
	}
	
	/**
	 * 修正錯誤日期，僅限民國日期格式<br>
	 * 日期格式範例：990101, 0990101, 01000101, -010101, -0010101,
	 * 99/01/01, 099/01/01, 0100/01/01<br><br>
	 * @param source 民國日期字串
	 * @return boolean
	 */
	public static boolean validate(String source) {
		if(StringUtils.indexOf(source, "/") > 0) {
			source = StringUtils.replace(source, "/", "");
		}
		
		if(StringUtils.equals(source, correct(source))) {
			return true;
		}
		return false;
	}
	
	/**
	 * 對日期做最簡易的格式化
	 * {@see tw.gov.bli.core.utils.TaiwanDateFormat}
	 * @param date java.util.Date
	 * @return "yyyMMdd" format date string
	 */
	public static String simpleFormat(Date date) {
	    return new TaiwanDateFormat().formatDate(date);
	}
	
	/**
	 * 對日期做簡單的格式化
	 * {@see tw.gov.bli.core.utils.TaiwanDateFormat}
	 * @param date java.util.Date
	 * @return "yyy/MM/dd hh:mm:ss" format date string
	 */
	public static String simpleFormatForDateTime(Date date){
	    return format(date, "yyy/MM/dd hh:mm:ss");
	}
	 
	/**
	 * 對日期做完整的格式化
	 * {@see tw.gov.bli.core.utils.TaiwanDateFormat}
	 * @param date java.util.Date
	 * @return "民國yyy年MM月dd日 hh時mm分ss秒" format date string
	 */
	public static String wholeFormatForDateTime(Date date) {
	    return format(date, "民國yyy年MM月dd日 hh時mm分ss秒");
	}
	
	/**
	 * 對日期依自訂的格化做格式化
	 * {@see tw.gov.bli.core.utils.TaiwanDateFormat}
	 * @param date java.util.Date
	 * @param pattern for output format
	 * @return pattern format date string
	 */
	public static String format(Date date, String pattern) {
	    return new TaiwanDateFormat(pattern).formatDate(date);
	}
	
	public static TaiwanCalendar date2TaiwanCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return (new TaiwanCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DATE), cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE),
				cal.get(Calendar.SECOND)));
	}
}
