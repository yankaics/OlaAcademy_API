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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kzsrm.model.Group;
import com.kzsrm.model.Homework;
import com.kzsrm.model.Subject;
import com.kzsrm.service.GroupService;
import com.kzsrm.service.HomeworkService;
import com.kzsrm.service.SubjectService;
import com.kzsrm.service.UserService;
import com.kzsrm.utils.ApiCode;
import com.kzsrm.utils.ComUtils;
import com.kzsrm.utils.MapResult;

@Controller
@RequestMapping("/homework")
public class HomeworkController {

	JsonConfig jsonConf = ComUtils.jsonConfig(new String[] { "createTime" });

	private static Logger logger = LoggerFactory
			.getLogger(BroadcastController.class);

	@Resource
	private UserService userService;
	@Resource
	private GroupService groupService;
	@Resource
	private HomeworkService homeworkService;
	@Resource
	private SubjectService subjectService;
	
	/**
	 * 创建群
	 */
	@ResponseBody
	@RequestMapping(value = "/createGroup")
	public Map<String, Object> createGroup(@RequestParam(required = true) String name,
			@RequestParam(required = true) int userId,@RequestParam(required = false) String avatar) {
		Group group = new Group();
		group.setName(name);
		group.setCreateUser(userId);
		group.setAvatar(avatar);
		group.setCreateTime(new Date());
		try {
			groupService.createGroup(group);
			return MapResult.initMap();
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	/**
	 * 用户(老师)所创建群列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getTeacherGroupList")
	public Map<String, Object> getTeacherGroupList(
			@RequestParam(required = true) String userId) {
		try {
			Map<String, Object> ret = MapResult.initMap();
			List<Group> groupList = groupService.getTeacherGroupList(userId);
			JSONArray groupArray = new JSONArray();
			for (Group group : groupList) {
				JSONObject jsonObj = JSONObject.fromObject(group,
						jsonConf);
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm");
				jsonObj.put("time", sdf.format(group.getCreateTime()));
				groupArray.add(jsonObj);
			}
			ret.put("result", groupArray);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	/**
	 * 用户(学生)所加入群列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getUserGroupList")
	public Map<String, Object> getUserGroupList(
			@RequestParam(required = true) String userId) {
		try {
			Map<String, Object> ret = MapResult.initMap();
			List<Group> groupList = groupService.getUserGroupList(userId);
			JSONArray groupArray = new JSONArray();
			for (Group group : groupList) {
				JSONObject jsonObj = JSONObject.fromObject(group,
						jsonConf);
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm");
				jsonObj.put("time", sdf.format(group.getCreateTime()));
				groupArray.add(jsonObj);
			}
			ret.put("result", groupArray);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}

	/**
	 * 用户作业列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getHomeworkList")
	public Map<String, Object> getHomeworkList(
			@RequestParam(required = true) String userId,
			@RequestParam(required = false) String homeworkId,
			@RequestParam(defaultValue = "20") int pageSize) {
		try {
			Map<String, Object> ret = MapResult.initMap();
			List<Homework> workList = homeworkService.getHomeworkList(userId, homeworkId, pageSize);
			JSONArray homeworkList = new JSONArray();
			for (Homework homework : workList) {
				int count = subjectService.getSubNumByHomework(homework.getId()+"");
				JSONObject jsonObj = JSONObject.fromObject(homework,
						jsonConf);
				jsonObj.put("count", count);
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm");
				jsonObj.put("finishedCount", homeworkService.getHasDoneSubNum(homework.getId(), userId));
				jsonObj.put("time", sdf.format(homework.getCreateTime()));
				homeworkList.add(jsonObj);
			}
			ret.put("result", homeworkList);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	/**
	 * 获取作业对应的测试题
	 * @param pointId		知识点id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getSubjectList")
	public Map<String, Object> getSubjectList(
			@RequestParam(required = true) String homeworkId) {
		try{
			if (StringUtils.isBlank(homeworkId))
				return MapResult.initMap(ApiCode.PARG_ERR, "作业id为空");
			
			Map<String, Object> ret = MapResult.initMap();
			List<Subject> subList = subjectService.getSubjectByHomework(homeworkId);
			ret.put("result", subList);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}

}
