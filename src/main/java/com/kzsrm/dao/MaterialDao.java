package com.kzsrm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.Material;
import com.kzsrm.mybatis.BaseMybatisDao;

@Repository
public class MaterialDao<E> extends BaseMybatisDao<Material, String> {

	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.MaterialMapper";
	}

	public List<Material> getMaterialList(Map<String, Object> param) {
		return this.getSqlSession().selectList(
				this.getMybatisMapperNamesapce() + ".getMaterialList", param);
	}
	
	
	public void updateBrowseCount(String materialId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", materialId);
		this.getSqlSession().selectOne(
				this.getMybatisMapperNamesapce() + ".updateBrowseCount", map);
	}

}
