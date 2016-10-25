package com.kzsrm.model;

/*
 * 欧拉币 交易 明细
 */

public class CoinHistory {
	
	private int id;
	private int userId;
	private String name; // 交易明细
	private int type;  // 1 签到赠送 2 分享赠送 3 注册赠送 4 会员赠送 5 购课赠送 6 兑换扣除 7 购买抵扣 8完善资料 
	private int dealNum; // 交易额
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public int getDealNum() {
		return dealNum;
	}
	public void setDealNum(int dealNum) {
		this.dealNum = dealNum;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
}
