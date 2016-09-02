package com.kzsrm.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.Homework;
import com.kzsrm.mybatis.BaseMybatisDao;

@Repository
public class HomeworkDao<E> extends BaseMybatisDao<Homework, String> {

	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.HomeworkMapper";
	}
	
	public List<Homework> getTeacherHomeworkList(Map<String, Object> map) {
		return this.getSqlSession().selectList(
				this.getMybatisMapperNamesapce() + ".getTeacherHomeworkList", map);
	}

	public List<Homework> getStudentHomeworkList(Map<String, Object> map) {
		return this.getSqlSession().selectList(
				this.getMybatisMapperNamesapce() + ".getStudentHomeworkList", map);
	}

}
