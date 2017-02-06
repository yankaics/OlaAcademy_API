package com.kzsrm.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kzsrm.model.Collection;
import com.kzsrm.model.Course;
import com.kzsrm.model.Examination;
import com.kzsrm.model.Homework;
import com.kzsrm.model.Option;
import com.kzsrm.model.Subject;
import com.kzsrm.model.Teacher;
import com.kzsrm.model.User;
import com.kzsrm.model.Video;
import com.kzsrm.model.VideoLog;
import com.kzsrm.model.WatchRecord;
import com.kzsrm.service.CollectionService;
import com.kzsrm.service.CourseService;
import com.kzsrm.service.ExamService;
import com.kzsrm.service.HomeworkService;
import com.kzsrm.service.OptionService;
import com.kzsrm.service.SubjectExamLogService;
import com.kzsrm.service.SubjectLogService;
import com.kzsrm.service.SubjectService;
import com.kzsrm.service.TeacherService;
import com.kzsrm.service.UserService;
import com.kzsrm.service.VideoLogService;
import com.kzsrm.service.VideoService;
import com.kzsrm.service.WatchRecordService;
import com.kzsrm.utils.ApiCode;
import com.kzsrm.utils.ComUtils;
import com.kzsrm.utils.DateUtil;
import com.kzsrm.utils.MapResult;

@Controller
@RequestMapping("/cour")
public class CourseController {
	private static Logger logger = LoggerFactory.getLogger(CourseController.class);
	JsonConfig courCf = ComUtils.jsonConfig(new String[]{});
	JsonConfig bannerCour = ComUtils.jsonConfig(new String[]{"address","bannerPic"});
	JsonConfig homeworkJC = ComUtils.jsonConfig(new String[] { "createTime" });

	@Resource private CourseService courseService;
	@Resource private ExamService examService;
	@Resource private VideoService videoService;
	@Resource private VideoLogService videoLogService;
	@Resource private SubjectService subjectService;
	@Resource private SubjectLogService subjecLogtService;
	@Resource private SubjectExamLogService subjecExamLogtService;
	@Resource private OptionService optionService;
	@Resource private UserService userService;
	@Resource private CollectionService collectionService;
	@Resource private HomeworkService homeworkService;
	@Resource private WatchRecordService recordService;
	@Resource private SubjectLogService subjectLogService;
	@Resource private TeacherService teacherService;

	/**
	 * 课程列表-三层
	 * @param pid		课程id，返回本级及其子集信息
	 * @param userid	用户id
	 * @param type		预留字段
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getCourList")
	public Map<String, Object> getCourList(
			@RequestParam(required = true) String pid,
			@RequestParam(required = false) String userid,
			@RequestParam(required = false) String type) {
		try{
			Map<String, Object> ret = MapResult.initMap();
			Course course = courseService.getById(pid);
			JSONObject _course = courseService.getMultilevelCour(course, userid, type);
			if(!TextUtils.isEmpty(userid)&&"1".equals(type)){
				List<Homework> homeworkList = homeworkService.getHomeworkList(userid, "", 1, 1);
				if(homeworkList.size()>0){
					JSONObject jsonObj = JSONObject.fromObject(homeworkList.get(0),
							homeworkJC);
					int count = subjectService.getSubNumByHomework(homeworkList.get(0).getId()+"");
					jsonObj.put("count", count);
					jsonObj.put("finishedCount", homeworkService.getHasDoneSubNum(homeworkList.get(0).getId(), userid));
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm");
					jsonObj.put("time", sdf.format(homeworkList.get(0).getCreateTime()));
					_course.put("homework", jsonObj);
				}
			}
			ret.put("result", _course);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	/**
	 * 课程列表-第二级
	 * @param userid	用户id
	 * @param type		预留字段
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getSecCourList")
	public Map<String, Object> getSecCourList(
			@RequestParam(required = false) String userid,
			@RequestParam(required = false) String type) {
		try{
			Map<String, Object> ret = MapResult.initMap();
			List<Course> courseList = courseService.getSecLevelCour(type);
			JSONArray jsonList = new JSONArray();
			for (Course course : courseList) {
				JSONObject jsonobj = JSONObject.fromObject(course, courCf);
				if (StringUtils.isNotBlank(userid))
					jsonobj.put("subNum", courseService.getHasDoneRightSubNum(course.getId(), userid, type));
				jsonList.add(jsonobj);
			}
			ret.put("result", jsonList);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	/**
	 * 课程首页推荐
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getBannerList")
	public Map<String, Object> getHomeVideoList() {
		try{
			JSONArray jsonList = new JSONArray();
			Map<String, Object> ret = MapResult.initMap();
			List<Course> courseList = courseService.getBannerList();
			for (Course course : courseList) {
				JSONObject jsonobj = JSONObject.fromObject(course, bannerCour);
				jsonobj.put("address", course.getBannerPic());
				jsonList.add(jsonobj);
			}
			ret.put("result", jsonList);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	/**
	 * 视频课程首页（新）列表 二级课程
	 * @param pid 1 数学 2 英语 3 逻辑 4 写作 5 面试
	 * @param order 排序类型 1默认  2 热度 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getVideoCourseList")
	public Map<String, Object> getVideoCourseList(
			@RequestParam(required = true) String pid,
			@RequestParam(required = true) String order) {
		try{
			
			JSONObject jsonObj = new JSONObject();
			
			//首页推荐课程
			JSONObject recommendObj = new JSONObject();
			recommendObj.put("courseId", "34");
			recommendObj.put("title", "导学课程");
			recommendObj.put("profile", "课程介绍");
			jsonObj.put("recommend", recommendObj);
			
			//首页精品课程
			JSONObject chargeObj = new JSONObject();
			chargeObj.put("title", "精品课程");
			chargeObj.put("profile", "课程介绍");
			jsonObj.put("charge", chargeObj);
			
			//课程列表
			String orderType = "orderIndex";
			if("2".equals(order)){
				orderType = "playcount desc";
			}
			List<Course> courseList = courseService.getchildrenCour(pid, "2",orderType);
			JSONArray jsonList = new JSONArray();
			for (Course course : courseList) {
				JSONObject jsonobj = JSONObject.fromObject(course, courCf);
				if(!TextUtils.isEmpty(jsonobj.getString("teacherId"))){
					Teacher teacher = teacherService.getTeacherById(jsonobj.getInt("teacherId"));
					jsonobj.put("teacherName", teacher.getName());
					jsonobj.put("teacherAvator", teacher.getAvatar());
				}
				jsonList.add(jsonobj);
			}
			jsonObj.put("courseList", jsonList);
			
			Map<String, Object> ret = MapResult.initMap();
			ret.put("result", jsonObj);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	/**
	 * 视频课程（新）列表 三级课程
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getVideoCourseSubList")
	public Map<String, Object> getVideoCourseSubList(
			@RequestParam(required = true) String pid) {
		try{
			Map<String, Object> ret = MapResult.initMap();
			JSONObject courseObj = JSONObject.fromObject(courseService.getById(pid), courCf);
			Teacher teacher = teacherService.getTeacherById(courseObj.getInt("teacherId"));
			courseObj.put("teacherName", teacher.getName());
			courseObj.put("teacherAvator", teacher.getAvatar());
			List<Course> courseList = courseService.getchildrenCour(pid, "2","orderIndex");
			JSONArray jsonList = new JSONArray();
			for (Course course : courseList) {
				JSONObject jsonobj = JSONObject.fromObject(course, courCf);
				jsonList.add(jsonobj);
			}
			courseObj.put("child",jsonList);
			ret.put("result", courseObj);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	/**
	 * 知识型谱 （统计做题结果）旧版
	 * @param userid	用户id
	 * @param type		1 题目课程 2 视频课程  固定传1
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getStatisticsList")
	public Map<String, Object> getStatisticsList(
			@RequestParam(required = false) String userid,
			@RequestParam(required = true) String type) {
		try{
			Map<String, Object> ret = MapResult.initMap();
			List<Course> topCourseList = courseService.getchildrenCour("0", type,"orderIndex");
			JSONArray statisticsList = new JSONArray();
			for(Course topCourse : topCourseList){
				JSONObject statisticsObj = new JSONObject();
				List<Course> courseList = courseService.getchildrenCour(topCourse.getId().toString(), type,"orderIndex");
				JSONArray jsonList = new JSONArray();
				for (Course course : courseList) {
					JSONObject jsonobj = JSONObject.fromObject(course, courCf);
					if (StringUtils.isNotBlank(userid))
						jsonobj.put("subNum", courseService.getHasDoneRightSubNum(course.getId(), userid, type));
					else
						jsonobj.put("subNum", "0");
					jsonList.add(jsonobj);
				}
				statisticsObj.put("id", topCourse.getId());
				statisticsObj.put("name", topCourse.getName());
				statisticsObj.put("child", jsonList);
				statisticsList.add(statisticsObj);
				
			}
			ret.put("result", statisticsList);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	/**
	 * 课程错题集列表
	 * @param userid	用户id
	 * @param type		1 考点 2 真题 3 模考
	 * @param subjectType		1 数学 2 英语 3 逻辑 4 写作
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getWrongList")
	public Map<String, Object> getWrongList(
			@RequestParam(required = true) String userId,
			@RequestParam(required = true) String type,
			@RequestParam(required = true) String subjectType) {
		try{
			Map<String, Object> ret = MapResult.initMap();
			if("1".endsWith(type)){
				List<Course> courseList = courseService.getchildrenCour(subjectType, "1","orderIndex");
				JSONArray jsonList = new JSONArray();
				for (Course course : courseList) {
					JSONObject jsonobj = new JSONObject();
					jsonobj.put("id", course.getId());
					jsonobj.put("name", course.getName());
					jsonobj.put("subAllNum", course.getSubAllNum());
					int wrongNum = 0;
					if (StringUtils.isNotBlank(userId)){
						List<Course> subList = courseService.getchildrenCour(course.getId()+"", "1","orderIndex");
						for(Course subCourse : subList){
							wrongNum+=courseService.getHasDoneWrongSubNum(subCourse.getId(), userId, "1");
						}
						jsonobj.put("wrongNum", wrongNum);
					}
					else{
						jsonobj.put("wrongNum", "0");
					}
					jsonList.add(jsonobj);
				}
				ret.put("result", jsonList);
			}else{
				JSONArray jsonArray = new JSONArray();
				List<Examination> examList = examService.getListByCour(subjectType,
						Integer.parseInt(type)-1+""); // 真题 or 模考

				for (int i = 0; i < examList.size(); i++) {
					JSONObject jsonObj = new JSONObject();
					Examination exam = examList.get(i);
					jsonObj.put("id", exam.getId());
					jsonObj.put("name", exam.getName());
					jsonObj.put("subAllNum", examService.getSubjectNum(exam.getId()));
					if(!StringUtils.isEmpty(userId)){
						jsonObj.put("wrongNum", examService.getDoneWrongNum(exam.getId(), userId));
					}else{
						jsonObj.put("wrongNum", 0);
					}
					jsonArray.add(jsonObj);
				}
				ret.put("result", jsonArray);
			}
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	/**
	 * 更新错题集
	 * @param questionType 1 考点 2模考或真题
	 * @param type 1添加错题 2  删除错题
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateWrongSet")
	public Map<String, Object> updateWrongSet(
			@RequestParam(required = true) String userId,
			@RequestParam(required = true) String subjectId,
			@RequestParam(required = true) int type,
			@RequestParam(required = true) int questionType) {
		try{
			if (StringUtils.isBlank(subjectId))
				return MapResult.initMap(ApiCode.PARG_ERR, "请选择题目");
			
			Map<String, Object> ret = MapResult.initMap();
			if(questionType==1){
				subjecLogtService.updateWrongSet(userId, subjectId, type);
			}else if(questionType==2){
				subjecExamLogtService.updateWrongSet(userId, subjectId, type);
			}
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	/**
	 * 获取知识点对应的测试题
	 * @param pointId		知识点id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getPoiSubList")
	public Map<String, Object> getPoiSubjectList(
			@RequestParam(required = true) String pointId) {
		try{
			if (StringUtils.isBlank(pointId))
				return MapResult.initMap(ApiCode.PARG_ERR, "知识点id为空");
			
			Map<String, Object> ret = MapResult.initMap();
			List<Subject> subList = subjectService.getSubjectByPoint(pointId);
			ret.put("result", subList);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	/**
	 * 获取知识点对应的视频
	 * @param pointId		知识点id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getVideoByPoi")
	public Map<String, Object> getVideoByPoi(@RequestParam(required = false) String userId,
			@RequestParam(required = true) String pointId) {
		try{
			if (StringUtils.isBlank(pointId))
				return MapResult.initMap(ApiCode.PARG_ERR, "知识点id为空");
			
			//更新观看数量
			Course course = courseService.getById(pointId);
			if(course.getType().equals("2")){
				courseService.updateCourseInfo(pointId);
			}
			
			Map<String, Object> ret = MapResult.initMap();
			List<Video> videoList = videoService.getVideoListByPoint(userId,pointId);
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("pointId", pointId);
			String isCollect = "0";
			String playIndex = "0"; //上次播放的视频
			String playProgress = "0";// 上次播放的位置
			if(!StringUtils.isEmpty(userId)){
				User user = userService.selectUser(Integer.parseInt(userId));
				Collection c = collectionService.getByUserIdAndVideoId(user.getId(), Integer.parseInt(pointId),1);
				if(c!=null){
					isCollect = "1";
				}
				WatchRecord watchRecord = recordService.getUserWatchRecord(userId, "1", pointId);
				if(watchRecord!=null){
					playIndex = watchRecord.getCurrentIndex()+"";
					playProgress = watchRecord.getCurrentTime();
				}
				for(int i=0;i< videoList.size();i++){
					Video video = videoList.get(i);
					if(video.getIsfree()==0){
						if(user.getVipTime()!=null&&DateUtil.getDifferSec(user.getVipTime(), new Date())<0){
							video.setIsfree(1);
						}else{
							video.setIsfree(0);
						}
					}else{
						video.setIsfree(1);
					}
					videoList.set(i, video);
				}
			}
			jsonObj.put("isCollect", isCollect);
			jsonObj.put("playIndex", playIndex);
			jsonObj.put("playProgress", playProgress);
			jsonObj.put("videoList", videoList);
			ret.put("result", jsonObj);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	/**
	 * 获取知识点详细信息
	 * @param pointId		知识点id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getPointDetail")
	public Map<String, Object> getPointDetail(
			@RequestParam(required = true) String pointId) {
		try{
			if (StringUtils.isBlank(pointId))
				return MapResult.initMap(ApiCode.PARG_ERR, "知识点id为空");
			
			Map<String, Object> ret = MapResult.initMap();
			Course point = courseService.getById(pointId);
			ret.put("result", point);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	/**
	 * 提交测试题的答案
	 * @param userId		用户id，用于记录做题日志
	 * @param answer		答案，json格式[{"no":"序号","optId":"选项id","timeSpan":"秒"},{},...]
	 * @param type			试题类型，1-课程，2-模考 3-作业
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkAnswer")
	public Map<String, Object> checkAnswer(
			@RequestParam(required = false) String userId,
			@RequestParam(required = true) String answer,
			@RequestParam(required = true) String type) {
		try{
			if (StringUtils.isBlank(answer))
				return MapResult.initMap(ApiCode.PARG_ERR, "答案为空");
			
			Map<String, Object> ret = MapResult.initMap();
			// 提交答案
			JSONArray subList = subjectService.checkAnswer(userId, answer, type);
			// 记录已做题书
			if(!TextUtils.isEmpty(userId)){
				User user = userService.selectUser(Integer.parseInt(userId));
				user.setAnswerNum(subjectLogService.getTotalFinishCount(userId)+"");
				userService.updateUser(user);
			}
			ret.put("result", subList);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	/**
	 * 获取试题答案
	 * @param subjectId			试题id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getSubjectAnswer")
	public Map<String, Object> getSubjectAnswer(
			@RequestParam(required = true) String subjectId) {
		try{
			if (StringUtils.isBlank(subjectId))
				return MapResult.initMap(ApiCode.PARG_ERR, "试题id为空");
			
			Map<String, Object> ret = MapResult.initMap();
			Option option = optionService.getSubjectAnswer(subjectId);
			ret.put("result", option);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	/**
	 * 获取试题提示
	 * @param subjectId			试题id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getSubjectHint")
	public Map<String, Object> getSubjectHint(
			@RequestParam(required = true) String subjectId) {
		try{
			if (StringUtils.isBlank(subjectId))
				return MapResult.initMap(ApiCode.PARG_ERR, "试题id为空");
			
			Map<String, Object> ret = MapResult.initMap();
			Subject subject = subjectService.getById(subjectId);
			ret.put("result", subject.getHint());
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	/**
	 * 刷新知识点下的题目总数
	 * @param type
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/refreshSubAllNum")
	public Map<String, Object> refreshSubAllNum(@RequestParam(required = true) String type) {
		try{
			Map<String, Object> ret = MapResult.initMap();
			courseService.refreshSubAllNum("0", type);
			ret.put("result", "成功！");
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	/**
	 * 记录视频观看记录
	 * @param videoId
	 * @param timeSpan
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/setVideoLog")
	public Map<String, Object> setVideoLog(
			@RequestParam(required = true) String userId,
			@RequestParam(required = true) String videoId,
			@RequestParam(required = true) Integer timeSpan) {
		try{
			Map<String, Object> ret = MapResult.initMap();
			videoLogService.setVideoLog(userId, videoId, timeSpan);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	/**
	 * 查看视频观看记录
	 * @param videoId
	 * @param timeSpan
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getVideoLog")
	public Map<String, Object> getVideoLog(
			@RequestParam(required = true) String userId,
			@RequestParam(required = true) String videoId) {
		try{
			Map<String, Object> ret = MapResult.initMap();
			VideoLog vlog = videoLogService.getVideoLog(userId, videoId);
			ret.put("result", vlog);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	/**
	 * 查看视频观看记录
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getHistoryList")
	public Map<String, Object> getHistoryList(@RequestParam(required = false) String videoId,
			@RequestParam(defaultValue="20") int pageSize) {
		try{
			Map<String, Object> ret = MapResult.initMap();
			List<VideoLog> vlogList = videoLogService.getVideoList(videoId,pageSize);
			JSONArray recordList = new JSONArray();
			for(VideoLog log :vlogList){
				User user = userService.selectUser(Integer.parseInt(log.getUserid()));
				if(!StringUtils.isEmpty(log.getVid())){
					Video video = videoService.getById(log.getVid());
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("logId", log.getId());
					jsonObj.put("userName", user.getName());
					jsonObj.put("userAvatar", user.getAvator());
					jsonObj.put("videoId", video.getId());
					jsonObj.put("videoName", video.getName());
					jsonObj.put("courseId", log.getCourseId());
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					jsonObj.put("time", sdf.format(log.getCreatetime()));
					recordList.add(jsonObj);
				}
			}
			ret.put("result", recordList);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	/**
	 * 错题集
	 * @param userid	用户id
	 * @param cid		课程id(第二级)
	 * @param type		1 考点 2 模考或真题
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getWrongSubSet")
	public Map<String, Object> getWrongSubSet(
			@RequestParam(required = true) String userid,
			@RequestParam(required = true) String cid,
			@RequestParam(required = false) String type) {
		try{
			Map<String, Object> ret = MapResult.initMap();
			if("1".equals(type)){
				List<Subject> subjectList = courseService.getWrongSubSet(userid, cid, "1");
				ret.put("result", subjectList);
			}else{
				List<Subject> subjectList = examService.getExamWrongSubSet(userid, cid);
				ret.put("result", subjectList);
			}
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
}
