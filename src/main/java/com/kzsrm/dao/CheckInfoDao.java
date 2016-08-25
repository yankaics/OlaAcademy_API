package com.kzsrm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.CheckInfo;
import com.kzsrm.mybatis.BaseMybatisDao;

@Repository
public class CheckInfoDao<E> extends BaseMybatisDao<CheckInfo, String> {

	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.CheckInMapper";
	}

	public int checkInToOrganization(CheckInfo checkInfo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orgId", checkInfo.getOrgId());
		map.put("userLocal", checkInfo.getUserLocal());
		map.put("userPhone", checkInfo.getUserPhone());
		map.put("type", checkInfo.getType());
		map.put("checkinTime", checkInfo.getCheckinTime());
		return this.getSqlSession().insert(this.getMybatisMapperNamesapce() + ".insert", map);
	}
	
	public CheckInfo getInfoByUserPhoneAndOrg(String userPhone,Integer orgId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userPhone", userPhone);
		map.put("orgId", orgId);
		return this.getSqlSession().selectOne(this.getMybatisMapperNamesapce() + ".getByPhoneAndOrg", map);
	}
	
	public List<CheckInfo> getByUserPhone(String userPhone) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userPhone", userPhone);
		return this.getSqlSession().selectList(this.getMybatisMapperNamesapce() + ".getByPhone", map);
	}
	
	public int deleteById(Map<String, Object> param) {
		return this.getSqlSession().delete(
				this.getMybatisMapperNamesapce() + ".deleteById", param);
	}

}