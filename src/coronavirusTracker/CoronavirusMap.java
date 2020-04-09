/*
 * Main map UI and map interactions
 */
package coronavirusTracker;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

//Processing library
import processing.core.PApplet;
//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class CoronavirusMap extends PApplet {
	
	private static final long serialVersionUID = 1L;
	
	// The map
	private UnfoldingMap map;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private Marker lastSelected;
	private List<Marker> markers;
	private List<Marker> provinceMarkers;
	private List<Marker> countryMarkers;
	private String [] topCases;
	
	public void setup() {
		size((int)screenSize.getWidth(), (int)screenSize.getHeight(), OPENGL);
		map = new UnfoldingMap(this, 150, 50, (int)screenSize.getWidth()-250, (int)screenSize.getHeight()-100, new Microsoft.RoadProvider());
		map.setBackgroundColor(255);
	    map.zoomToLevel(3);
	    MapUtils.createDefaultEventDispatcher(this, map);
	    
	    markers = new LinkedList<Marker>();
	    
	    try {
			Parse_feed.readFile();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    List<PointFeature> countriesAffected = Parse_feed.locationFeatures();
	    provinceMarkers = new LinkedList<Marker>();
	    countryMarkers = new LinkedList<Marker>();
	    for(PointFeature x: countriesAffected)
	    {
	    	markers.add(createMarker(x));
	    	if(x.getProperty("isCountry").equals(true))
	    	{
	    		countryMarkers.add(new CountryMarker(x));
	    	}
	    	else
	    	{
	    		provinceMarkers.add(new CityMarker(x));
	    	}
	    }
	    map.addMarkers(countryMarkers);
	    map.addMarkers(provinceMarkers);
	    topCases=top5Cases();
	}
	
	private SimplePointMarker createMarker(PointFeature feature)
	{
		// Create a new SimplePointMarker at the location given by the PointFeature
		SimplePointMarker marker = new SimplePointMarker(feature.getLocation());
		return marker;
	}
	
	//Constantly updates drawing the map
	public void draw() {
	    background(224, 224, 224);
	    map.draw();
	    addKey();	
	}
	
	//Map legend
	private void addKey() 
	{	
		textSize(18);
		fill(0,0,0);
		text(Parse_feed.getDate(), (int)screenSize.getWidth()/2, 30);
		text("COVID-19 Tracker Map", 15, 30);
		textSize(12);
		fill(255, 0, 0, 63);
		ellipse(20, 95, 20, 20);
		fill(0, 0, 0);
		text("Country", 40, 100);
		fill(255, 0, 0, 63);
		rect(20-10, 145-10, 20, 20);
		fill(0, 0, 0);
		text("Province/State", 40, 150);
		textSize(12);
		text("Total Global Cases:\n"+Integer.toString(Parse_feed.getTotalCases()), 10, 250);
		text("Top 5 Most Cases:", 10, 340);
		text(topCases[0]+":", 10, 360);
		text(topCases[1]+":", 10, 380);
		text(topCases[2]+":", 10, 400);
		text(topCases[3]+":", 10, 420);
		text(topCases[4]+":", 10, 440);
	}
	
	//method that gets the top 5 cases through merge sort
	private String [] top5Cases()
	{
		int arr[] = Parse_feed.getTopCases();
        int arrLength = arr.length; 
        String [] topCasesTitles=Parse_feed.getTopCasesTitle();
        int [] sortedPositions=new int [arrLength];
         
        for(int j=0; j<arr.length; j++)
        {
        	sortedPositions[j]=mergeSort.getIndexInSortedArray(arr, arrLength, j); 
        }
        
        mergeSort a= new mergeSort();
        a.sort(arr);

        String [] newTitle=new String [arrLength];
        for(int i=0; i<arrLength; i++)
        {
        	newTitle[sortedPositions[i]]=topCasesTitles[i];
        }
        String [] top5=new String [5];
        //gets top 5 most cases and their title
        for(int i=arr.length-1; i>arr.length-6 ;i--)
        {
        	top5[arr.length-1-i]=newTitle[i]+", "+Integer.toString(arr[i]);
        }
		return top5;
	}
	
	@Override
	public void mouseMoved()
	{
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;
		}
		selectMarkerIfHover(provinceMarkers);
		selectMarkerIfHover(countryMarkers);
	}
	
	private void selectMarkerIfHover(List<Marker> markers)
	{
		for(Marker m: markers)
		{
			if(m.isInside(map, (float)mouseX, (float)mouseY)==true && lastSelected==null)
			{
				m.setSelected(true);
				lastSelected = m;
			}
		}
	}
}