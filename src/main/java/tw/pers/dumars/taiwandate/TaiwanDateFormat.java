package tw.pers.dumars.taiwandate;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

public class TaiwanDateFormat extends SimpleDateFormat {

	private static final long serialVersionUID = -393359304759931214L;
	
	private String year;
	public static final String DEFAULT_PATTERN = "yyy/MM/dd";
	
	/**
	 * 預設的格式為 yyy/MM/dd
	 */
	public TaiwanDateFormat() {
		this(DEFAULT_PATTERN);
	}

	/**
	 * 依照輸入的格式來格式化日期
	 */
	public TaiwanDateFormat(String pattern) {
		super(pattern);
		int i = pattern.indexOf('y');
		int l = pattern.lastIndexOf('y');
		if (i > -1)
			year = pattern.substring(i, l + 1);
		year = year.replaceAll("y", "0");
	}
	
	/**
	 * 將 {@link  java.util.Date} 物件轉化為日期字串
	 * @param date {@link  java.util.Date}
	 * @return 格式化的日期字串
	 */
	public String formatDate(Date date) {
		StringBuffer buf = new StringBuffer();
		FieldPosition pos = new FieldPosition(DateFormat.YEAR_FIELD);
		super.format(date, buf, pos);
		
		String prefix = buf.substring(0, pos.getBeginIndex());
		String suffix = buf.substring(pos.getEndIndex());

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int ROCyear = cal.get(Calendar.YEAR) - 1911;
		if (ROCyear <= 0)
			ROCyear -= 1;
		
		String mid = "";
		if (year.length() > 0) {
			if(ROCyear < 0) {
				year = StringUtils.replaceOnce(year, "0", "#");
			}
			mid = (new DecimalFormat(year)).format(ROCyear);
		}
		year = StringUtils.replaceOnce(year, "#", "0");
		
		if(StringUtils.equals(prefix, "民國") && ROCyear < 0) {
			prefix = prefix + "前";
			mid = mid.replace("-", "");
		}
		
		return prefix + mid + suffix;
	}
	
	/**
	 * 將民國日期格式的字串轉成 {@link  java.util.Date} 物件
	 * @param source 民國日期字串
	 * @param sourcePattern 來源字串的格式
	 * @return {@link  java.util.Date}，產生錯誤的時候會回傳null
	 * @throws ParseException
	 */
	public Date parseString(String source, String sourcePattern) throws ParseException {
		try {
			Date date = DateUtils.parseDate(source, new String[]{sourcePattern});
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			if(cal.get(Calendar.ERA) == 0) {
				cal.add(Calendar.YEAR, 1912);
			} else {
				cal.add(Calendar.YEAR, 1911);
			}
			return cal.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 將民國日期格式的字串轉成 {@link  java.util.Date} 物件
	 * @param source 民國日期字串
	 * @return {@link  java.util.Date}，產生錯誤的時候會回傳null
	 * @throws ParseException
	 */
	public Date parse(String source) throws ParseException {
		return parseString(source, this.toPattern());
	}
}
