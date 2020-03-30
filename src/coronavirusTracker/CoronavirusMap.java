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
	    		//provinceMarkers.add(new CityMarker(markers.get(counter).getLocation()));
	    	}
	    	else
	    	{
	    		provinceMarkers.add(new CityMarker(x));
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
		return marker;
	}
	
	public void draw() {
	    background(224, 224, 224);
	    map.draw();
	    addKey();	
	}
	
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
	

}