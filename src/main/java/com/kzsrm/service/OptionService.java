package com.kzsrm.service;

import com.kzsrm.baseservice.BaseServiceMybatis;
import com.kzsrm.model.Option;

public interface OptionService  extends BaseServiceMybatis<Option, String> {
	/**
	 * 获取试题答案
	 * @param subjectId
	 * @return
	 */
	Option getSubjectAnswer(String subjectId);

}
