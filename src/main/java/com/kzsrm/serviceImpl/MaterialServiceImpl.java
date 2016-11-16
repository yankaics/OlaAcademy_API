package com.kzsrm.serviceImpl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.MaterialDao;
import com.kzsrm.model.Material;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.MaterialService;

@Service
@Transactional
public class MaterialServiceImpl extends
		BaseServiceMybatisImpl<Material, String> implements MaterialService {
	@Resource
	private MaterialDao<?> materialDao;

	@Override
	protected EntityDao<Material, String> getEntityDao() {
		return materialDao;
	}

	@Override
	public List<Material> getMaterialList(String materialId, String type, int pageSize) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(materialId)) {
			Material material = materialDao.getById(materialId);
			if (material != null) {
				map.put("createTime", material.getCreateTime());
			} else {
				map.put("createTime", new Date());
			}
		} else {
			map.put("createTime", new Date());
		}

		map.put("pageSize", pageSize);
		map.put("type", type);
		return materialDao.getMaterialList(map);
	}
	
	@Override
	public void updateBrowseCount(String materialId) {
		materialDao.updateBrowseCount(materialId);
	}

}
