package com.kzsrm.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.Message;
import com.kzsrm.mybatis.BaseMybatisDao;

@Repository
public class MessageDao<E> extends BaseMybatisDao<Message, String> {

	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.MessageMapper";
	}

	public List<Message> getMessageList(Map<String, Object> map) {
		return this.getSqlSession().selectList(
				this.getMybatisMapperNamesapce() + ".getMessageList", map);
	}

	public List<Message> getUnreadMessageList(Map<String, Object> map) {
		return this.getSqlSession().selectList(
				this.getMybatisMapperNamesapce() + ".getUnreadMessageList", map);
	}
}
