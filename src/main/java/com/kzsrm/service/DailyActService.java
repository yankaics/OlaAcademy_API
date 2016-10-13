package com.kzsrm.service;

import com.kzsrm.baseservice.BaseServiceMybatis;
import com.kzsrm.model.DailyAct;

public interface DailyActService extends
		BaseServiceMybatis<DailyAct, String> {
	
	/**
	 * 获取签到状态
	 */
	int getCheckInStatus(int userId);
	
	/**
	 * 获取签到状态
	 */
	DailyAct getLastestByUser(int userId);
	
	/**
	 * 签到
	 */
	void signIn(DailyAct dailyAct);
}
