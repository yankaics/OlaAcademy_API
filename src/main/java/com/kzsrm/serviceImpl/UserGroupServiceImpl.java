package com.kzsrm.serviceImpl;

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
}
