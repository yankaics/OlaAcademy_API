package com.kzsrm.service;

import java.util.List;

import com.kzsrm.baseservice.BaseServiceMybatis;
import com.kzsrm.model.Broadcast;

public interface BroadcastService extends BaseServiceMybatis<Broadcast, String> {

	/**
	 * 直播列表
	 * 
	 * @return
	 */
	List<Broadcast> getBroadcastList(String broadcastId, int pageCount);

}
