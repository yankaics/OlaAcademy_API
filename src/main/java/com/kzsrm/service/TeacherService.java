package com.kzsrm.service;

import com.kzsrm.model.Teacher;

public interface TeacherService {
	
	/**
	 * 根据Id获取教师信息
	 * 
	 * @param integer
	 *            id
	 * @return
	 */
	Teacher getTeacherById(Integer integer);

}
