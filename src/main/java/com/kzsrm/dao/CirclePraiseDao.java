package com.kzsrm.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.CirclePraise;
import com.kzsrm.mybatis.BaseMybatisDao;

@Repository
public class CirclePraiseDao<E> extends BaseMybatisDao<CirclePraise, String> {

	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.CirclePraiseMapper";
	}

	public List<CirclePraise> getListByParams(Map<String, Object> map) {
		return this.getSqlSession().selectList(
				this.getMybatisMapperNamesapce() + ".getByParams", map);
	}
	
	public List<CirclePraise> getPraiseList(Map<String, Object> map) {
		return this.getSqlSession().selectList(
				this.getMybatisMapperNamesapce() + ".getPraiseList", map);
	}
	
	// 未读点赞数
	public Integer getPraiseCount(Map<String, Object> map) {
		return this.getSqlSession().selectOne(this.getMybatisMapperNamesapce() + ".getPraiseCount", map);
	}
	
	// 更新点赞已读状态
	public void updatePraiseState(Map<String, Object> map) {
		this.getSqlSession().update(
				this.getMybatisMapperNamesapce() + ".updatePraiseState", map);
	}

}
