package com.kzsrm.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.HomeworkSubject;
import com.kzsrm.mybatis.BaseMybatisDao;

@Repository
public class HomeworkSubjectDao<E> extends BaseMybatisDao<HomeworkSubject, String> {

	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.HomeworkSubjectMapper";
	}
	
	public List<HomeworkSubject> getNumByUserAndGroup(Map<String, Object> map) {
		return this.getSqlSession().selectList(this.getMybatisMapperNamesapce() + ".getByParam", map);
	}

}
