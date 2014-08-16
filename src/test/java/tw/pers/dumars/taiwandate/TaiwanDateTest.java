package tw.pers.dumars.taiwandate;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class TaiwanDateTest {

	@Test
	public void test() {
		testGet();
		testBeforeAndAfter();
		testDateString();
		testToString();
		testFormatAndParse();
	}
	
	private void testGet() {
		TaiwanCalendar now = new TaiwanCalendar();
		println("Year:  " + now.get(Calendar.YEAR));
		println("Month: " + now.get(Calendar.MONTH) + "  (month 由 0 開始計算)");
		println("Date:  " + now.get(Calendar.DATE));
		println("Time:  " + now.getTime());
		println("-----------------------------------------------");
	}
	
	private void testBeforeAndAfter() {
		TaiwanCalendar now = new TaiwanCalendar();
		TaiwanCalendar other = new TaiwanCalendar();
		other.add(Calendar.DATE, 2);
		
		println("  now:" + now.toString());
		println("other:" + other.toString());
		println("now is before other: " + now.before(other));
		println("now is after  other: " + now.after(other));
		println("now.compareTo(other): " + now.compareTo(other));
		println("other.compareTo(now): " + other.compareTo(now));
		println("-----------------------------------------------");
	}
	
	private void testDateString() {
		TaiwanCalendar now = new TaiwanCalendar("990101");
		println(now.toString());
		
		now = new TaiwanCalendar("0990101");
		println(now.toString());
		
		now = new TaiwanCalendar("1000101");
		println(now.toString());
		
		// 民國0年等同民國1年
		now = new TaiwanCalendar("-000101");
		println(now.toString());
		
		// 民國元年(民國1年)
		now = new TaiwanCalendar("-010101");
		println(now.toString());
		
		// 民國前10年
		now = new TaiwanCalendar("-100101");
		println(now.toString());
		println("-----------------------------------------------");
	}
	
	private void testToString() {
		TaiwanCalendar now = new TaiwanCalendar();
		// 西元年
		println(now.toString());
		println(now.toString("yyyy-MM-dd HH:mm:ss"));
		
		// 民國年
		println(now.toTaiwanDateString());
		println(now.toTaiwanDateString("yyy/MM/dd HH:mm:ss"));
		println(now.toTaiwanDateString("yyy-MM-dd HH:mm:ss"));
		println("-----------------------------------------------");
	}
	
	private void testFormatAndParse() {
		TaiwanDateFormat format = new TaiwanDateFormat("yyy-MM-dd");
		println(format.formatDate(new Date()));
		
		try {
			String s = "民國103年05月01日";
			Date date = format.parseString(s, "民國yyy年MM月dd日");
			
			println(s + " --> " + new TaiwanDateFormat("yyy/MM/dd").formatDate(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		println("-----------------------------------------------");
	}
	
	private static void println(String s) {
		System.out.println(s);
	}

}
