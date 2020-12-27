/*
 * Reads the data from the online data set and stores it in an organized format
 */
package coronavirusTracker;

import java.util.*; 
import java.io.IOException;
import coronavirusTracker.Location_stats;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Parse_feed {
	private static int numberOfCountries;
	private static LinkedList<Location_stats> locationList = new LinkedList<Location_stats>();
	public static String latestDate;
	private static int totalCases;
	private static int [] topCases;
	private static String [] topCasesTitles;
	
	public static void readFile() throws IOException, InterruptedException
	{
		//Obtains data through Http request
		URL url = new URL("https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	    String inputLine;
	    String fullData = "";
	    
	    //Makes a copy of the data stored in fullData
	    while ((inputLine = in.readLine()) != null) {
	    	fullData = fullData+inputLine+"\n";
	    }
	    in.close();
	    
	    //Splits each line and stores each line as an element in dataLine
	    String[] dataLine = fullData.split("\n"); 
	    //Number of different areas included in the data set
	    numberOfCountries = dataLine.length;
	    //Splits the first line (titles of the table) to get the latest date in the data set
	    String[] firstLine = dataLine[0].split(",(?=\\S)");
	    int latestDateIndex = firstLine.length-1;
	    latestDate = firstLine[latestDateIndex];
	    
	    //Prints last line of the data set
	    //System.out.println(dataLine[dataLine.length-1]);
	    //System.out.println(dataLine.length);
	    
	    //Prints each line of the data
	    //for(int i=0; i<dataLine.length;i++)
		//{
	    //	System.out.println(dataLine[i]);
		//}    
	    
	    //Organizes province, country, latitude, longitude and cases into locationStat by splitting commas in each dataLine
	    for(int i = 1; i<dataLine.length; i++)
		{
			String[] split_data = dataLine[i].split(",(?=\\S)");
			
			Location_stats locationStat=new Location_stats();
			locationStat.setProvince(split_data[0]);
			locationStat.setCountry(split_data[1]);
			locationStat.setLatitude(split_data[2]);
			locationStat.setLongitude(split_data[3]);
			locationStat.setCases(Integer.parseInt(split_data[latestDateIndex]));
			
			locationList.add(locationStat);
		}
	    
	}
	
	//Adds properties for your each country to a list of PointFeatures so they can be added on the map as markers in CoronavirusMap
	public static List<PointFeature> locationFeatures() {
		List<PointFeature> features = new ArrayList<PointFeature>();
		PointFeature point;
		topCases = new int[numberOfCountries];
		topCasesTitles = new String[numberOfCountries];
		
	    for(int i = 0; i<numberOfCountries-1; i++)
	    {
	    	//if latitude or longitude field is empty, do nothing
	    	if(locationList.get(i).getLatitude().equals("") || locationList.get(i).getLongitude().equals(""))
	    	{
	    	}
	    	//if latitude or longitude field is not empty, classifies as country or province/state and then adds it as a marker
	    	else
	    	{
	    	Location coordinates = new Location(Float.parseFloat(locationList.get(i).getLatitude()), Float.parseFloat(locationList.get(i).getLongitude()));
	    	
			point = new PointFeature(coordinates);
			features.add(point);
			
			int numOfCases = locationList.get(i).getCases();
			topCases[i] = numOfCases;
			point.putProperty("Confirmed Cases", numOfCases);
			totalCases += numOfCases;
			
			//is a country
			if(locationList.get(i).getProvince().equals(""))
			{
				String locTitle = locationList.get(i).getCountry();
				topCasesTitles[i] = locTitle;
				point.putProperty("Title", locTitle);
				point.putProperty("isCountry", true);
			}
			//is a state/province
			else
			{
				String locTitle = locationList.get(i).getProvince() + ", " + locationList.get(i).getCountry();
				topCasesTitles[i] = locTitle;
		    	point.putProperty("Title", locTitle);
		    	point.putProperty("isCountry", false);
			}
	    	}
	    }
		return features;
	}

	public static String getDate()
	{
		return latestDate;
	}
	public static int getTotalCases()
	{
		return totalCases;
	}
	
	public static int [] getTopCases()
	{
		return topCases;
	}
	public static String [] getTopCasesTitle()
	{
		return topCasesTitles;
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		readFile();
		locationFeatures();
		getTopCases();
		
	}
}
