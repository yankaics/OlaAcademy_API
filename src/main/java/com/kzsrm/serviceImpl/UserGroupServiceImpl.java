package com.kzsrm.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.UserGroupDao;
import com.kzsrm.model.UserGroup;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.UserGroupService;

@Service
@Transactional
public class UserGroupServiceImpl extends
		BaseServiceMybatisImpl<UserGroup, String> implements UserGroupService {
	@Resource
	private UserGroupDao<?> usergroupDao;

	@Override
	protected EntityDao<UserGroup, String> getEntityDao() {
		return usergroupDao;
	}

	/**
	 * 添加用户和群的关联
	 * 
	 * @param userId
	 * @param groupId
	 * @param createTime
	 */
	@Override
	public void insertData(UserGroup usergroup) {
		usergroupDao.save(usergroup);
	}
	
	/**
	 * 是否已加入群
	 * 
	 * @return
	 */
	@Override
	public List<UserGroup> getByParams(String userId,String groupId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("groupId", groupId);
		return usergroupDao.getNumByUserAndGroup(map);
	}
	
	/**
	 * 退出群
	 * 
	 * @return
	 */
	@Override
	public void delete(String groupId) {
		usergroupDao.deleteById(groupId);
	}
}
