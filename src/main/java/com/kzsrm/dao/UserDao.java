package com.kzsrm.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.User;
import com.kzsrm.model.Yzm;
import com.kzsrm.mybatis.BaseMybatisDao;
import com.kzsrm.utils.MapResult;

@Repository("userDao")
public class UserDao<E> extends BaseMybatisDao<User, Integer> {
	private static final String yzmMapper = "com.kzsrm.model.YzmMapper";
	Map<String, Object> map = MapResult.initMap();

	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.UserMapper";
	}

	@SuppressWarnings("hiding")
	public <E> int saveEntity(E entity) {
		System.out.println(entity);
		// map.put("name", name);
		// map.put("phone", phone);
		// map.put("passwd", passwd);\
		int i = this.getSqlSession().insert(this.getMybatisMapperNamesapce() + ".insert", entity);
		return i;
	}

	public void save(String name, int age, String sex, String phone, String passwd, String email, String sign,
			String tag, String status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		map.put("phone", phone);
		map.put("passwd", passwd);
		this.getSqlSession().insert(this.getMybatisMapperNamesapce() + ".insert", map);
	}

	public int insertYZM(User user) {
		return this.getSqlSession().insert(yzmMapper + ".insertYZM", user);
	}

	public User selectIsExitUser(String phone) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("phone", phone);
		User user = this.getSqlSession().selectOne(this.getMybatisMapperNamesapce() + ".selectUser", map);
		return user;
	}

	public Yzm selectOneYzm(String phone) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("phone", phone);
		return this.getSqlSession().selectOne(yzmMapper + ".selectOneYzm", map);
	}

	public User userLogin(User user) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("phone", user.getPhone());
		map.put("passwd", user.getPasswd());
		return this.getSqlSession().selectOne(this.getMybatisMapperNamesapce() + ".userLogin", map);
	}

	public User getUserByEmailOrMobile(String mobile) {
		map.put("phone", mobile);
		return this.getSqlSession().selectOne(this.getMybatisMapperNamesapce() + ".getByIdOrMobileOrEmail", map);
	}
	
	public User getUserBySessionId(int id){
		map.put("id", id);
		return this.getSqlSession().selectOne(this.getMybatisMapperNamesapce() + ".getUserBySessionId", map);
	}
	
	public int updateVIPTime(int userId,Date vipTime){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", userId);
		map.put("vipTime", vipTime);
		return this.getSqlSession().update(this.getMybatisMapperNamesapce() + ".updateUserVIPTime",map);
	}
	
	public List<User> getListByGroup(Map<String, Object> param) {
		return this.getSqlSession().selectList(this.getMybatisMapperNamesapce() + ".getByGroupId", param);
	}
}
