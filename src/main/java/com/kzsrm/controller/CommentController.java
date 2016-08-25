package com.kzsrm.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kzsrm.model.Comment;
import com.kzsrm.model.User;
import com.kzsrm.service.CommentService;
import com.kzsrm.service.CourseService;
import com.kzsrm.service.UserService;
import com.kzsrm.utils.MapResult;

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

	/**
	 * 欧拉圈发帖
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
			@RequestParam(required = false) String location,
			@RequestParam(required = true) int type) {
		Comment comment = new Comment();
		comment.setUserId(userId);
		comment.setContent(content);
		comment.setLocation(location);
		comment.setPostId(postId);
		comment.setToUserId(toUserId);
		comment.setType(type); // 帖子
		comment.setCreateTime(new Date());
		try {
			commentService.addComment(comment);
			return MapResult.initMap();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("", e);
			return MapResult.failMap();
		}
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
			@RequestParam(required = true) int type) {
		try {
			Map<String, Object> ret = MapResult.initMap();
			List<Comment> commentList = commentService.getCommentList(postId, type);
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
				jsonObj.put("praiseNumber", comment.getPraiseNumber());
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
