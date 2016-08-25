package com.kzsrm.dao;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.UserGroup;
import com.kzsrm.mybatis.BaseMybatisDao;

@Repository
public class UserGroupDao<E> extends BaseMybatisDao<UserGroup, String> {

	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.UserGroupMapper";
	}

}
