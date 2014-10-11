package com.project.zuji.entity;

public class List_LineInfo {
	private String end_latlng;
	private String start_latlng;

	public List_LineInfo(String end_latlng, String start_latlng) {
		super();
		this.end_latlng = end_latlng;
		this.start_latlng = start_latlng;
	}

	public String getEnd_latlng() {
		return end_latlng;
	}

	public void setEnd_latlng(String end_latlng) {
		this.end_latlng = end_latlng;
	}

	public String getStart_latlng() {
		return start_latlng;
	}

	public void setStart_latlng(String start_latlng) {
		this.start_latlng = start_latlng;
	}

	public List_LineInfo() {

	}

	@Override
	public String toString() {
		return "List_LineInfo [end_latlng=" + end_latlng + ", start_latlng="
				+ start_latlng + "]";
	}

}
