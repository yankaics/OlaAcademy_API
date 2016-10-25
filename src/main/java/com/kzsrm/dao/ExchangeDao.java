package com.kzsrm.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.Exchange;
import com.kzsrm.mybatis.BaseMybatisDao;

@Repository
public class ExchangeDao<E> extends BaseMybatisDao<Exchange, String> {

	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.ExchangeMapper";
	}

	public List<Exchange> getByPrarms(Map<String, Object> map) {
		return this.getSqlSession().selectList(
				this.getMybatisMapperNamesapce() + ".getByParam", map);
	}

}
