package com.kzsrm.service;

import java.util.List;

import com.kzsrm.baseservice.BaseServiceMybatis;
import com.kzsrm.model.Material;

public interface MaterialService extends BaseServiceMybatis<Material, String> {

	/**
	 * 列表
	 * 
	 * @return
	 */
	List<Material> getMaterialList(String materialId, String type, int pageSize);
	
	/**
	 * 浏览量＋1
	 */
	void updateBrowseCount(String materialId);

}
