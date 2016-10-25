package com.kzsrm.service;

import java.util.List;

import com.kzsrm.baseservice.BaseServiceMybatis;
import com.kzsrm.model.CoinHistory;

public interface CoinHistoryService extends
		BaseServiceMybatis<CoinHistory, String> {
	
	/**
	 * 获取签到状态
	 */
	int getCheckInStatus(int userId);
	
	/*
	 * 是否第一次购买
	 * @param type 4 购买会员 5 购买课程
	 */
	int validateFirstPay(int userId, int type);
	
	/**
	 * 获取签到状态
	 */
	CoinHistory getLastestByUser(int userId);
	
	// 每日分享次数
	int getDailyShareCount(int userId);
	
	// 欧拉币明细记录
	List<CoinHistory> getCoinHistoryList(int userId,String date) ;
	
	/**
	 * 欧拉币明细
	 */
	void insertData(CoinHistory dailyAct);
}
