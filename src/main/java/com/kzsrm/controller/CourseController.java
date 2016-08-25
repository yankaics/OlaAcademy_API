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
import com.kzsrm.model.Homework;
import com.kzsrm.model.Option;
import com.kzsrm.model.Subject;
import com.kzsrm.model.User;
import com.kzsrm.model.Video;
import com.kzsrm.model.VideoLog;
import com.kzsrm.service.CollectionService;
import com.kzsrm.service.CourseService;
import com.kzsrm.service.HomeworkService;
import com.kzsrm.service.OptionService;
import com.kzsrm.service.PointLogService;
import com.kzsrm.service.SubjectService;
import com.kzsrm.service.UserService;
import com.kzsrm.service.VideoLogService;
import com.kzsrm.service.VideoService;
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
	@Resource private VideoService videoService;
	@Resource private VideoLogService videoLogService;
	@Resource private SubjectService subjectService;
	@Resource private PointLogService pointLogService;
	@Resource private OptionService optionService;
	@Resource private UserService userService;
	@Resource private CollectionService collectionService;
	@Resource private HomeworkService homeworkService;

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
				List<Homework> homeworkList = homeworkService.getHomeworkList(userid, "", 1);
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
	 * 首页推荐
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
	 * 知识型谱 （统计做题结果）
	 * @param userid	用户id
	 * @param type		
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getStatisticsList")
	public Map<String, Object> getStatisticsList(
			@RequestParam(required = false) String userid,
			@RequestParam(required = true) String type) {
		try{
			Map<String, Object> ret = MapResult.initMap();
			List<Course> topCourseList = courseService.getchildrenCour("0", type);
			JSONArray statisticsList = new JSONArray();
			for(Course topCourse : topCourseList){
				JSONObject statisticsObj = new JSONObject();
				List<Course> courseList = courseService.getchildrenCour(topCourse.getId().toString(), type);
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
			if(!StringUtils.isEmpty(userId)){
				User user = userService.selectUser(Integer.parseInt(userId));
				Collection c = collectionService.getByUserIdAndVideoId(user.getId(), Integer.parseInt(pointId));
				if(c!=null){
					isCollect = "1";
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
			JSONArray subList = subjectService.checkAnswer(userId, answer, type);
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
	 * @param type		
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
			List<Subject> SubjectList = courseService.getWrongSubSet(userid, cid, type);
			ret.put("result", SubjectList);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
}
