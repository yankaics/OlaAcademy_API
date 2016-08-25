package com.kzsrm.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.Goods;
import com.kzsrm.mybatis.BaseMybatisDao;

@Repository
public class GoodsDao<E> extends BaseMybatisDao<Goods, String> {
	
	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.GoodsMapper";
	}

	public List<Goods> getList(Map<String, Object> param) {
		return this.getSqlSession().selectList(this.getMybatisMapperNamesapce() + ".getList", param);
	}

}