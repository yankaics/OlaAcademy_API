package com.kzsrm.service;

import java.util.List;

import net.sf.json.JSONObject;

import com.kzsrm.baseservice.BaseServiceMybatis;
import com.kzsrm.model.Course;
import com.kzsrm.model.Subject;
import com.kzsrm.utils.CustomException;

public interface CourseService  extends BaseServiceMybatis<Course, String> {

	/**
	 * 查询pid下的所有子项
	 * @param pid
	 * @param type
	 * @param orderType 排序类型
	 * @return
	 */
	List<Course> getchildrenCour(String pid, String type,String orderType);
	
	/**
	 * 查询课程
	 * @param pid
	 * @return
	 */
	Course getCourseById(String id);
	
	/**
	 * 获取首页轮播
	 * @return
	 */
	List<Course> getBannerList();
	
	/**
	 * 最新及最热课程
	 * @param pid
	 * @return
	 */
	List<Course> getRecentHotList(int limit);
	
	/**
	 * 更新课程信息
	 * @param videoId
	 * @param timeSpan
	 * @throws CustomException 
	 */
    void updateCourseInfo(String courseId) throws CustomException;
    /**
     * 钻取多级课程
     * @param pid
     * @param userid 
     * @param type 
     * @return
     */
	JSONObject getMultilevelCour(Course course, String userid, String type);
	/**
	 * 刷新知识点下的题目总数
	 * @param pid 
	 * @param type 
	 */
	int refreshSubAllNum(String pid, String type);
	/**
	 * 获取所有二级的课程
	 * @param type
	 * @return
	 */
	List<Course> getSecLevelCour(String type);
	/**
	 * 获取用户已做对题数
	 * @param id
	 * @param userid
	 * @param type 
	 * @return
	 */
	Integer getHasDoneRightSubNum(Integer cid, String userid, String type);
	/**
	 * 获取用户已做错题数
	 * @param id
	 * @param userid
	 * @param type 
	 * @return
	 */
	Integer getHasDoneWrongSubNum(Integer cid, String userid, String type);
	/**
	 * 错题集
	 * @param userid
	 * @param cid
	 * @param type
	 * @return
	 */
	List<Subject> getWrongSubSet(String userid, String cid, String type);

}
