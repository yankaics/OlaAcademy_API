package com.kzsrm.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.AttentionShip;
import com.kzsrm.mybatis.BaseMybatisDao;

@Repository
public class AttentionShipDao<E> extends BaseMybatisDao<AttentionShip, String> {

	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.AttentionShipMapper";
	}

	public List<AttentionShip> getListByPrama(Map<String, Object> map) {
		return this.getSqlSession().selectList(
				this.getMybatisMapperNamesapce() + ".getByParam", map);
	}

}
