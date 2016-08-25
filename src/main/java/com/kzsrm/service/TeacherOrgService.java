package com.kzsrm.service;

import java.util.List;

import com.kzsrm.model.TeacherOrg;

public interface TeacherOrgService {

	/**
	 * 根据机构Id获取教师Id
	 * 
	 * @param orgId
	 *            机构id
	 * @return
	 */
	List<TeacherOrg> getListByOrgId(String orgId);

}
