package com.kzsrm.serviceImpl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.UserDao;
import com.kzsrm.dao.UserGroupDao;
import com.kzsrm.model.User;
import com.kzsrm.model.UserGroup;
import com.kzsrm.model.Yzm;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.UserService;
import com.kzsrm.utils.MapResult;

@Service("userService")
@Transactional
public class UserServiceImpl extends BaseServiceMybatisImpl<User, Integer>implements UserService {
	// public class UserServiceImpl implements UserService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Resource
	private UserDao<?> userDao;
	@Resource
	private UserGroupDao<?> usergroupDao;
	
	Map<String, Object> map = MapResult.initMap();

	@Override
	protected EntityDao<User, Integer> getEntityDao() {
		return userDao;
	}

	@Override
	public User selectUser(int id) {
		User user = this.userDao.getUserBySessionId(id);
		return user;
	}

	@Override
	public Map<String, Object> insertUser(User user) {
		int i = this.userDao.saveEntity(user);
		if(i==1){  //添加默认群
			UserGroup userGroup = new UserGroup();
			userGroup.setUserId(user.getId());
			userGroup.setGroupId(1);
			userGroup.setCreateTime(new Date());
			usergroupDao.save(userGroup);
		}
		map.put("data", i);
		return map;
	}

	@Override
	public Map<String, Object> updateUser(User user) {
		int result = this.userDao.update(user);
		boolean flag = false;
		if (result == 1) {
			flag = true;
		}
		map.put("data", flag);
		return map;
	}

	@Override
	public Map<String, Object> selectUniqueUser(String phone) {
		User user = this.userDao.selectIsExitUser(phone);
		map.put("data", user);
		return map;
	}

	@Override
	public int insertYZM(User user) {
		return this.userDao.insertYZM(user);
	}

	@Override
	public Yzm getYzm(String phone) {
		return this.userDao.selectOneYzm(phone);
	}

	@Override
	public Map<String, Object> login(User user) {
		User u = this.userDao.userLogin(user);
		map.put("data", u);
		if (u != null) {
			//如果首次登录，赠送5天会员
			if(u.getVipTime()==null){
//				Calendar c = Calendar.getInstance();
//				c.add(Calendar.DATE, 5);
//				userDao.updateVIPTime(u.getId(), c.getTime());
				userDao.updateVIPTime(u.getId(), new Date());
			}
		}
		return map;
	}

	@Override
	public User selByEmailOrMobile(String mobile) {
		return this.userDao.getUserByEmailOrMobile(mobile);
	}

	@Override
	public int batchQuartz() {
		return 1;  // 暂未执行定时任务
	}
	
	/*
	 * 更新用户Vip有效期
	 */
	@Override
	public int updateVipTime(int userId, Date vipTime) {
		return  this.userDao.updateVIPTime(userId, vipTime);
	}
	
	/**
	 * 群成员列表
	 * 
	 * @return
	 */
	@Override
	public List<User> getGroupMember(String groupId,int pageIndex, int pageSize) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("groupId", groupId);
		if(pageIndex!=0){
			params.put("pageIndex", (pageIndex - 1) * pageSize);
			params.put("pageSize", pageSize);
		}
		return userDao.getListByGroup(params);
	}
	
	/**
	 * 老师列表
	 * 
	 * @return
	 */
	@Override
	public List<User> getTeacherList(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "2");
		return userDao.getUserList(params);
	}
}
