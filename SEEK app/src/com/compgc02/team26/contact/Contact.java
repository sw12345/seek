package com.compgc02.team26.contact;

public class Contact {

	private String userId, name, age, interests;
	private double distance;

	public Contact(){

	}
	
	public Contact (String id, String name, String age, String interests, double distance){
		this.userId = id;
		this.name = name;
		this.age = age;
		this.interests = interests;
		this.distance = distance;
		
	}
	
	public String getId(){
		return userId;		
	}
	
	public void setId(String id){
		this.userId = id;
	}
	
	public String getName(){
		return name;		
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getInterests(){
		return interests;		
	}
	
	public void setInterests(String interests){
		this.interests = interests;
	}
	
	public String getAge(){
		return age;
	}
	
	public void setAge(String age){
		this.age = age;
	}
	
	public double getDistance(){
		return distance;
	}
	
	public void setDistance(double distance){
		this.distance = distance;
	}
}
