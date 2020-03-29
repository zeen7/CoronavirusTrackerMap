package coronavirusTracker;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import processing.core.PApplet;
import processing.core.PGraphics;
//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;


public class CoronavirusMap extends PApplet {
	
	// The map
	private UnfoldingMap map;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private Marker lastSelected;
	private List<Marker> markers;
	private List<Marker> provinceMarkers;
	private List<Marker> countryMarkers;
	
	public void setup() {
		size((int)screenSize.getWidth(), (int)screenSize.getHeight(), OPENGL);
		map = new UnfoldingMap(this, 200, 50, (int)screenSize.getWidth()-250, (int)screenSize.getHeight()-100, new Microsoft.RoadProvider());
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
	    		provinceMarkers.add(new CityMarker(x));
	    		//provinceMarkers.add(new CityMarker(markers.get(counter).getLocation()));
	    	}
	    	else
	    	{
	    		countryMarkers.add(new CountryMarker(x));
	    		//countryMarkers.add(new CountryMarker(markers.get(counter).getLocation()));
	    	}
	    }
	    map.addMarkers(countryMarkers);
	    map.addMarkers(provinceMarkers);
	}
	
	private SimplePointMarker createMarker(PointFeature feature)
	{
		// Create a new SimplePointMarker at the location given by the PointFeature
		SimplePointMarker marker = new SimplePointMarker(feature.getLocation());
		Object cases = feature.getProperty("Confirmed Cases");
		float numOfCases = Float.parseFloat(cases.toString());
		
		int red = color(255, 0 ,0, 63);
		marker.setColor(red);
		if(numOfCases<=0)
		{
			marker.setRadius(0);
		}
		else if(numOfCases>0 && numOfCases<11)
		{
			marker.setRadius(5);
		}
		else if (numOfCases>10 && numOfCases<51)
		{
			marker.setRadius(10);
		}
		else if (numOfCases>50 && numOfCases<201)
		{
			marker.setRadius(15);
		}
		else if(numOfCases>200 && numOfCases<401)
		{
			marker.setRadius(20);
		}
		else if(numOfCases>400 && numOfCases<801)
		{
			marker.setRadius(25);
		}
		else if(numOfCases>800 && numOfCases<1601)
		{
			marker.setRadius(35);
		}
		else if(numOfCases>1600 && numOfCases<3001)
		{
			marker.setRadius(40);
		}
		else if(numOfCases>3000 && numOfCases<17001)
		{
			marker.setRadius(50);
		}
		else if(numOfCases>17000 && numOfCases<50001)
		{
			marker.setRadius(55);
		}
		else if(numOfCases>50000 && numOfCases<100001)
		{
			marker.setRadius(60);
		}
		else
		{
			marker.setRadius(65);
		}
		return marker;
	}
	
	public void draw() {
	    background(10);
	    map.draw();
	    addKey();	
	}
	
	private void addKey() 
	{	
		textSize(18);
		text(Parse_feed.getDate(), (int)screenSize.getWidth()/2, 30);
		text("COVID-19 Tracker Map", 15, 30);
		textSize(12);
		fill(0, 0, 255);
		ellipse(20, 95, 10, 10);
		fill(255, 255, 255);
		text("Below 4.0 Magnitude", 40, 100);
		fill(255, 255, 0);
		ellipse(20, 145, 15, 15);
		fill(255, 255, 255);
		text("4.0-4.9 Magnitude", 40, 150);
		fill(255, 0, 0);
		ellipse(20, 195, 20, 20);
		fill(255, 255, 255);
		text("Above 5.0 Magnitude", 40, 200);
	}
	
	@Override
	public void mouseMoved()
	{
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;
		}
		selectMarkerIfHover(markers);
	}

	private void selectMarkerIfHover(List<Marker> markers)
	{
		// TODO: Implement this method
		for(Marker m: markers)
		{
			if(m.isInside(map, (float)mouseX, (float)mouseY)==true && lastSelected==null)
			{
				m.setSelected(true);
				lastSelected = m;
			}
		}
	}
	public void showTitle(float x, float y, Marker m)
	{
			fill(0,0,0);
			text((String)m.getProperty("Confirmed Cases"), x, y);
	}

}