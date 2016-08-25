package com.kzsrm.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.Teacher;
import com.kzsrm.mybatis.BaseMybatisDao;

@Repository
public class TeacherDao<E> extends BaseMybatisDao<Teacher, String> {

	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.TeacherMapper";
	}

	public Teacher getTeacherById(Map<String, Object> param) {
		return this.getSqlSession().selectOne(this.getMybatisMapperNamesapce() + ".getById",param);
	}

}
