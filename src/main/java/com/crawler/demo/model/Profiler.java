package com.crawler.demo.model;

public class Profiler {
	
	private String rowCount;
	private String nullCount;
	private String distinctCount;
	private String min;
	private String max;
	
	public Profiler(String rowCount, String nullCount, String distinctCount, String min, String max) {
		super();
		this.rowCount = rowCount;
		this.nullCount = nullCount;
		this.distinctCount = distinctCount;
		this.min = min;
		this.max = max;
	}
	public String getRowCount() {
		return rowCount;
	}
	public void setRowCount(String rowCount) {
		this.rowCount = rowCount;
	}
	public String getNullCount() {
		return nullCount;
	}
	public void setNullCount(String nullCount) {
		this.nullCount = nullCount;
	}
	public String getDistinctCount() {
		return distinctCount;
	}
	public void setDistinctCount(String distinctCount) {
		this.distinctCount = distinctCount;
	}
	public String getMin() {
		return min;
	}
	public void setMin(String min) {
		this.min = min;
	}
	public String getMax() {
		return max;
	}
	public void setMax(String max) {
		this.max = max;
	}
	
	
	
}
