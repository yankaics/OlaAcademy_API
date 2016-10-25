package com.kzsrm.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kzsrm.model.User;
import com.kzsrm.model.Yzm;

public interface UserService {

	public User selectUser(int id);

	public Map<String, Object> insertUser(User user);

	public Map<String, Object> updateUser(User user);

	public Map<String, Object> selectUniqueUser(String phone);

	public int insertYZM(User user);

	public Yzm getYzm(String phone);

	public Map<String, Object> login(User user);

	public User selByEmailOrMobile(String mobile);
	
	public int batchQuartz();
	
	/*
	 * 更新用户Vip有效期
	 */
	int updateVipTime(int userId,Date vipTime);
	
	/**
	 * 群成员列表
	 * 
	 * @return
	 */
	List<User> getGroupMember(String groupId,int pageIndex, int pageSize);
	
}
