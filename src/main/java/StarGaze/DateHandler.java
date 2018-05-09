package StarGaze;

import java.text.DateFormat;
import java.text.ParseException;
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
	
}
