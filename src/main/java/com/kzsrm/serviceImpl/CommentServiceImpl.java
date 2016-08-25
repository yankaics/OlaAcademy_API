package com.kzsrm.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
	 * @author tianxiaopeng
	 * @return
	 */
	public List<Comment> getCommentList(int postId, int type){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("postId", postId);
		map.put("type", type);
		return commentDao.getCommentList(map);
	}
}
