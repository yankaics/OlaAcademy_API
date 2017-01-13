package com.kzsrm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.WatchRecord;
import com.kzsrm.mybatis.BaseMybatisDao;

@Repository
public class WatchRecordDao<E> extends BaseMybatisDao<WatchRecord, String> {

	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.WatchRecordMapper";
	}

	public List<WatchRecord> getListByParams(String userId, String objectId, String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("objectId", objectId);
		map.put("type", type);
		return this.getSqlSession().selectList(this.getMybatisMapperNamesapce() + ".getListByParams", map);
	}

}
