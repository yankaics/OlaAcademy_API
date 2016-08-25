package com.kzsrm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.OlaCircle;
import com.kzsrm.mybatis.BaseMybatisDao;

@Repository
public class OlaCircleDao<E> extends BaseMybatisDao<OlaCircle, String> {

	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.OlaCircleMapper";
	}

	public List<OlaCircle> getVideoList(Map<String, Object> map) {
		return this.getSqlSession().selectList(this.getMybatisMapperNamesapce() + ".getCircleList", map);
	}
	
	public OlaCircle getByUserIdAndCourseId(String userId, String courseId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userid", userId);
		map.put("courseId", courseId);
		return this.getSqlSession().selectOne(this.getMybatisMapperNamesapce() + ".selectByUserIdAndCourseId", map);
	}
	
}
