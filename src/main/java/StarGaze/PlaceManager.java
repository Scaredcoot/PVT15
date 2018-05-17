package StarGaze;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/place")
public class PlaceManager {

	private static ConnectionManager connMan;

	// ------------------------------Contructors------------------------------------------

	// ------------------------------Methods------------------------------------------

	/**
	 * 5 kommande metoder
	 * 
	 * Returnerar kvällens eller kommande kvällens väderdata baserat på
	 * hårdkodad tid i private JSONObject getJSONObjectForPlace(String
	 * placeName){...} för specificerad plats i en String (ändra till JSON!)
	 * 
	 * Hämtas från: pvt.dsv.su.se/Group04/place/platsnamn/today
	 * 
	 * @return String med platsinfo formaterat som JSON
	 *
	 **/
	@RequestMapping("/gardet/today") // pvt.dsv.su.se/Group04/place/gardet/today
	public String gardet() {
		getJSONObjectForPlace("gardet");
		return SMHIDataReader.finalString;
	}

	@RequestMapping("/grimsta/today") // pvt.dsv.su.se/Group04/place/grimsta/today
	public String grimsta() {
		getJSONObjectForPlace("grimsta");
		return SMHIDataReader.finalString;
	}

	@RequestMapping("/jarvafaltet/today") // pvt.dsv.su.se/Group04/place/jarvafaltet/today
	public String jarvafaltet() {
		getJSONObjectForPlace("jarvafaltet");
		return SMHIDataReader.finalString;
	}

	@RequestMapping("/nackareservatet/today") // pvt.dsv.su.se/Group04/place/nackareservatet/today
	public String nackareservatet() {
		getJSONObjectForPlace("nackareservatet");
		return SMHIDataReader.finalString;
	}

	@RequestMapping("/tyrestanationalpark/today") // pvt.dsv.su.se/Group04/place/tyrestanationalpark/today
	public String tyrestanationalpark() {
		getJSONObjectForPlace("tyrestanationalpark");
		return SMHIDataReader.finalString;

	}

	// private JSONArray getJSONObjectForPlace(String placeName){
	private String getJSONObjectForPlace(String placeName) {
		// JSONObject obj = new JSONObject();
		JSONArray array = new JSONArray();

		if (!checkDbDate() && getCurrentTime().compareTo("06:00:00") > 0) { // CHOOSE
																			// APPROPRIATE
																			// TIME
																			// FOR
																			// THRESHOLD!!
			updateDbDate();
		}
		SMHIDataReader smhi = new SMHIDataReader();
		// obj = smhi.getPlaceDataForTonight(placeName);
		array = smhi.getPlaceDataForTonight(placeName);

		// return obj;
		return array.toString();
	}

	private boolean checkDbDate() {
		boolean dbUpdatedStatus = false;
		connMan = ConnectionManager.getInstance();
		String sql = "SELECT Date FROM Place WHERE Name = 'Gardet'";
		String dbDate = null;

		try {
			Connection conn = connMan.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			rs.next();
			dbDate = rs.getString("Date");

			dbUpdatedStatus = checkCurrentDate(dbDate);

		} catch (SQLException e) {
			System.err.println("PlaceManager intans.displayAllRows() kastar exception" + e);
		}

		return dbUpdatedStatus;
	}

	private boolean checkCurrentDate(String dbDate) {
		return getCurrentDate().equals(dbDate);
	}

	private String getCurrentDate() {
		DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT);
		Date today = new Date();

		return dateFormatter.format(today);
	}

	private String getCurrentTime() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

		return sdf.format(cal.getTime());
	}

	private void updateDbDate() {
		String currentDate = getCurrentDate();
		System.err.println(currentDate);
		connMan = ConnectionManager.getInstance();
		String sql = "UPDATE Place SET Date = '" + currentDate + "'";

		try {
			Connection conn = connMan.getConnection();
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			System.err.println("PlaceManager updateDbDate() kastar exception" + e);
		}
	}

	// private String getAllColumnForPlace(String placeName){ // BEHÖVS INTE
	// LÄNGRE
	//
	// connMan = ConnectionManager.getInstance();
	// String sql = "SELECT * FROM Place WHERE Name = '" + placeName + "'";
	// StringBuffer bf = new StringBuffer();
	//
	// try {
	// Connection conn = connMan.getConnection();
	// Statement stmt = conn.createStatement();
	// ResultSet rs = stmt.executeQuery(sql);
	//
	// System.out.println("Place Table:");
	//
	// while (rs.next()) {
	// bf.append(rs.getString("Name") + ": ");
	// bf.append(rs.getDouble("Temperature") +", ");
	// bf.append(rs.getInt("Rainfall") +", ");
	// bf.append(rs.getInt("Cloudiness") +", ");
	// bf.append(rs.getString("Url") + ": ");
	// bf.append(rs.getInt("Grade") + ": ");
	// }
	// if(bf.length() > 2){
	// bf.delete(bf.length()-2, bf.length()-1);
	// }
	//
	// System.out.println(bf.toString());
	// } catch (SQLException e){
	// System.err.println("PlaceManager intans.displayAllRows() kastar
	// exception" + e);
	// }
	//
	// return bf.toString();
	// }

	@RequestMapping("/test") // UPDATE DB MALL
	public String putTest() {
		String placeInfo = null;
		connMan = ConnectionManager.getInstance();
		Random rnd = new Random();
		int i = rnd.nextInt(10);
		String sql = "UPDATE Place SET Temperature = '" + i + "' WHERE Name = 'Gardet'";

		try {
			Connection conn = connMan.getConnection();
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);

			// placeInfo = gardet();
		} catch (SQLException e) {
			System.err.println("PlaceManager intans.displayAllRows() kastar exception" + e);
		}

		return placeInfo;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String displayAllRow() throws SQLException { // visar allt i
														// databasen
		connMan = ConnectionManager.getInstance();
		String sql = "SELECT * FROM Place";
		StringBuffer bf = new StringBuffer();

		try {
			Connection conn = connMan.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			System.out.println("Place Table:");

			while (rs.next()) {
				bf.append(rs.getString("Name") + ": ");
				bf.append(rs.getDouble("Temperature") + ", ");
				bf.append(rs.getInt("Rainfall") + ", ");
				bf.append(rs.getInt("Cloudiness") + ", ");
				bf.append(rs.getString("Url") + ": ");
				bf.append(rs.getInt("Grade") + ": ");
			}
			if (bf.length() > 2) {
				bf.delete(bf.length() - 2, bf.length() - 1);
			}

			System.out.println(bf.toString());
		} catch (SQLException e) {
			System.err.println("PlaceManager intans.displayAllRows() kastar exception" + e);
		}

		return bf.toString();
	}

}
