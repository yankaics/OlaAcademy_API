package com.kzsrm.service;

import java.util.List;

import com.kzsrm.baseservice.BaseServiceMybatis;
import com.kzsrm.model.Comment;

public interface CommentService  extends BaseServiceMybatis<Comment, String> {
	
	/**
	 * 添加评论
	 */
	 void addComment(Comment comment);
	/**
	 * 评论列表
	 * @param postId 课程或帖子Id
	 * @param type 1 课程Id 2 帖子Id
	 * @author tianxiaopeng
	 * @return
	 */
	List<Comment> getCommentList(int postId, int type);

}
