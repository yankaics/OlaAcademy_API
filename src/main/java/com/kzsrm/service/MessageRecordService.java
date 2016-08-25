package com.kzsrm.service;

import com.kzsrm.baseservice.BaseServiceMybatis;
import com.kzsrm.model.MessageRecord;

public interface MessageRecordService extends
		BaseServiceMybatis<MessageRecord, String> {

	/**
	 * 添加观看记录或发帖
	 * 
	 * @param userId
	 * @param videoId
	 * @param timeSpan
	 */
	void addMessageRecord(int userId, String messageIds) ;
}
