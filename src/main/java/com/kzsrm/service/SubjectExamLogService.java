package com.kzsrm.service;

import com.kzsrm.baseservice.BaseServiceMybatis;
import com.kzsrm.model.Option;
import com.kzsrm.model.Subject;
import com.kzsrm.model.SubjectExamLog;

public interface SubjectExamLogService  extends BaseServiceMybatis<SubjectExamLog, String> {
	/**
	 * 更新或保存答题记录
	 * @param opt
	 * @param sub
	 * @param userId
	 */
	void saveOrUpdate(Option opt, Subject sub, String userId);
	
	/**
	 * 更新错题本（手动添加删除）
	 * @param subjectId
	 * @param type 1 添加错题 2 移除错题
	 * @return
	 */
	void updateWrongSet(String userId,String subjectId, int type);

}
