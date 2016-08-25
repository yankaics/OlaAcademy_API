package com.kzsrm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.VideoLog;
import com.kzsrm.mybatis.BaseMybatisDao;

@Repository
public class VideoLogDao<E> extends BaseMybatisDao<VideoLog, String> {

	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.VideoLogMapper";
	}

	public VideoLog getVideoLog(Map<String, Object> map) {
		return this.getSqlSession().selectOne(this.getMybatisMapperNamesapce() + ".getVideoLog", map);
	}
	
	public List<VideoLog> getVideoList(Map<String, Object> map) {
		return this.getSqlSession().selectList(this.getMybatisMapperNamesapce() + ".getVideoList", map);
	}
	
	public int addVideoLog(VideoLog videoLog) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("vid", videoLog.getVid());
		map.put("courseId", videoLog.getCourseId());
		map.put("userid", videoLog.getUserid());
		map.put("createTime", videoLog.getCreatetime());
		return this.getSqlSession().insert(this.getMybatisMapperNamesapce() + ".insert", map);
	}

	public VideoLog getByUserIdAndCourseId(String userId, String courseId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userid", userId);
		map.put("courseId", courseId);
		return this.getSqlSession().selectOne(this.getMybatisMapperNamesapce() + ".selectByUserIdAndCourseId", map);
	}
	
	public int updateVideoLog(VideoLog videoLog) {
		return this.getSqlSession().update(
				this.getMybatisMapperNamesapce() + ".updateByPrimaryKeySelective", videoLog);
	}
}
