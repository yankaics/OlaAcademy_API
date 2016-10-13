package com.kzsrm.model;

/*
 * 每日签到 分享
 */

public class DailyAct {
	
	private int id;
	private int userId;
	private int checkin;
	private int wechat;
	private int wetimeline;
	private int sina;
	private int qq;
	private int qzone;
	private String date;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getWechat() {
		return wechat;
	}
	public int getCheckin() {
		return checkin;
	}
	public void setCheckin(int checkin) {
		this.checkin = checkin;
	}
	public void setWechat(int wechat) {
		this.wechat = wechat;
	}
	public int getWetimeline() {
		return wetimeline;
	}
	public void setWetimeline(int wetimeline) {
		this.wetimeline = wetimeline;
	}
	public int getSina() {
		return sina;
	}
	public void setSina(int sina) {
		this.sina = sina;
	}
	public int getQq() {
		return qq;
	}
	public void setQq(int qq) {
		this.qq = qq;
	}
	public int getQzone() {
		return qzone;
	}
	public void setQzone(int qzone) {
		this.qzone = qzone;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
}
