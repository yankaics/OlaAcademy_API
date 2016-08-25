package com.kzsrm.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.Course;
import com.kzsrm.mybatis.BaseMybatisDao;

@Repository
public class CourseDao<E> extends BaseMybatisDao<Course, String> {
	
	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.CourseMapper";
	}

	public List<Course> getchildrenCour(Map<String, Object> param) {
		return this.getSqlSession().selectList(this.getMybatisMapperNamesapce() + ".getchildrenCour", param);
	}
	
	public Course getCourseById(Map<String, Object> param) {
		return this.getSqlSession().selectOne(this.getMybatisMapperNamesapce() + ".getById", param);
	}
	
	public List<Course> getSecLevelCour(String type) {
		return this.getSqlSession().selectList(this.getMybatisMapperNamesapce() + ".getSecLevelCour", type);
	}
	
	public List<Course> getBannerCourse() {
		return this.getSqlSession().selectList(this.getMybatisMapperNamesapce() + ".getBannerCourse");
	}
	
	public List<Course> getRecentCourse(Map<String, Object> param) {
		return this.getSqlSession().selectList(this.getMybatisMapperNamesapce() + ".getRecentCourse",param);
	}
	
	public List<Course> getHotCourse(Map<String, Object> param) {
		return this.getSqlSession().selectList(this.getMybatisMapperNamesapce() + ".getHotCourse",param);
	}

}
