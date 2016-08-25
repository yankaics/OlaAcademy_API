package com.kzsrm.service;

import java.util.List;

import com.kzsrm.baseservice.BaseServiceMybatis;
import com.kzsrm.model.Subject;

import net.sf.json.JSONArray;

public interface SubjectService  extends BaseServiceMybatis<Subject, String> {

	/**
	 * 获取视频对应的测试题
	 * @param videoId
	 * @return
	 */
	List<Subject> getSubjectByVideo(String videoId);
	/**
	 * 校验测试题的答案
	 * @param userId
	 * @param answer
	 * @param type 
	 * @return
	 */
	JSONArray checkAnswer(String userId, String answer, String type);
	/**
	 * 获取知识点对应的测试题
	 * @param videoId
	 * @return
	 */
	List<Subject> getSubjectByPoint(String pointId);
	/**
	 * 获取知识点对应的测试题的数量
	 * @param videoId
	 * @return
	 */
	Integer getSubNumByPoint(String pointId);
	/**
	 * 获取模考对应的测试题
	 * @param examId
	 * @return
	 */
	List<Subject> getSubjectByExam(String examId);
	/**
	 * 获取作业对应的测试题
	 * @param homeworkId
	 * @return
	 */
	List<Subject> getSubjectByHomework(String homeworkId);
	/**
	 * 获取知识点对应的测试题的数量
	 * @param videoId
	 * @return
	 */
	Integer getSubNumByHomework(String homeworkId);
}
