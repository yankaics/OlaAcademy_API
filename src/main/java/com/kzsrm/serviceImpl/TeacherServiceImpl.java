package com.kzsrm.serviceImpl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.TeacherDao;
import com.kzsrm.model.Teacher;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.TeacherService;

@Service
@Transactional
public class TeacherServiceImpl extends
		BaseServiceMybatisImpl<Teacher, String> implements TeacherService {
	@Resource
	private TeacherDao<?> teacherDao;

	@Override
	protected EntityDao<Teacher, String> getEntityDao() {
		return teacherDao;
	}

	/**
	 * 根据Id获取教师信息
	 * 
	 * @param id
	 *            id
	 * @return
	 */
	@Override
	public Teacher getTeacherById(Integer id){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		Teacher teacher = teacherDao.getTeacherById(map);
		return teacher;
	}

}

