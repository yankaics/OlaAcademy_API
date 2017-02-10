package com.kzsrm.serviceImpl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.CommentDao;
import com.kzsrm.model.Comment;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.CommentService;

@Service
@Transactional
public class CommentServiceImpl extends BaseServiceMybatisImpl<Comment, String> implements CommentService {
	@Resource
	private CommentDao<?> commentDao;

	@Override
	protected EntityDao<Comment, String> getEntityDao() {
		return commentDao;
	}
	
	/**
	 * 添加评论
	 */
	@Override
	public void addComment(Comment comment) {
		commentDao.save(comment);
	}

	/**
	 * 评论列表
	 * @param postId 课程或帖子Id
	 * @param type 1 课程Id 2 帖子Id
	 * @param userId 指定回答用户的Id
	 * @author tianxiaopeng
	 * @return
	 */
	public List<Comment> getCommentList(int postId, int type,String userId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("postId", postId);
		map.put("type", type);
		map.put("userId", userId);
		return commentDao.getCommentList(map);
	}
	
	/**
	 * 消息列表
	 * @param postId 课程或帖子Id
	 * @param type 1 课程Id 2 帖子Id
	 * @author tianxiaopeng
	 * @return
	 */
	public List<Comment> getMessageList(int userId, int type,String commentId,int pageSize){
		Map<String, Object> map = new HashMap<String, Object>();
		if(!StringUtils.isEmpty(commentId)){
			Comment comment = commentDao.getById(commentId);
			if(comment!=null){
				map.put("createTime", comment.getCreateTime());
			}else{
				map.put("createTime", new Date());
			}
		}else{
			map.put("createTime", new Date());
		}
		map.put("userId", userId);
		map.put("type", type);
		map.put("pageSize", pageSize);
		return commentDao.getMessageList(map);
	}
	
	/**
	 * 未读消息数
	 * @return
	 */
	public Integer getUnreadMessageCount(int userId, int type){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("type", type);
		return commentDao.getUnreadMessageCount(map);
	}
	
	/**
	 * 更新自己所发帖的阅读状态
	 * @param postId 课程或帖子Id
	 * @param userId 当前用户ID
	 * @author tianxiaopeng
	 * @return
	 */
	public void updateReadState(String userId,String postId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("postId", postId);
		commentDao.updateReadState(map);
	}
	
	/**
	 * 更新他人所发帖且@自己的阅读状态
	 * @param postId 课程或帖子Id
	 * @param userId 当前用户ID
	 * @author tianxiaopeng
	 * @return
	 */
	public void updateOtherReadState(String userId,String postId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("postId", postId);
		commentDao.updateOtherReadState(map);
	}
	
	//该帖中针对某人的回帖数
	public Integer getSubCommentCount(String userId,String postId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("postId", postId);
		return commentDao.getSubCommentCount(map);
	}
}
