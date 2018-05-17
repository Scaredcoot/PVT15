package StarGaze;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SMHIDataReader {
	
	private JSONArray tempJsonArray = new JSONArray(); //hold all parameters for 6 days forward, on correct times (00:00)
	private JSONArray sortedParametersArray = new JSONArray(); //holds sorted parameters for chosen days on chosen time
	static String finalString;
	
	
	//------------------------------Contructors------------------------------------------
	
	
	
	//------------------------------Methods------------------------------------------
	
	public JSONArray getPlaceDataForTonight(String placeName){ //kallas från PlaceManager (mellan metod till requestData(){...} )
		//JSONObject obj = null;
		JSONArray array = null;
		
			try {
				
				requestData(placeName);
				//obj = sortedParametersArray.getJSONObject(0);
				array = sortedParametersArray;
				
			} catch (IOException e) {
				System.out.println("SMHIDataReader.getPlaceDataForTonight() kastar IOException");
				e.printStackTrace();
			} catch (JSONException e) {
				System.out.println("SMHIDataReader.getPlaceDataForTonight() kastar JSONException");
				e.printStackTrace();
			}
			//System.out.println(sortedParametersArray.toString());
		//return obj;
			return array;
	}
	
	public void requestData (String location) throws IOException, JSONException //call this method and pass in String
	{
		String url = null;
		
		switch (location){
			case "gardet": 
			 url = "http://opendata-download-metfcst.smhi.se/api/category/pmp2g/version/3/geotype/point/lon/18.096/lat/59.343/data.json";
			break;
			case "grimsta":
			url = "http://opendata-download-metfcst.smhi.se/api/category/pmp2g/version/3/geotype/point/lon/17.859/lat/59.359/data.json";
			break;
			case "jarvafaltet":
			url = "http://opendata-download-metfcst.smhi.se/api/category/pmp2g/version/3/geotype/point/lon/18.0/lat/59.383/data.json";
			break;
			case "nackareservatet":
			url ="http://opendata-download-metfcst.smhi.se/api/category/pmp2g/version/3/geotype/point/lon/18.146/lat/59.284/data.json";
			break;
			case "tyrestanationalpark":
			url = "http://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/lon/18.269/lat/59.184/data.json";
			break;
			default: return;
		}
		
		clearArrays();
		dataReader(url);
	} 
	
	private void clearArrays ()
	{
		for (int i = 0; i < tempJsonArray.length(); i++) {
			tempJsonArray.remove(i);
		}
		for (int i = 0; i < sortedParametersArray.length(); i++) {
			sortedParametersArray.remove(i);
		}
	}
	
	private void dataReader(String url) throws IOException, JSONException {
		
		JSONObject parameterObject  = readJsonFromUrl (url);
		JSONArray timeSeriesArray = parameterObject.getJSONArray("timeSeries"); 
		
		int j = 0;
		for (int i = 0; i < timeSeriesArray.length(); i++) {
			
			JSONObject timeObject = timeSeriesArray.getJSONObject(i);
			String validTime = timeObject.getString("validTime");
			if(validTime.contains("T00:00:00Z") & j < 6){
				tempJsonArray.put(j, timeObject);
				j++;
				
			}
		}
		sortParameters();
	}
	
	private void sortParameters () throws JSONException {
		
		for (int k = 0; k < tempJsonArray.length(); k++) {
			
			JSONObject obj = new JSONObject();
			JSONArray newParametersArray = new JSONArray();
			int cloudiness = 0;
			
			String validTime = tempJsonArray.getJSONObject(k).getString("validTime");
			JSONArray parametersArray = tempJsonArray.getJSONObject(k).getJSONArray("parameters");
	
			obj.put("validTime", validTime).put("parameters", newParametersArray);
			
			sortedParametersArray.put(k, obj);
		
			for (int i = 0; i < parametersArray.length(); i++) {
				JSONObject parameter = parametersArray.getJSONObject(i); 
				String parameterKey = parameter.getString("name");
				
				switch (parameterKey){
					case "t": 
					double temp = parameter.getJSONArray("values").getDouble(0);	
					newParametersArray.put(new JSONObject().put("temp", temp));
					break;
					case "tcc_mean":
					int tcc = parameter.getJSONArray("values").getInt(0);
					cloudiness +=tcc;
					break;
					case "lcc_mean":
					int lcc = parameter.getJSONArray("values").getInt(0);
					cloudiness +=lcc;
					break;
					case "mcc_mean":
					int mcc = parameter.getJSONArray("values").getInt(0);
					cloudiness +=mcc;
					break;
					case "hcc_mean":
					int hcc = parameter.getJSONArray("values").getInt(0);
					cloudiness +=hcc;
					break;
					case "pmean": //percipitation mean
					double rainfall = parameter.getJSONArray("values").getDouble(0);	
					newParametersArray.put(new JSONObject().put("rainfall",rainfall));
					break;
					default: break;
				}
				
			}
			cloudiness = cloudiness / 4;
			newParametersArray.put(new JSONObject().put("cloudiness",cloudiness));
		}
		
		formatForecastArray();
	}
	
	private void formatForecastArray () throws JSONException
	{	
		StringBuilder testString = new StringBuilder();
		testString.append("{\"days\":{");
		for (int k = 0; k < sortedParametersArray.length(); k++) {
		
		JSONArray parametersArray = sortedParametersArray.getJSONObject(k).getJSONArray("parameters");
		
		double temp = parametersArray.getJSONObject(0).getDouble("temp");
		double rainfall = parametersArray.getJSONObject(1).getDouble("rainfall");
		int cloudiness = parametersArray.getJSONObject(2).getInt("cloudiness");
		ReflectionSensor sensor = new ReflectionSensor();
		
		if (k== 0)
		{
			int lighReflection = sensor.getSensorData(rainfall);
			String grade = setGrade(parametersArray,temp,rainfall,cloudiness,lighReflection);
			//parametersArray.put(new JSONObject().put("grade",grade));
			testString.append("\"day1\":{\"temp\":\"" + temp + "\",\"rainfall\":\"" + rainfall + "\",\"cloudiness\":\"" + cloudiness + "\",\"grade\":\""  + grade + "\"},");
			//System.out.println(sortedParametersArray); for testing
		}else {
			String validTime = sortedParametersArray.getJSONObject(k).getString("validTime");
			String weekDay = getWeekDay(validTime);
			int lighReflection = 0;
			String grade = setGrade(parametersArray,temp,rainfall,cloudiness,lighReflection);
			//sortedParametersArray.put(k, new JSONObject().put("day"+(k+1), grade));
			if(k == 5) {
				testString.append("\"day" + (k+1) + "\":\"" + grade + "\"}}");
			} else {
				testString.append("\"day" + (k+1) + "\":\"" + grade + "\",");
			}
			}
		}
		System.err.println(testString);
		finalString = testString.toString();
	}
	
	private String setGrade (JSONArray ParametersArray, double temp, double rainfall, int cloudiness, int lighReflection )
	{
		String grade = null;
		
		if (temp < 10.0 && rainfall == 0 && cloudiness < 1 && lighReflection < 2)
		{
			grade = "Excellent";
			return grade;
		}
		if (temp < 15.0 && rainfall == 0 && cloudiness < 1 && lighReflection < 3)
		{
			grade = "Good";
			return grade;
		}
		if (temp < 20.0 && rainfall == 0 && cloudiness < 2 && lighReflection < 3)
		{
			grade = "Ok";
			return grade;
		}
		else {grade = "Bad";
		return grade;
	}
	}
	
	private String getWeekDay(String validTime) throws JSONException
	{
		DateHandler insta  = new DateHandler();
		String weekDay = insta.getWeekDay (validTime);
		return weekDay;
	}	
	
	private JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		String text = readStringFromUrl(url);
		return new JSONObject(text); //converts URL string to new Json Object
	}
	
	private String readStringFromUrl(String url) throws IOException {

		InputStream inputStream = new URL(url).openStream(); //opens the connection 
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
			StringBuilder stringBuilder = new StringBuilder();
			int cp;
			while ((cp = reader.read()) != -1) {
				stringBuilder.append((char) cp);
			}
			return stringBuilder.toString(); //returns the content of the URL as a string
		} finally {
			inputStream.close();
		}
		
	}
}
		
