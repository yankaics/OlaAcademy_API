package com.kzsrm.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.TeacherOrgDao;
import com.kzsrm.model.TeacherOrg;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.TeacherOrgService;

@Service
@Transactional
public class TeacherOrgServiceImpl extends
		BaseServiceMybatisImpl<TeacherOrg, String> implements TeacherOrgService {
	@Resource
	private TeacherOrgDao<?> teacherOrgDao;

	@Override
	protected EntityDao<TeacherOrg, String> getEntityDao() {
		return teacherOrgDao;
	}

	/**
	 * 根据机构Id获取教师Id
	 * 
	 * @param orgId
	 *            机构id
	 * @return
	 */
	@Override
	public List<TeacherOrg> getListByOrgId(String oid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("oid", oid);
		List<TeacherOrg> teacherOrgList = teacherOrgDao.getTeacherOrgByOid(map);
		return teacherOrgList;
	}

}
