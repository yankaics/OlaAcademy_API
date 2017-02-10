package com.kzsrm.service;

import java.util.List;

import com.kzsrm.baseservice.BaseServiceMybatis;
import com.kzsrm.model.Comment;

public interface CommentService extends BaseServiceMybatis<Comment, String> {

	/**
	 * 添加评论
	 */
	void addComment(Comment comment);

	/**
	 * 评论列表
	 * 
	 * @param postId
	 *            课程或帖子Id
	 * @param type
	 *            1 课程Id 2 帖子Id
	 * @param userId
	 *            指定回答用户的Id
	 * @author tianxiaopeng
	 * @return
	 */
	List<Comment> getCommentList(int postId, int type, String userId);

	/**
	 * 消息列表
	 * 
	 * @param postId
	 *            课程或帖子Id
	 * @param type
	 *            1 课程Id 2 帖子Id
	 * @author tianxiaopeng
	 * @return
	 */
	List<Comment> getMessageList(int userId, int type, String commentId,
			int pageSize);

	/**
	 * 未读消息数
	 * 
	 * @return
	 */
	Integer getUnreadMessageCount(int userId, int type);

	/**
	 * 更新自己所发帖的阅读状态
	 * 
	 * @param postId
	 *            课程或帖子Id
	 * @param userId
	 *            当前用户ID
	 * @author tianxiaopeng
	 * @return
	 */
	void updateReadState(String userId, String postId);

	/**
	 * 更新他人所发帖且@自己的阅读状态
	 * 
	 * @param postId
	 *            课程或帖子Id
	 * @param userId
	 *            当前用户ID
	 * @author tianxiaopeng
	 * @return
	 */
	void updateOtherReadState(String userId, String postId);

	// 该帖中针对某人的回帖数
	Integer getSubCommentCount(String userId, String postId);

}
