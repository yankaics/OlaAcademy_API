package com.kzsrm.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.CoinHistory;
import com.kzsrm.mybatis.BaseMybatisDao;

@Repository
public class CoinHistoryDao<E> extends BaseMybatisDao<CoinHistory, String> {

	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.CoinHistoryMapper";
	}

	public List<CoinHistory> getActByPramas(Map<String, Object> map) {
		return this.getSqlSession().selectList(
				this.getMybatisMapperNamesapce() + ".getByParam", map);
	}
	
	public CoinHistory getLastestByUser(int userId) {
		return this.getSqlSession().selectOne(
				this.getMybatisMapperNamesapce() + ".getLastestByUser", userId);
	}
	
	public void insertData(CoinHistory dailyAct) {
		this.getSqlSession().selectOne(
				this.getMybatisMapperNamesapce() + ".insertSelective", dailyAct);
	}

}
