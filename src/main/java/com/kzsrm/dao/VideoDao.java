package com.kzsrm.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.Video;
import com.kzsrm.mybatis.BaseMybatisDao;

@Repository
public class VideoDao<E> extends BaseMybatisDao<Video, String> {

	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.VideoMapper";
	}

	public Video getById(Integer id) {
		return this.getSqlSession().selectOne(this.getMybatisMapperNamesapce() + ".getById", id);
	}
	
	public Video getVideoByPoint(String pointId) {
		return this.getSqlSession().selectOne(this.getMybatisMapperNamesapce() + ".getVideoByPoint", pointId);
	}
	public List<Video> getVideoListByPoint(String pointId) {
		return this.getSqlSession().selectList(this.getMybatisMapperNamesapce() + ".getVideoListByPoint", pointId);
	}
	
	public List<Video> getVideoByTag(String keyword) {
		String searchText = new StringBuilder("%").append(keyword).append("%").toString(); 
		return this.getSqlSession().selectList(this.getMybatisMapperNamesapce() + ".getVideoByTag", searchText);
	}

	public List<Video> getVideoBySubject(String subjectId) {
		return this.getSqlSession().selectList(this.getMybatisMapperNamesapce() + ".getVideoBySubject", subjectId);
	}
	
	public List<Video> getVideosByGoods(String gid) {
		return this.getSqlSession().selectList(this.getMybatisMapperNamesapce() + ".getVideosByGoods", gid);
	}

}
