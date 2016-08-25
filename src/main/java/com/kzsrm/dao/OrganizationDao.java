package com.kzsrm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.Organization;
import com.kzsrm.mybatis.BaseMybatisDao;

@Repository
public class OrganizationDao <E> extends BaseMybatisDao<Organization, String> {
	
	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.OrganizationMapper";
	}
	
	public Organization getOrganizationById(Integer orgId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", orgId);
		return this.getSqlSession().selectOne(this.getMybatisMapperNamesapce() + ".getById",map);
	}

	public List<Organization> getAllOrganization() {
		return this.getSqlSession().selectList(this.getMybatisMapperNamesapce() + ".findAll");
	}
	
	public List<Organization> getOrgList(Map<String, Object> map) {
		return this.getSqlSession().selectList(this.getMybatisMapperNamesapce() + ".getOrgList",map);
	}
}
