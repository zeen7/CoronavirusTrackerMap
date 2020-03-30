package coronavirusTracker;

import java.util.*; 

import java.io.IOException;
import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
import coronavirusTracker.Location_stats;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import processing.core.PApplet;
import processing.data.XML;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Parse_feed {
	
	private static String DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Confirmed.csv";
	private static int numberOfCountries;
	private static LinkedList<Location_stats> locationList = new LinkedList<Location_stats>();
	public static String latestDate;

	
	public Parse_feed() {
		
	}
	
	public static void readFile() throws IOException, InterruptedException
	{
		URL url = new URL("https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	    String inputLine;
	    int lineNumber=0;
	    String fullData="";
	    
	    LinkedList<String> province = new LinkedList<String>(); 
		LinkedList<String> countries = new LinkedList<String>(); 
		LinkedList<String> cases = new LinkedList<String>(); 
		LinkedList<String> longitude = new LinkedList<String>(); 
		LinkedList<String> latitude = new LinkedList<String>(); 
	    LinkedList<String> lines=new LinkedList<String>();
	    
	    while ((inputLine = in.readLine()) != null) {
	    	fullData=fullData+inputLine+"\n";
	    }
	    in.close();
	    
	    //System.out.println(fullData);
	    //String[] dataLine = fullData.split(System.lineSeparator()); 
	    String[] dataLine = fullData.split("\n"); 
	    numberOfCountries=dataLine.length;
	    String[] firstLine = dataLine[0].split(",(?=\\S)");
	    int latestDateIndex = firstLine.length-1;
	    latestDate=firstLine[latestDateIndex];
	    
	    //System.out.println(dataLine[dataLine.length-1]);
	    for(int i=0; i<dataLine.length;i++)
		{
	    	//System.out.println(dataLine[i]);
		}    
	    
	    for(int i=1; i<dataLine.length;i++)
		{
			String[] split_data=dataLine[i].split(",(?=\\S)");
			
			Location_stats locationStat=new Location_stats();
			locationStat.setProvince(split_data[0]);
			locationStat.setCountry(split_data[1]);
			locationStat.setLatitude(split_data[2]);
			locationStat.setLongitude(split_data[3]);
			locationStat.setCases(Integer.parseInt(split_data[latestDateIndex]));
			
			locationList.add(locationStat);
			
			//province.add(split_data[0]);
			//countries.add(split_data[1]);
			//latitude.add(split_data[2]);
			//longitude.add(split_data[3]);
			//cases.add(split_data[latestDateIndex]);
			//System.out.println(split_data[0]+" "+split_data[1]+" "+split_data[latestDateIndex]);
			//System.out.println(locationStat);	
			
		}
		//For Java 13
		//HttpClient client= HttpClient.newHttpClient();
		//HttpRequest request= HttpRequest.newBuilder().uri(URI.create(DATA_URL)).build();
		//HttpResponse<String> httpResponse= client.send(request, HttpResponse.BodyHandlers.ofString());
		//System.out.println(httpResponse.body());
		//System.out.println(System.getProperty("java.runtime.version"));
	}
	
	public static List<PointFeature> locationFeatures() {
		List<PointFeature> features = new ArrayList<PointFeature>();
		PointFeature point;
	    for(int i=0;i<numberOfCountries-1;i++)
	    {
	    	Location coordinates=new Location(Float.parseFloat(locationList.get(i).getLatitude()), Float.parseFloat(locationList.get(i).getLongitude()));
	    
			point = new PointFeature(coordinates);
			features.add(point);
			if(locationList.get(i).getProvince().equals(""))
			{
				String locTitle=locationList.get(i).getCountry();
				point.putProperty("Title", locTitle);
				int numOfCases=locationList.get(i).getCases();
				point.putProperty("Confirmed Cases", numOfCases);
				point.putProperty("isCountry", true);
			}
			else
			{
				String locTitle=locationList.get(i).getProvince()+", "+locationList.get(i).getCountry();
		    	point.putProperty("Title", locTitle);
		    	int numOfCases=locationList.get(i).getCases();
		    	point.putProperty("Confirmed Cases", numOfCases);
		    	point.putProperty("isCountry", false);
			}
	    }
		return features;
	}

	public static String getDate()
	{
		return latestDate;
	}

	
	
	public static void main(String[] args) throws IOException, InterruptedException {
		readFile();
		locationFeatures();
		Location_stats locationStat=new Location_stats();
	}
}