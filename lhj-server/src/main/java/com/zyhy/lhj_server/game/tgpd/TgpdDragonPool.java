package com.zyhy.lhj_server.game.tgpd;

public class TgpdDragonPool {

	public static final String GRAND = "grand";
	public static final String MAJOR = "major";
	public static final String MINOR = "minor";
	public static final String MINI = "mini";
	
	private double grand;
	private double major;
	private double minor;
	private double mini;

	public double getGrand() {
		return grand;
	}
	public void setGrand(double grand) {
		this.grand = grand;
	}
	public double getMajor() {
		return major;
	}
	public void setMajor(double major) {
		this.major = major;
	}
	public double getMinor() {
		return minor;
	}
	public void setMinor(double minor) {
		this.minor = minor;
	}
	public double getMini() {
		return mini;
	}
	public void setMini(double mini) {
		this.mini = mini;
	}
	@Override
	public String toString() {
		return "TgpdDragonPool [grand=" + grand + ", major=" + major + ", minor=" + minor + ", mini=" + mini + "]";
	}
}
