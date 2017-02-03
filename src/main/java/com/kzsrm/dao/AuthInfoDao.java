package com.kzsrm.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.AuthInfo;
import com.kzsrm.mybatis.BaseMybatisDao;

@Repository
public class AuthInfoDao<E> extends BaseMybatisDao<AuthInfo, String> {

	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.AuthInfoMapper";
	}

	public AuthInfo getByUserId(Map<String, Object> map) {
		return this.getSqlSession().selectOne(
				this.getMybatisMapperNamesapce() + ".getByUserId", map);
	}

}
