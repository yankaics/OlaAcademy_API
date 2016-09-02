package com.kzsrm.service;

import com.kzsrm.baseservice.BaseServiceMybatis;
import com.kzsrm.model.UserGroup;

public interface UserGroupService extends BaseServiceMybatis<UserGroup, String> {
	/**
	 * 添加用户与群组的关联
	 * 
	 * @param userId
	 * @param videoId
	 * @param timeSpan
	 */
	void insertData(UserGroup usergroup);
	
	/**
	 * 是否已加入群
	 * 
	 * @return
	 */
	UserGroup getByParams(String userId,String groupId);
	
	/**
	 * 退出群
	 * 
	 * @return
	 */
	void delete(String groupId);
}
