package com.kzsrm.service;

import java.util.Date;
import java.util.Map;

import com.kzsrm.model.Sign;
import com.kzsrm.model.User;
import com.kzsrm.model.Yzm;

public interface UserService {

//	public Map<String, Object> createUser(String name, Integer age, String sex, String phone, String passwd,
//			String email, String sign, String tag, String status, String appv, String src);

	public User selectUser(int id);

	public Map<String, Object> insertUser(User user);

	public Map<String, Object> updateUser(User user);

	public Map<String, Object> selectUniqueUser(String phone);

	public int insertYZM(User user);

	public Yzm getYzm(String phone);

	public Map<String, Object> login(User user);

	public boolean insertSign(Sign sign);

	public Sign getSign(String phone);
	
	public int updateSign(Sign sign);
	
	public User selByEmailOrMobile(String mobile);
	
	public int batchQuartz();
	
	/*
	 * 更新用户Vip有效期
	 */
	int updateVipTime(int userId,Date vipTime);
	
}
