package org.tfa.mtld.web.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author divesh.solanki
 * 
 */
public class Utils {

	public static String getCurrentTimeStamp() {
		DateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

		// get current date time with Calendar()
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}

}
