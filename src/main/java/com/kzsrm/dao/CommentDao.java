package com.kzsrm.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.Comment;
import com.kzsrm.mybatis.BaseMybatisDao;

@Repository
public class CommentDao<E> extends BaseMybatisDao<Comment, String> {

	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.CommentMapper";
	}

	public List<Comment> getCommentList(Map<String, Object> map) {
		return this.getSqlSession().selectList(
				this.getMybatisMapperNamesapce() + ".getCommentList", map);
	}
	
	public List<Comment> getMessageList(Map<String, Object> map) {
		return this.getSqlSession().selectList(
				this.getMybatisMapperNamesapce() + ".getMessageList", map);
	}
	
	// 未读消息数
	public Integer getUnreadMessageCount(Map<String, Object> map) {
		return this.getSqlSession().selectOne(this.getMybatisMapperNamesapce() + ".getUnreadMessageCount", map);
	}

	//更新自己所发贴的已读状态
	public void updateReadState(Map<String, Object> map) {
		this.getSqlSession().update(
				this.getMybatisMapperNamesapce() + ".updateReadState", map);
	}
	//更新他人所发贴且@自己的已读状态
	public void updateOtherReadState(Map<String, Object> map) {
		this.getSqlSession().update(
				this.getMybatisMapperNamesapce() + ".updateOtherReadState", map);
	}
	//该帖中针对某人的回帖数
	public Integer getSubCommentCount(Map<String, Object> map) {
		return this.getSqlSession().selectOne(
				this.getMybatisMapperNamesapce() + ".getSubCommentCount", map);
	}
}
