package com.kzsrm.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.DailyAct;
import com.kzsrm.mybatis.BaseMybatisDao;

@Repository
public class DailyActDao<E> extends BaseMybatisDao<DailyAct, String> {

	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.DailyActMapper";
	}

	public DailyAct getActByPramas(Map<String, Object> map) {
		return this.getSqlSession().selectOne(
				this.getMybatisMapperNamesapce() + ".getByParam", map);
	}
	
	public DailyAct getLastestByUser(int userId) {
		return this.getSqlSession().selectOne(
				this.getMybatisMapperNamesapce() + ".getLastestByUser", userId);
	}
	
	public void signIn(DailyAct dailyAct) {
		this.getSqlSession().selectOne(
				this.getMybatisMapperNamesapce() + ".insertSelective", dailyAct);
	}

}
