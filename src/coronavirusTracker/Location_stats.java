package coronavirusTracker;

import coronavirusTracker.Parse_feed;

public class Location_stats {

	public Location_stats() {
		// TODO Auto-generated constructor stub
	}
	private String province;
	private String country;
	private int cases;
	private String latitude;
	private String longitude;

	public void setProvince(String province) {
		this.province=province;
	}
	
	public String getProvince() {
		return province;
	}
	
	public void setLatitude(String latitude) {
		this.latitude=latitude;
	}
	public String getLatitude() {
		return latitude;
	}
	
	public void setLongitude(String longitude) {
		this.longitude=longitude;
	}
	
	public String getLongitude() {
		return longitude;
	}
	
	public void setCountry(String country) {
		this.country=country;
	}
	
	public String getCountry() {
		return country;
	}
	
	public void setCases(int cases) {
		this.cases=cases;
	}
	
	public int getCases() {
		return cases;
	}
	
	@Override
	public String toString()
	{
		return "Location_stats{" +
				"Province='"+ province +'\'' +
				", Country='" + country + '\'' +
				", Latitude=" + latitude +
				", Longitude=" + longitude +
				", Total Cases=" + cases +
				'}';
	}	
	
	
}
