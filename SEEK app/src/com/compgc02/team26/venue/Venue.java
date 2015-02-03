package com.compgc02.team26.venue;

public class Venue {

	private String venueId, title, type, postcode, maxcap;
	private double distance;

	public Venue(){

	}
	
	public Venue (String id, String title, String type, String postcode, String maxcap, double distance){
		this.venueId = id;
		this.title = title;
		this.type = type;
		this.maxcap = maxcap;
		this.postcode = postcode;
		this.distance = distance;
		
	}
	
	public String getId(){
		return venueId;		
	}
	
	public void setId(String id){
		this.venueId = id;
	}
	
	public String getTitle(){
		return title;		
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getType(){
		return type;		
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public String getPostcode(){
		return postcode;
	}
	
	public void setPostcode(String postcode){
		this.postcode = postcode;
	}
	
	public String getMaxcap(){
		return maxcap;		
	}
	
	public void setMaxcap(String maxcap){
		this.maxcap = maxcap;
	}
	public double getDistance(){
		return distance;
	}
	
	public void setDistance(double distance){
		this.distance = distance;
	}
}
