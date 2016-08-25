package com.kzsrm.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.kzsrm.model.Sign;
import com.kzsrm.model.User;
import com.kzsrm.model.Yzm;
import com.kzsrm.mybatis.BaseMybatisDao;
import com.kzsrm.utils.MapResult;
import com.kzsrm.utils.Tools;

@Repository("userDao")
public class UserDao<E> extends BaseMybatisDao<User, Integer> {
	private static final String yzmMapper = "com.kzsrm.model.YzmMapper";
	private static final String signMapper = "com.kzsrm.model.SignMapper";
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

	/*
	 * 新增用户打卡记录
	 */
	public int insertSign(Sign signEntity) {
		Map<String, Object> map = new HashMap<String, Object>();
		String currDate = Tools.ymd.format(new Date());
		map.put("startSignDay", currDate);
		map.put("lastSignDay", currDate);
		map.put("antCoin", 1);
		map.put("signNum", signEntity.getSignNum());
		map.put("email", signEntity.getEmail());
		map.put("phone", signEntity.getPhone());
		map.put("uid", signEntity.getUid());
		map.put("signTotalNum", signEntity.getSignTotalNum());
		return this.getSqlSession().insert(signMapper + ".insertSign", map);
	}

	/*
	 * 修改用户打卡记录
	 */
	public int updateSign(Sign signEntity) {
		map.put("signNum", signEntity.getSignNum());
		map.put("lastSignDay", Tools.ymd.format(new Date()));
		map.put("antCoin", signEntity.getAntCoin());
		map.put("email", signEntity.getEmail());
		map.put("phone", signEntity.getPhone());
		map.put("signTotalNum", signEntity.getSignTotalNum());
		if (signEntity.getSignNum() == 0) {
			map.put("signNum", 1);
			map.put("startSignDay", Tools.ymd.format(new Date()));
		}
		return this.getSqlSession().insert(signMapper + ".updateSign", map);
	}

	/*
	 * 获取用户打卡数据
	 */
	public Sign getSign(String phone) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("phone", phone);
		// map.put("checkTimes_Last", Tools.ymd.format(new Date()));
		return this.getSqlSession().selectOne(signMapper + ".getSignNum", map);
	}

	public User getUserByEmailOrMobile(String mobile) {
		map.put("phone", mobile);
		return this.getSqlSession().selectOne(this.getMybatisMapperNamesapce() + ".getByIdOrMobileOrEmail", map);
	}
	
	public User getUserBySessionId(int id){
		map.put("id", id);
		return this.getSqlSession().selectOne(this.getMybatisMapperNamesapce() + ".getUserBySessionId", map);
	}
	
	public int getBatchQuartz(){
		return this.getSqlSession().update(this.getMybatisMapperNamesapce() + ".getBatchQuartz");
	}
	
	public int updateVIPTime(int userId,Date vipTime){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", userId);
		map.put("vipTime", vipTime);
		return this.getSqlSession().update(this.getMybatisMapperNamesapce() + ".updateUserVIPTime",map);
	}
}
