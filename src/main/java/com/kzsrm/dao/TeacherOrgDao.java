package com.kzsrm.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.TeacherOrg;
import com.kzsrm.mybatis.BaseMybatisDao;

@Repository
public class TeacherOrgDao<E> extends BaseMybatisDao<TeacherOrg, String> {

	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.TeacherOrgMapper";
	}

	public List<TeacherOrg> getTeacherOrgByOid(Map<String, Object> param) {
		return this.getSqlSession().selectList(
				this.getMybatisMapperNamesapce() + ".getByOrgId", param);
	}

}
