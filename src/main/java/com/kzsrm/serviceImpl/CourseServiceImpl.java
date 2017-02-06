package com.kzsrm.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.CourseDao;
import com.kzsrm.dao.OptionDao;
import com.kzsrm.dao.SubjectDao;
import com.kzsrm.dao.SubjectLogDao;
import com.kzsrm.dao.TeacherDao;
import com.kzsrm.model.Course;
import com.kzsrm.model.Subject;
import com.kzsrm.model.Teacher;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.CourseService;
import com.kzsrm.utils.ComUtils;
import com.kzsrm.utils.CustomException;

@Service
@Transactional
public class CourseServiceImpl extends BaseServiceMybatisImpl<Course, String> implements CourseService {
	@Resource private CourseDao<?> courseDao;
	@Resource private SubjectDao<?> subjectDao;
	@Resource private SubjectLogDao<?> subjectLogDao;
	@Resource private OptionDao<?> optionDao;
	@Resource private TeacherDao<?> teacherDao;

	@Override
	protected EntityDao<Course, String> getEntityDao() {
		return courseDao;
	}
	
	JsonConfig courCf = ComUtils.jsonConfig(new String[]{"playcount"});

	/**
	 * 查询pid下的所有子项
	 * @param pid
	 * @param type
	 * @param orderType 排序类型
	 * @return
	 */
	@Override
	public List<Course> getchildrenCour(String pid, String type,String orderType) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pid", pid);
		map.put("type", type);
		map.put("orderType", orderType);
		return courseDao.getchildrenCour(map);
	}
	
	/**
	 * 查询课程
	 * @param pid
	 * @return
	 */
	@Override
	public Course getCourseById(String id){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		return courseDao.getCourseById(map);
	}
	
	/**
	 * 最新及最热课程
	 * @param pid
	 * @return
	 */
	@Override
	public List<Course> getRecentHotList(int limit){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("limit", limit);
		map.put("type", 2);
		List<Course> recentList = courseDao.getRecentCourse(map);
		List<Course> hotList = courseDao.getHotCourse(map);
		List<Course> courseList = new ArrayList<Course>();
		courseList.addAll(hotList);
		courseList.addAll(recentList);
		return courseList;
	}
	
	/**
	 * 获取轮播内容
	 * @return
	 */
	@Override
	public List<Course> getBannerList(){
		return courseDao.getBannerCourse();
	}
	
	/**
	 * 更新课程信息
	 * @param videoId
	 * @throws CustomException 
	 */
	@Override
	public void updateCourseInfo(String courseId) throws CustomException {

		Course course = courseDao.getById(courseId);
		if (course == null)
			throw new CustomException("课程不存在");
		
		course.setPlaycount(course.getPlaycount() + 1);
		courseDao.update(course);
	}

	/**
     * 获取多级课程
     * @param pid
     * @return
     */
	@Override
	public JSONObject getMultilevelCour(Course course, String userid, String type) {
		if (course != null){
			JSONObject _course = JSONObject.fromObject(course, courCf);
			Integer subNum = 0;// 已做题数
			List<Course> courseList = getchildrenCour(course.getId()+"", type, "orderIndex");
			if (courseList != null && courseList.size() > 0) {
				JSONArray children = new JSONArray();
				for (Course child : courseList){
					JSONObject ch = getMultilevelCour(child, userid, type);
					if(!TextUtils.isEmpty(ch.getString("teacherId"))){
						Teacher teacher = teacherDao.getById(ch.getString("teacherId"));
						ch.put("teacherName", teacher.getName());
						ch.put("teacherAvator", teacher.getAvatar());
					}
					subNum += ch.getInt("subNum");
					children.add(ch);
				}
				_course.put("child", children);
			} else if (StringUtils.isNotBlank(userid)){
				subNum = getHasDoneSubNum(course.getId(), userid);
			}
			_course.put("subNum", subNum);
			_course.put("playcount", course.getPlaycount());
			return _course;
		}
		return new JSONObject();
	}

	private Integer getHasDoneSubNum(Integer cid, String userid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cid", cid);
		map.put("userid", userid);
		return subjectLogDao.getHasDoneSubNum(map);
	}
	/**
	 * 获取用户已做对题数
	 * @param id
	 * @param userid
	 * @param type 
	 * @return
	 */
	@Override
	public Integer getHasDoneRightSubNum(Integer cid, String userid, String type) {
		List<Course> courseList = getchildrenCour(cid + "", type,"orderIndex");
		if (courseList != null && courseList.size() > 0) {
			int i = 0;
			for (Course course : courseList)
				i += getHasDoneRightSubNum(course.getId(), userid, type);
			return i;
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("cid", cid);
			map.put("userid", userid);
			return subjectLogDao.getHasRightDoneSubNum(map);
		}
	}
	
	/**
	 * 获取用户已做错题数
	 * @param id
	 * @param userid
	 * @param type 
	 * @return
	 */
	@Override
	public Integer getHasDoneWrongSubNum(Integer cid, String userid, String type) {
		List<Course> courseList = getchildrenCour(cid + "", type,"orderIndex");
		if (courseList != null && courseList.size() > 0) {
			int i = 0;
			for (Course course : courseList)
				i += getHasDoneRightSubNum(course.getId(), userid, type);
			return i;
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("cid", cid);
			map.put("userid", userid);
			return subjectLogDao.getHasDoneWrongSubNum(map);
		}
	}
	
	/**
	 * 刷新知识点下的题目总数
	 * @param pid 
	 * @param type 
	 */
	@Override
	public int refreshSubAllNum(String pid, String type) {
		int num = 0;
		List<Course> courseList = getchildrenCour(pid, null,"orderIndex");// 暂不考虑type
		if (courseList != null && courseList.size() > 0) {
			for (Course course : courseList) {
				int tmp = refreshSubAllNum(course.getId()+"", type);
				course.setSubAllNum(tmp);
				update(course);
				num += tmp;
			}
		} else {
			num = subjectDao.getSubNumByPoint(pid);
		}
		return num;
	}

	/**
	 * 获取所有二级的课程
	 * @param type
	 * @return
	 */
	@Override
	public List<Course> getSecLevelCour(String type) {
		return courseDao.getSecLevelCour(type);
	}

	/**
	 * 错题集
	 * @param userid
	 * @param cid
	 * @param type
	 * @return
	 */
	@Override
	public List<Subject> getWrongSubSet(String userid, String cid, String type) {
		List<Course> courseList = getchildrenCour(cid, type,"orderIndex");
		if (courseList != null && courseList.size() > 0) {
			List<Subject> ret = new ArrayList<Subject>();
			for (Course course : courseList) {
				ret.addAll(getWrongSubSet(userid, course.getId()+"", type));
			}
			return ret;
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userid", userid);
			map.put("cid", cid);
			List<Subject> subList = subjectDao.getWrongSubSet(map);
			for (Subject sub : subList)
				sub.setOptionList(optionDao.getOptionBySubject(sub.getId()));
			return subList;
		}
	}

}
