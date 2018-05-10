package StarGaze;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DateHandler {
	
	public static String getCurrentDate(){
		DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT);
		Date today = new Date();

		return dateFormatter.format(today);
	}
	public boolean compareDates(String date){
		return getCurrentDate().equals(date);
	}
	
	 public static ArrayList<String> getComingDates() {		// Returnar ArrayList<String> med dagens datum och kommande 5 dagar
		 ArrayList<String> dateList = new ArrayList<>();
		 String dateString;
		 
		 DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT);
		 Date today = new Date();
		 dateString = dateFormatter.format(today);
		 dateList.add(dateString);
		 for(int i = 0; i < 5; i++){
			 
			 try {
				 Calendar c = Calendar.getInstance();
				 c.setTime(dateFormatter.parse(dateString));
				 c.add(Calendar.DATE, 1);  							// number of days to add
				 dateString = dateFormatter.format(c.getTime());  	// dt is now the new date
				 dateList.add(dateString);
			} catch (ParseException e) {
				System.err.println("DateHandler.getComingDates() kastar exception");
				e.printStackTrace();
			}
		 }
		 return dateList;
	 }
	 
	 public String getWeekDay(String validTime)//ger veckodagen för specifikt datum enl SMHI datumformat
	 {
		 String[] parts = validTime.split("T");
		 String part1 = parts[0]; // YYYY-MM-dd

		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 Date d;
		try {
			d = sdf.parse(part1);
			 sdf.applyPattern("E");
			 String weekDay= sdf.format(d);
			 return weekDay.toString();
			 
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		 return null;
	 }
	
}
