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

import com.kzsrm.model.CirclePraise;
import com.kzsrm.model.Comment;
import com.kzsrm.model.Course;
import com.kzsrm.model.OlaCircle;
import com.kzsrm.model.User;
import com.kzsrm.service.CirclePraiseService;
import com.kzsrm.service.CommentService;
import com.kzsrm.service.CourseService;
import com.kzsrm.service.OlaCircleService;
import com.kzsrm.service.UserService;
import com.kzsrm.service.VideoService;
import com.kzsrm.utils.ComUtils;
import com.kzsrm.utils.MapResult;

@Controller
@RequestMapping("/circle")
public class OlaCircleController {
	JsonConfig circleJC = ComUtils.jsonConfig(new String[]{"createTime"});
	JsonConfig commentJC = ComUtils.jsonConfig(new String[]{"createTime"});

	private static Logger logger = LoggerFactory
			.getLogger(OlaCircleController.class);

	@Resource
	private UserService userService;
	@Resource
	private OlaCircleService circleService;
	@Resource
	private CirclePraiseService praiseService;
	@Resource
	private CommentService commentService;
	@Resource
	private CourseService courseService;
	@Resource
	private VideoService videoService;

	/**
	 * 欧拉圈发帖
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addOlaCircle")
	public Map<String, Object> addOlaCircle(
			@RequestParam(required = true) int userId,
			@RequestParam(required = false) String title,
			@RequestParam(required = false) String content,
			@RequestParam(required = false) String imageGids,
			@RequestParam(required = false) String assignUser,
			@RequestParam(defaultValue="1") int isPublic,
			@RequestParam(required = false) String location) {
		OlaCircle circle = new OlaCircle();
		circle.setUserId(userId);
		circle.setTitle(title);
		circle.setContent(content);
		circle.setImageGids(imageGids);
		circle.setLocation(location);
		circle.setAssignUser(assignUser);
		circle.setIsPublic(isPublic);
		circle.setType(2); // 帖子
		circle.setCreateTime(new Date());
		try {
			circleService.addOlaCircle(circle);
			return MapResult.initMap();
		} catch (Exception e) {
			// TODO: handle exception
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
	@RequestMapping(value = "/getCircleList")
	public Map<String, Object> getCircleList(
			@RequestParam(required = false) String userId,
			@RequestParam(required = false) String circleId,
			@RequestParam(defaultValue = "20") int pageSize,
			@RequestParam(required = false) String type) {
		try {
			Map<String, Object> ret = MapResult.initMap();
			List<OlaCircle> vlogList = circleService.getCircleList(userId,circleId,
					pageSize,type);
			JSONArray cicleList = new JSONArray();
			for (OlaCircle circle : vlogList) {
				User user = userService.selectUser(circle.getUserId());
				if(user!=null){
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("circleId", circle.getId());
					jsonObj.put("userId", user.getId());
					jsonObj.put("userName", TextUtils.isEmpty(user.getName())?"小欧":user.getName());
					jsonObj.put("userAvatar", user.getAvator());
					jsonObj.put("type", circle.getType());
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					jsonObj.put("time", sdf.format(circle.getCreateTime()));

					if (circle.getType() == 1) {
						if (circle.getCourseId() != null) {
							Course course = courseService.getById(circle
									.getCourseId() + "");
							jsonObj.put("courseId", circle.getCourseId());
							jsonObj.put("videoId", circle.getVideoId());
							jsonObj.put("title", "学习记录");
							jsonObj.put("content", "学习了：" + course.getName());
							jsonObj.put("imageGids", course.getAddress());
							jsonObj.put("location", user.getLocal());
							cicleList.add(jsonObj);
						}
					} else if (circle.getType() == 2) {
						jsonObj.put("title", circle.getTitle());
						jsonObj.put("content", circle.getContent());
						jsonObj.put("imageGids", circle.getImageGids());
						jsonObj.put("location", circle.getLocation());
						jsonObj.put("praiseNumber", praiseService.queryPraiseNumber(circle.getId()+""));
						int isPraised = 0;
						if(!TextUtils.isEmpty(userId)){
							if(praiseService.queryIsPraised(userId, circle.getId()+"")){
								isPraised = 1;
							}
						}
						jsonObj.put("isPraised", isPraised);
						jsonObj.put("readNumber", circle.getReadNumber());
						jsonObj.put("commentNumber",circle.getCommentNumber());
						cicleList.add(jsonObj);
					}
				}
			}
			ret.put("result", cicleList);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	/**
	 * 帖子详情
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryCircleDetail")
	public Map<String, Object> queryCircleDetail(
			@RequestParam(required = true) String circleId,
			@RequestParam(required = false) String userId){
		try {
			Map<String, Object> ret = MapResult.initMap();
			OlaCircle circle = circleService.getById(circleId);
			circleService.updateReadNumber(circle);  //更新浏览量
			if(!TextUtils.isEmpty(userId)){
				updateReadState(userId,circle); //更新消息阅读状态
				praiseService.updateReadState(userId, circleId);
			}
			User postUser = userService.selectUser(circle.getUserId());
			JSONObject jsonobj = JSONObject.fromObject(circle, circleJC);
			jsonobj.put("userName", postUser.getName());
			jsonobj.put("userAvatar", postUser.getAvator());
			int isPraised = 0;
			if(!TextUtils.isEmpty(userId)){
				if(praiseService.queryIsPraised(userId, circleId)){
					isPraised = 1;
				}
			}
			jsonobj.put("isPraised", isPraised);
			SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			jsonobj.put("time", sdf.format(circle.getCreateTime()));
			List<Comment> commentList = commentService.getCommentList(Integer.parseInt(circleId), 2,"");
			JSONArray commentArray = new JSONArray();
			for(Comment comment:commentList){
				JSONObject commentJson = JSONObject.fromObject(comment, commentJC);
				User user = userService.selectUser(comment.getUserId());
				commentJson.put("userName", user.getName());
				commentJson.put("userAvatar", user.getAvator());
				commentJson.put("location", user.getLocal());
				if(!StringUtils.isEmpty(comment.getToUserId())){
					User toUser = userService.selectUser(Integer.parseInt(comment.getToUserId()));
					commentJson.put("toUserName", toUser.getName());
				}
				commentJson.put("time", sdf.format(comment.getCreateTime()));
				commentArray.add(commentJson);
			}
			jsonobj.put("commentList", commentArray);
			ret.put("result", jsonobj);
			return ret;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	/**
	 * 更新留言的阅读状态
	 * 
	 * @return
	 */
	public void updateReadState(String userId, OlaCircle post) {
		if((post.getUserId()+"").equals(userId)){
			//更新自己所发贴
			commentService.updateReadState(userId, post.getId()+"");
		}else{
			//更新自己所发贴
			commentService.updateOtherReadState(userId, post.getId()+"");
		}
	}

	
	/**
	 * 点赞
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/praiseCirclePost")
	public Map<String, Object> praiseCirclePost(@RequestParam(required = false) String userId,
			@RequestParam(required = false) String circleId){
		try {
			if(TextUtils.isEmpty(userId)){
				return MapResult.initMap(9999, "您尚未登录");
			}
			praiseService.praiseCircle(userId, circleId);
			return MapResult.initMap();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	/**
	 * 点赞列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getPraiseList")
	public Map<String, Object> getPraiseList(@RequestParam(required = true) String userId,
			@RequestParam(required = false) String praiseId,@RequestParam(defaultValue = "20") int pageSize){
		try {
			Map<String, Object> ret = MapResult.initMap();
			List<CirclePraise> praiseList = praiseService.getPraiseList(userId, praiseId, pageSize);
			JSONArray praiseArray = new JSONArray();
			for(CirclePraise praise:praiseList){
				User u = userService.selectUser(Integer.parseInt(praise.getUserId()));
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("praiseId", praise.getId());
				jsonObj.put("postId", praise.getCircleId());
				OlaCircle circle = circleService.getById(praise.getCircleId());
				if(circle!=null){
					jsonObj.put("title", circle.getTitle());
				}else{
					jsonObj.put("title", "");
				}
				jsonObj.put("userId", praise.getUserId());
				jsonObj.put("userName", u.getName());
				jsonObj.put("userAvatar", u.getAvator());
				SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
				jsonObj.put("time", dateFormater.format(praise.getCreateTime()));
				jsonObj.put("isRead", praise.getIsRead());
				praiseArray.add(jsonObj);
			}
			ret.put("result", praiseArray);
			return ret;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	/**
	 * 个人资料及所发帖 所回答贴
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getUserPostList")
	public Map<String, Object> getUserPostList(
			@RequestParam(required = true) String userId){
		try {
			Map<String, Object> ret = MapResult.initMap();
			JSONObject jsonObj = new JSONObject();
			User user = userService.selectUser(Integer.parseInt(userId));
			List<OlaCircle> deployList = circleService.getDeployPostList(userId);
			jsonObj.put("id", user.getId());
			jsonObj.put("name", user.getName());
			jsonObj.put("avator", user.getAvator());
			jsonObj.put("sign", user.getSign());
			JSONArray deployArray = new JSONArray();
			for(OlaCircle circle:deployList){
				JSONObject obj = JSONObject.fromObject(circle,circleJC);
				obj.put("userName", user.getName());
				obj.put("userAvatar", user.getAvator());
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				obj.put("time", sdf.format(circle.getCreateTime()));
				deployArray.add(obj);
			}
			jsonObj.put("deployList", deployArray);
			JSONArray replyArray = new JSONArray();
			List<OlaCircle> replyList = circleService.getReplyPostList(userId);
			for(OlaCircle circle:replyList){
				JSONObject obj = JSONObject.fromObject(circle,circleJC);
				obj.put("userName", user.getName());
				obj.put("userAvatar", user.getAvator());
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				obj.put("time", sdf.format(circle.getCreateTime()));
				replyArray.add(obj);
			}
			jsonObj.put("replyList", replyArray);
			ret.put("result", jsonObj);
			return ret;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	/**
	 * 浏览量
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateReadNumber")
	public Map<String, Object> updateReadNumber(
			@RequestParam(required = false) String circleId){
		try {
			OlaCircle circle = circleService.getById(circleId);
			circleService.updateReadNumber(circle);
			return MapResult.initMap();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("", e);
			return MapResult.failMap();
		}
	}

}
