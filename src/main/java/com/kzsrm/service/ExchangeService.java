package com.kzsrm.service;

import java.util.List;

import com.kzsrm.baseservice.BaseServiceMybatis;
import com.kzsrm.model.Exchange;

public interface ExchangeService extends BaseServiceMybatis<Exchange, String> {
	/**
	 * 欧拉币兑换
	 * 
	 * @param userId
	 * @param videoId
	 * @param timeSpan
	 */
	void insertData(Exchange exchange);

	/**
	 * 是否已兑换
	 * 
	 * @return
	 */
	List<Exchange> getByParams(String userId, String objectId,String type);

}
