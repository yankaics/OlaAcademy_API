package com.kzsrm.service;

import java.util.List;
import java.util.Map;

import com.kzsrm.baseservice.BaseServiceMybatis;
import com.kzsrm.model.Homework;

public interface HomeworkService extends BaseServiceMybatis<Homework, String> {

	/**
	 * 发布作业
	 * 
	 * @return
	 */
	void createHomework(String groupId, String name, String subjectIds);

	/**
	 * 作业列表
	 * 
	 * @return
	 */
	List<Homework> getHomeworkList(String userId, String homeworkId,
			int pageCount, int type);

	/**
	 * 单个用户已答题数量
	 * 
	 * @return
	 */
	Integer getHasDoneSubNum(int homeworkId, String userId);

	/**
	 * 所有群成员已答题数量
	 * 
	 * @return
	 */
	Map<String, Long> getHasDoneSubNumByGroup(String homeworkId, String groupId);

	/**
	 * 所有群成员已答题正确数量
	 * 
	 * @return
	 */
	Integer getRightSubNumByGroup(String homeworkId, String groupId);

}
