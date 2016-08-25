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

}
