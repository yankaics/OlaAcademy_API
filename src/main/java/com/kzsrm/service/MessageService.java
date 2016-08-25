package com.kzsrm.service;

import java.util.List;

import com.kzsrm.baseservice.BaseServiceMybatis;
import com.kzsrm.model.Message;

public interface MessageService  extends BaseServiceMybatis<Message, String> {
	
	/**
	 * 消息列表
	 * @param userId 
	 * @author tianxiaopeng
	 * @return
	 */
	List<Message> getMessageList(int userId,String messageId, int pageCount);
	
	/**
	 * 未读消息列表
	 * 
	 * @param userId
	 * @author tianxiaopeng
	 * @return
	 */
	List<Message> getUnreadMessageList(int userId);

}