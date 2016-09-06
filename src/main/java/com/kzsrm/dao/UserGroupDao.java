package com.kzsrm.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.UserGroup;
import com.kzsrm.mybatis.BaseMybatisDao;

@Repository
public class UserGroupDao<E> extends BaseMybatisDao<UserGroup, String> {

	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.UserGroupMapper";
	}
	
	public List<UserGroup> getNumByUserAndGroup(Map<String, Object> map) {
		return this.getSqlSession().selectList(this.getMybatisMapperNamesapce() + ".getByParam", map);
	}

}
