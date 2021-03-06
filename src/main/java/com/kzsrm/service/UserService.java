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
	
	/**
	 * 根据第三方登陆 Id 查询用户
	 * @param source
	 * @param sourceId
	 * @return
	 */
	public User getUserByThirdId(String source,String sourceId);
	
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
	
	/**
	 * 老师列表
	 * 
	 * @return
	 */
	List<User> getTeacherList();
	
	/**
	 * 用户综合排名（打败多少用户）
	 * 
	 * @return
	 */
	int getUesrDefeatPercent(String userId);
	
}
