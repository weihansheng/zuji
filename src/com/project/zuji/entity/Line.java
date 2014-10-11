package com.project.zuji.entity;

public class Line {
	private Integer id;
	private String lineStr;
	private String time;
	private String aveSpeed;
	private String runTime;
	private String distance;
	private String tripMethod;
	public Line() {

	}
	public Line(String lineStr, String time, String aveSpeed, Integer listid
			,String distance,String runTime,String tripMethod) {
		super();
		this.lineStr = lineStr;
		this.time = time;
		this.aveSpeed = aveSpeed;
		this.id = listid;
		this.runTime=runTime;
		this.distance=distance;
		this.tripMethod=tripMethod;
	}
	
	public String getTripMethod() {
		return tripMethod;
	}
	public void setTripMethod(String tripMethod) {
		this.tripMethod = tripMethod;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLineStr() {
		return lineStr;
	}
	public void setLineStr(String lineStr) {
		this.lineStr = lineStr;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getAveSpeed() {
		return aveSpeed;
	}
	public void setAveSpeed(String aveSpeed) {
		this.aveSpeed = aveSpeed;
	}
	public String getRunTime() {
		return runTime;
	}
	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	
}
