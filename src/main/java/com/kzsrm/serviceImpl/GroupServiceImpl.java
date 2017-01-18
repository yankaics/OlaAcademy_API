package com.kzsrm.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.GroupDao;
import com.kzsrm.model.Group;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.GroupService;

@Service
@Transactional
public class GroupServiceImpl extends BaseServiceMybatisImpl<Group, String>
		implements GroupService {
	@Resource
	private GroupDao<?> groupDao;

	@Override
	protected EntityDao<Group, String> getEntityDao() {
		return groupDao;
	}
	
	/**
	 * 创建群
	 */
	@Override
	public void createGroup(Group group){
		groupDao.save(group);
	}
	
	/**
	 * 用户(教师)所创建群列表
	 * 
	 * @return
	 */
	@Override
	public List<Group> getTeacherGroupList(String userId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		return groupDao.getTeacherGroupList(map);
	}

	/**
	 * 学生群列表
	 * 
	 * @return
	 */
	@Override
	public List<Group> getUserGroupList(String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(!type.equals("0")){
			map.put("type", type);
		}
		return groupDao.getUserGroupList(map);
	}

}
