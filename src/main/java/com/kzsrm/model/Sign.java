package com.kzsrm.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author wwy
 *
 */
public class Sign {
	private int id;
	private int uid;
	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	private Date lastSignDay;
	private Date startSignDay;
	private int payCoin;//充值所得蚂蚁币
	private String email;
	private String phone;
	private int signTotalNum;//打卡总数

	public int getSignTotalNum() {
		return signTotalNum;
	}

	public void setSignTotalNum(int signTotalNum) {
		this.signTotalNum = signTotalNum;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getPayCoin() {
		return payCoin;
	}

	public void setPayCoin(int payCoin) {
		this.payCoin = payCoin;
	}

	public Date getStartSignDay() {
		return startSignDay;
	}

	public void setStartSignDay(Date startSignDay) {
		this.startSignDay = startSignDay;
	}

	private int antCoin;
	public int getAntCoin() {
		return antCoin;
	}

	public void setAntCoin(int antCoin) {
		this.antCoin = antCoin;
	}

	private int signNum;

	public int getSignNum() {
		return signNum;
	}

	public void setSignNum(int signNum) {
		this.signNum = signNum;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getLastSignDay() {
		return lastSignDay;
	}

	public void setLastSignDay(Date lastSignDay) {
		this.lastSignDay = lastSignDay;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "[id=" + id + ",uid="+uid+",lastSignDay=" + lastSignDay + ",startSignDay=" + startSignDay
				+",antCoin = " + antCoin + ", signNum=" + signNum + ",phone="+phone+",email="+email+",signTotalNum="+signTotalNum+"]";
	}
}
