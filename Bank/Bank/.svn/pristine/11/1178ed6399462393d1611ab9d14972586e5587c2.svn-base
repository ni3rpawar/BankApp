package bankapp.utils;

import java.util.*;

/**
 * 
 * CSI3317_PROJECT
 *
 * @file Now.java
 * @package bankapp.utils
 * @author Mohamed Mansour @2005
 * @email m0.interactive@gmail.com
 * @web www.m0interactive.com
 * @date 9-Dec-2005
 *
 */
public class Now {

	/**
	 * Retrieve the current time stamp using uinx formats
	 * 
	 * @param DATE_FORMAT
	 * @return String
	 */
	public String getCurrentDate(String DATE_FORMAT) {
	
	    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("EST"));
	  
	    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
	
	    sdf.setTimeZone(TimeZone.getTimeZone("EST"));         
	
	    String time = sdf.format(cal.getTime());
	
	    return time;
	}
	
	/**
	 * Checks if the Date is greater than or equal to the second date
	 * 
	 * @param String originalDate 
	 * @param String checkDate
	 * @return boolean
	 */
	public boolean checkDate(String originalDate, String checkDate) {
		String datearr[];
		datearr = originalDate.split("-");
		int year = Integer.parseInt(datearr[0]);
		int month = Integer.parseInt(datearr[1]);
		int day = Integer.parseInt(datearr[2]);
		datearr = checkDate.split("-");
		int year2 = Integer.parseInt(datearr[0]);
		int month2 = Integer.parseInt(datearr[1]);
		int day2 = Integer.parseInt(datearr[2]);
		
		if(year >= year2)
			if(month >= month2)
				if(day >= day2)
					return true;
		
		return false;
	}
}