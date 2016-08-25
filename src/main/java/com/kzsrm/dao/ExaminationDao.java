package com.kzsrm.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.Examination;
import com.kzsrm.mybatis.BaseMybatisDao;

@Repository
public class ExaminationDao<E> extends BaseMybatisDao<Examination, String> {
	
	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.ExaminationMapper";
	}

	public List<Examination> getListByCour(Map<String, Object> map) {
		return this.getSqlSession().selectList(this.getMybatisMapperNamesapce() + ".getListByCour", map);
	}

}