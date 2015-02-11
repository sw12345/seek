package com.compgc02.team26.event;

public class Event {

	private String eventId, title, postcode;
	private double distance;

	public Event(){

	}
	
	public Event (String id, String title, String postcode, double distance){
		this.eventId = id; // Event id
		this.title = title; // Name of event
		this.postcode = postcode; // Event postcode
		this.distance = distance; // Event distance
		
	}
	
	public String getId(){
		return eventId;		
	}
	
	public void setId(String id){
		this.eventId = id;
	}
	
	public String getTitle(){
		return title;		
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getPostcode(){
		return postcode;
	}
	
	public void setPostcode(String postcode){
		this.postcode = postcode;
	}
	
	public double getDistance(){
		return distance;
	}
	
	public void setDistance(double distance){
		this.distance = distance;
	}
}
