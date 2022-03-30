package com.crawler.demo.model;

public class ConnectionEstablish{
	private String hostId;
	private String user;
	private String password;
	
	
	public ConnectionEstablish(String hostId, String user, String password) {
		super();
		this.hostId = hostId;
		this.user = user;
		this.password = password;
	}
	public String getHostId() {
		return hostId;
	}
	public void setHostId(String hostId) {
		this.hostId = hostId;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
