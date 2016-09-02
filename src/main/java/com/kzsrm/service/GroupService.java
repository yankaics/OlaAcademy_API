package com.kzsrm.service;

import java.util.List;

import com.kzsrm.baseservice.BaseServiceMybatis;
import com.kzsrm.model.Group;

public interface GroupService extends BaseServiceMybatis<Group, String> {
	
	/**
	 * 创建群
	 */
	void createGroup(Group group);

	/**
	 * 用户(教师)所创建群列表
	 * 
	 * @return
	 */
	List<Group> getTeacherGroupList(String userId);
	
	/**
	 * 用户(学生)群列表
	 * @param type 1数学 2英语 3 逻辑 4写作
	 * 
	 * @return
	 */
	List<Group> getUserGroupList(String type);
	
}
