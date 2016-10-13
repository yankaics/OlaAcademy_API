package com.kzsrm.service;

import java.util.List;

import com.kzsrm.baseservice.BaseServiceMybatis;
import com.kzsrm.model.AttentionShip;

public interface AttentionShipService extends
		BaseServiceMybatis<AttentionShip, String> {
	/**
	 * 添加用户关注关系
	 * 
	 */
	void insertData(AttentionShip ship);

	/**
	 * 是否已加入群
	 * 
	 * @return
	 */
	List<AttentionShip> getByParams(int attendId, int attendedId);

	/**
	 * 取消关注
	 * 
	 * @return
	 */
	void delete(String shipId);
}
