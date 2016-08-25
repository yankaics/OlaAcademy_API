package com.kzsrm.service;

import java.util.List;

import com.kzsrm.baseservice.BaseServiceMybatis;
import com.kzsrm.model.Examination;

public interface ExamService  extends BaseServiceMybatis<Examination, String> {
	/**
	 * 获取模考列表
	 * @param cid	课程id
	 * @param type 	1-题库，2-真题
	 * @return
	 */
	List<Examination> getListByCour(String cid, String type);
	
	/**
	 * 获取题库已做题目进度
	 * @param cid	题库id
	 * @param userid
	 * @return
	 */
	public Integer getHasDoneProgress(int eid, String userid);

}
