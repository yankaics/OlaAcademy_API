package com.kzsrm.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.Group;
import com.kzsrm.mybatis.BaseMybatisDao;

@Repository
public class GroupDao<E> extends BaseMybatisDao<Group, String> {

	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.GroupMapper";
	}

	public List<Group> getTeacherGroupList(Map<String, Object> map) {
		return this.getSqlSession().selectList(
				this.getMybatisMapperNamesapce() + ".getTeacherGroupList", map);
	}
	
	public List<Group> getUserGroupList(Map<String, Object> map) {
		return this.getSqlSession().selectList(
				this.getMybatisMapperNamesapce() + ".getUserGroupList", map);
	}

}