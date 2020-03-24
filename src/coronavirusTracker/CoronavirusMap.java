package coronavirusTracker;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
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
import de.fhpotsdam.unfolding.events.ZoomMapEvent;

public class CoronavirusMap extends PApplet {

	
	// The map
	private UnfoldingMap map;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	public void setup() {
		size((int)screenSize.getWidth(), (int)screenSize.getHeight(), OPENGL);
		map = new UnfoldingMap(this, 200, 50, (int)screenSize.getWidth()-250, (int)screenSize.getHeight()-100, new Microsoft.RoadProvider());
		map.setBackgroundColor(255);
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
	    
	    List<Marker> markers = new ArrayList<Marker>();
	    
	    try {
			Parse_feed.readFile();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    List<PointFeature> countriesAffected = Parse_feed.locationFeatures();
	    for(PointFeature x: countriesAffected)
	    {
	    	markers.add(createMarker(x));
	    }
	    map.addMarkers(markers);
	}
	
	private SimplePointMarker createMarker(PointFeature feature)
	{
		// Create a new SimplePointMarker at the location given by the PointFeature
		SimplePointMarker marker = new SimplePointMarker(feature.getLocation());
		Object cases = feature.getProperty("Confirmed Cases");
		float numOfCases = Float.parseFloat(cases.toString());
		
		
		int red = color(255, 0 ,0, 63);
		marker.setColor(red);
		marker.setRadius((numOfCases*(float)0.005));
		
		return marker;
	}
	
	private float getZoomLV()
	{
		float zoomLV = map.getZoom();
		return zoomLV;
	}
	
	public void draw() {
	    background(10);
	    map.draw();
	    addKey();
	    getZoomLV();
	    //System.out.println(zoomLevel);
	}
	
	private void addKey() 
	{	
		textSize(18);
		text(Parse_feed.getDate(), (int)screenSize.getWidth()/2, 20);
		text("COVID-19 Tracker Map", 15, 60);
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

}
