package br.com.bradesco.lelis;

public class Trend {
	public String trendname;
	public String rulesUrl;
	
	public Trend(String trendname, String rulesUrl) {
		this.trendname = trendname;
		this.rulesUrl = rulesUrl;
	}
	
	public void ClearProps() {
		this.trendname = "";
		this.rulesUrl = "";
	}
}
