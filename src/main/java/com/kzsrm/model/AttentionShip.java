package com.kzsrm.model;

import java.util.Date;

public class AttentionShip {
	
	private int id;
	private int attendId;
	private int attendedId;
	private int defriend; // (attendedId将attendId) 拉入黑名单
	private Date createTime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAttendId() {
		return attendId;
	}
	public void setAttendId(int attendId) {
		this.attendId = attendId;
	}
	public int getAttendedId() {
		return attendedId;
	}
	public void setAttendedId(int attendedId) {
		this.attendedId = attendedId;
	}
	public int getDefriend() {
		return defriend;
	}
	public void setDefriend(int defriend) {
		this.defriend = defriend;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
