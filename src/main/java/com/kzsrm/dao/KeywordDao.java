package com.kzsrm.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.Keyword;
import com.kzsrm.mybatis.BaseMybatisDao;

@Repository
public class KeywordDao<E> extends BaseMybatisDao<Keyword, String> {

	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.KeywordMapper";
	}

	public List<Keyword> getKeywordList() {
		return this.getSqlSession().selectList(this.getMybatisMapperNamesapce() + ".findAll");
	}

}