package com.kzsrm.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kzsrm.model.Comment;
import com.kzsrm.model.OlaCircle;
import com.kzsrm.model.User;
import com.kzsrm.service.CommentService;
import com.kzsrm.service.CourseService;
import com.kzsrm.service.OlaCircleService;
import com.kzsrm.service.UserService;
import com.kzsrm.utils.MapResult;
import com.push.AndroidNotification;
import com.push.PushClient;
import com.push.PushConfig;
import com.push.android.AndroidCustomizedcast;
import com.push.ios.IOSCustomizedcast;

@Controller
@RequestMapping("/comment")
public class CommentController {
	
	private static Logger logger = LoggerFactory
			.getLogger(CourseController.class);
	
	@Resource
	private UserService userService;
	@Resource
	private CourseService courseService;
	@Resource
	private CommentService commentService;
	@Resource
	private OlaCircleService circleService;
	
	private PushClient client = new PushClient();

	/**
	 * 添加评论
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addComment")
	public Map<String, Object> addComment(
			@RequestParam(required = true) int userId,
			@RequestParam(required = true) int postId,
			@RequestParam(required = false) String toUserId,
			@RequestParam(required = false) String content,
			@RequestParam(required = false) String imageIds,
			@RequestParam(required = false) String videoUrls,
			@RequestParam(required = false) String videoImgs,
			@RequestParam(required = false) String audioUrls,
			@RequestParam(required = false) String location,
			@RequestParam(required = true) int type) {
		Comment comment = new Comment();
		comment.setUserId(userId);
		comment.setContent(content);
		comment.setLocation(location);
		comment.setPostId(postId);
		comment.setImageIds(imageIds);
		comment.setVideoImgs(videoImgs);
		comment.setVideoUrls(videoUrls);
		comment.setAudioUrls(audioUrls);
		comment.setToUserId(toUserId);
		comment.setType(type); // 帖子
		comment.setCreateTime(new Date());
		try {
			commentService.addComment(comment);
			// 友盟推送消息
			OlaCircle circle = circleService.getById(postId+"");
			String pushUserId = circle.getUserId()+"";
			if(!TextUtils.isEmpty(toUserId)){
				pushUserId = toUserId;
			}
			User u = userService.selectUser(Integer.parseInt(pushUserId));
			sendIOSCustomizedcast(u.getName(),u.getPhone());
			sendAndroidCustomizedcast(u.getName(),u.getPhone());
			return MapResult.initMap();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	private void sendAndroidCustomizedcast(String userName,String phone) throws Exception {
		AndroidCustomizedcast customizedcast = new AndroidCustomizedcast(PushConfig.APP_KEY, PushConfig.APP_SECRECT);
		// TODO Set your alias here, and use comma to split them if there are multiple alias.
		// And if you have many alias, you can also upload a file containing these alias, then 
		// use file_id to send customized notification.
		customizedcast.setAlias(phone, "USR_PHONE");
		customizedcast.setTicker("欧拉圈留言");
		customizedcast.setTitle("欧拉学院");
		customizedcast.setText("您收到一条来自"+userName+"的留言");
		customizedcast.goAppAfterOpen();
		customizedcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		// TODO Set 'production_mode' to 'false' if it's a test device. 
		// For how to register a test device, please see the developer doc.
		customizedcast.setProductionMode();
		client.send(customizedcast);
	}
	
	private void sendIOSCustomizedcast(String userName,String phone) throws Exception {
		IOSCustomizedcast customizedcast = new IOSCustomizedcast(PushConfig.APP_KEY, PushConfig.APP_SECRECT);
		customizedcast.setAlias(phone, "USER_PHONE");
		customizedcast.setAlert("您收到一条来自"+userName+"的留言");
		customizedcast.setBadge(1);
		customizedcast.setSound("default");
		customizedcast.setCustomizedField("type", "5");
		customizedcast.setProductionMode();
		client.send(customizedcast);
	}

	/**
	 * 查看评论列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getCommentList")
	public Map<String, Object> getCommentList(
			@RequestParam(required = true) int postId,
			@RequestParam(required = true) int type,
			@RequestParam(defaultValue = "0") int assign) {
		try {
			Map<String, Object> ret = MapResult.initMap();
			// 指定回答
			String userId = "";
			if(assign==1){
				OlaCircle circle = circleService.getById(postId+"");
				if(!TextUtils.isEmpty(circle.getAssignUser())){
					userId = circle.getAssignUser();
				}else{
					userId = "0";
				}
			}
			List<Comment> commentList = commentService.getCommentList(postId, type,userId);
			JSONArray jsonArray = new JSONArray();
			for (Comment comment : commentList) {
				User user = userService.selectUser(comment.getUserId());
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("commentId", comment.getId());
				jsonObj.put("userId", comment.getUserId());
				jsonObj.put("userName", user.getName());
				jsonObj.put("userAvatar", user.getAvator());
				jsonObj.put("location", user.getLocal());
				if(!StringUtils.isEmpty(comment.getToUserId())){
					User toUser = userService.selectUser(Integer.parseInt(comment.getToUserId()));
					jsonObj.put("toUserId", comment.getToUserId());
					jsonObj.put("toUserName", toUser.getName());
				}
				jsonObj.put("content", comment.getContent());
				jsonObj.put("imageIds", comment.getImageIds());
				jsonObj.put("videoUrls", comment.getVideoUrls());
				jsonObj.put("videoImgs", comment.getVideoImgs());
				jsonObj.put("audioUrls", comment.getAudioUrls());
				jsonObj.put("praiseNumber", comment.getPraiseNumber());
				jsonObj.put("subCount", commentService.getSubCommentCount(comment.getUserId()+"", postId+""));
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				jsonObj.put("time", sdf.format(comment.getCreateTime()));
				jsonArray.add(jsonObj);
			}
			ret.put("result", jsonArray);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	/**
	 * 消息列表（收到的回复）
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getCircleMessageList")
	public Map<String, Object> getCircleMessageList(
			@RequestParam(required = true) int userId,
			@RequestParam(required = true) int type,
			@RequestParam(required = false) String commentId,
			@RequestParam(defaultValue="20") int pageSize) {
		try {
			Map<String, Object> ret = MapResult.initMap();
			List<Comment> commentList = commentService.getMessageList(userId, type,commentId,pageSize);
			JSONArray jsonArray = new JSONArray();
			for (Comment comment : commentList) {
				User user = userService.selectUser(comment.getUserId());
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("commentId", comment.getId());
				jsonObj.put("postId", comment.getPostId());
				jsonObj.put("userId", comment.getUserId());
				jsonObj.put("userName", user.getName());
				jsonObj.put("userAvatar", user.getAvator());
				jsonObj.put("location", user.getLocal());
				OlaCircle circle = circleService.getById(comment.getPostId()+"");
				if(!StringUtils.isEmpty(comment.getToUserId())){
					User toUser = userService.selectUser(Integer.parseInt(comment.getToUserId()));
					jsonObj.put("toUserId", comment.getToUserId());
					jsonObj.put("toUserName", toUser.getName());
				}
				if(circle!=null){
					jsonObj.put("title", circle.getTitle());
				}else{
					jsonObj.put("title", "");
				}
				jsonObj.put("content", comment.getContent());
				jsonObj.put("imageIds", comment.getImageIds());
				jsonObj.put("videoUrls", comment.getVideoUrls());
				jsonObj.put("videoImgs", comment.getVideoImgs());
				jsonObj.put("audioUrls", comment.getAudioUrls());
				jsonObj.put("praiseNumber", comment.getPraiseNumber());
				jsonObj.put("isRead", comment.getIsRead());
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				jsonObj.put("time", sdf.format(comment.getCreateTime()));
				jsonArray.add(jsonObj);
			}
			ret.put("result", jsonArray);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
}
