package com.kzsrm.controller;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kzsrm.model.Course;
import com.kzsrm.model.Message;
import com.kzsrm.model.User;
import com.kzsrm.service.CirclePraiseService;
import com.kzsrm.service.CommentService;
import com.kzsrm.service.CourseService;
import com.kzsrm.service.MessageRecordService;
import com.kzsrm.service.MessageService;
import com.kzsrm.service.UserService;
import com.kzsrm.utils.ComUtils;
import com.kzsrm.utils.MapResult;

@Controller
@RequestMapping("/message")
public class MessageController {
	
	JsonConfig messageJC = ComUtils.jsonConfig(new String[]{"createTime"});

	private static Logger logger = LoggerFactory
			.getLogger(MessageController.class);

	@Resource
	private UserService userService;
	@Resource
	private CourseService courService;
	@Resource
	private MessageService messageService;
	@Resource
	private MessageRecordService recordService;
	@Resource
	private CommentService commentService;
	@Resource
	private CirclePraiseService praiseService;

	/**
	 * 消息列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getMessageList")
	public Map<String, Object> getMessageList(
			@RequestParam(required = false) String messageId,
			@RequestParam(defaultValue = "20") int pageSize,
			@RequestParam(required = true) int userId) {
		try {
			Map<String, Object> ret = MapResult.initMap();
			List<Message> messageList = messageService.getMessageList(userId,messageId,pageSize);
			JSONArray messageArray = new JSONArray();
			for(Message message:messageList){
				JSONObject jsonObj = JSONObject.fromObject(message, messageJC);
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				jsonObj.put("time", sdf.format(message.getCreateTime()));
				if(message.getType()==1){
					User user = userService.selectUser(userId);
					jsonObj.put("imageUrl", user.getAvator());
					jsonObj.put("url", "");
				}else if(message.getType()==2){
					Course course = courService.getById(message.getOtherId()+"");
					jsonObj.put("imageUrl", course.getAddress());
					jsonObj.put("url", "");
				}else if(message.getType()==3){
					Course course = courService.getById(message.getOtherId()+"");
					jsonObj.put("imageUrl", course.getBannerPic());
					jsonObj.put("url", course.getProfile());
				}
				messageArray.add(jsonObj);
			}
			ret.put("result", messageArray);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	/**
	 * 消息列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addMessageRecord")
	public Map<String, Object> addMessageRecord(
			@RequestParam(required = true) String messageIds,
			@RequestParam(required = true) int userId) {
		try {
			Map<String, Object> ret = MapResult.initMap();
			recordService.addMessageRecord(userId, messageIds);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	/**
	 * 未读消息数（老版本）
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getUnreadCount")
	public Map<String, Object> getUnreadCount(
			@RequestParam(required = true) int userId) {
		try {
			Map<String, Object> ret = MapResult.initMap();
			List<Message> messageList = messageService.getUnreadMessageList(userId);
			ret.put("result", messageList.size());
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	/**
	 * 未读消息数
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getUnreadTotalCount")
	public Map<String, Object> getUnreadTotalCount(
			@RequestParam(required = true) int userId) {
		try {
			Map<String, Object> ret = MapResult.initMap();
			List<Message> messageList = messageService.getUnreadMessageList(userId);
			Integer circleCount = commentService.getUnreadMessageCount(userId, 2);
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("systemCount",  messageList.size());
			jsonObj.put("circleCount", circleCount);
			jsonObj.put("praiseCount", praiseService.getPraiseCount(userId));
			ret.put("result", jsonObj);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}

}
