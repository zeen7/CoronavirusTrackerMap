package coronavirusTracker;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;

public class CountryMarker extends CommonMarker {

	public CountryMarker(Location location) {
		super(location);
	}
	
	
	public CountryMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
	}
	@Override
	public void drawMarker(PGraphics pg, float x, float y) {
		// Save previous drawing style
		pg.pushStyle();
		pg.fill(255, 0, 0, 63);
		
		 if(getCases()<=0)
			{
		    	pg.ellipse(x, y, 0, 0);
			}
			else if(getCases()>0 && getCases()<11)
			{
				int width=5/2;
				pg.ellipse(x, y, width, width);
			}
			else if (getCases()>10 && getCases()<51)
			{
				int width=10/2;
				pg.ellipse(x, y, width, width);
			}
			else if (getCases()>50 && getCases()<201)
			{
				int width=15/2;
				pg.ellipse(x, y, width, width);
			}
			else if(getCases()>200 && getCases()<401)
			{
				int width=20/2;
				pg.ellipse(x, y, width, width);
			}
			else if(getCases()>400 && getCases()<801)
			{
				int width=25/2;
				pg.ellipse(x, y, width, width);
			}
			else if(getCases()>800 && getCases()<1601)
			{
				int width=30/2;
				pg.ellipse(x, y, width, width);
			}
			else if(getCases()>1600 && getCases()<3001)
			{
				int width=35/2;
				pg.ellipse(x, y, width, width);
			}
			else if(getCases()>3000 && getCases()<17001)
			{
				int width=40/2;
				pg.ellipse(x, y, width, width);
			}
			else if(getCases()>17000 && getCases()<50001)
			{
				int width=45/2;
				pg.ellipse(x, y, width, width);
			}
			else if(getCases()>50000 && getCases()<100001)
			{
				int width=50/2;
				pg.ellipse(x, y, width, width);
			}
			else
			{
				int width=55/2;
				pg.ellipse(x, y, width, width);
			}
		
		// Restore previous drawing style
		pg.popStyle();
	}
	
	public String getCountry()
	{
		return getStringProperty("Title");
	}
	
	public float getCases()
	{
		return Float.parseFloat((getProperty("Confirmed Cases")).toString());
	}
	
	@Override
	public void showTitle(PGraphics pg, float x, float y) {
		String title=(String)(" "+getCountry()+": "+Math.round(getCases())+" cases"+" ");
		float textWidth=pg.textWidth(title);
		pg.fill(255, 255, 204);
		pg.rect(x+15,y-15,textWidth, 20);
		pg.fill(0, 0, 0);
		pg.text(title, x+15, y);
	}
}
