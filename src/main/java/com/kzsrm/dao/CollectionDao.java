package com.kzsrm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.Collection;
import com.kzsrm.mybatis.BaseMybatisDao;

@Repository
public class CollectionDao<E> extends BaseMybatisDao<Collection, String> {

	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.CollectionMapper";
	}
	
	public int insert(Collection collection) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", collection.getUserId());
		map.put("videoId", collection.getVideoId());
		map.put("courseId", collection.getCourseId());
		map.put("type", collection.getType());
		return this.getSqlSession().insert(this.getMybatisMapperNamesapce() + ".insert", map);
	}

	public List<Collection> getByUserId(Map<String, Object> param) {
		return this.getSqlSession().selectList(
				this.getMybatisMapperNamesapce() + ".getByUserId", param);
	}
	
	public List<Collection> getByVideoId(Map<String, Object> param) {
		return this.getSqlSession().selectList(
				this.getMybatisMapperNamesapce() + ".getByVideoId", param);
	}
	
	public Collection getByUserIdAndVideoId(Map<String, Object> param) {
		return this.getSqlSession().selectOne(
				this.getMybatisMapperNamesapce() + ".getByUserIdAndVideoId", param);
	}
	
	public int deleteByUserIdAndVideoId(Map<String, Object> param) {
		return this.getSqlSession().delete(
				this.getMybatisMapperNamesapce() + ".deleteByUserIdAndVideoId", param);
	}
	
	public int deleteByUserId(Map<String, Object> param) {
		return this.getSqlSession().delete(
				this.getMybatisMapperNamesapce() + ".deleteByUserId", param);
	}
	
	

}

