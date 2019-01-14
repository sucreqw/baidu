package com.sucre.entity;

public class Vid {
	private String vids;
	private String pids;
	
	public String getVids() {
		return vids;
	}
	public void setVids(String vids) {
		this.vids = vids;
	}
	public String getPids() {
		return pids;
	}
	public void setPids(String pids) {
		this.pids = pids;
	}
	@Override
	public String toString() {
		return "Vid [vids=" + vids + ", pids=" + pids + "]";
	}
	public Vid(String vids) {
		super();
		this.vids = vids;
	}
	public Vid() {
	}
}
