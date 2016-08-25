package com.kzsrm.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.Broadcast;
import com.kzsrm.mybatis.BaseMybatisDao;

@Repository
public class BroadcastDao<E> extends BaseMybatisDao<Broadcast, String> {

	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.BroadcastMapper";
	}

	public List<Broadcast> getBroadcastList(Map<String, Object> map) {
		return this.getSqlSession().selectList(
				this.getMybatisMapperNamesapce() + ".getBroadcastList", map);
	}
	
}