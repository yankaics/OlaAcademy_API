package com.kzsrm.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.PointLog;
import com.kzsrm.mybatis.BaseMybatisDao;

@Repository
public class PointLogDao<E> extends BaseMybatisDao<PointLog, String> {

	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.PointLogMapper";
	}

	public List<PointLog> getLearned(Map<String, Object> param) {
		return this.getSqlSession().selectList(this.getMybatisMapperNamesapce() + ".getLearned", param);
	}
}
