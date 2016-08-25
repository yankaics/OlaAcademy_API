package com.kzsrm.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.MessageRecord;
import com.kzsrm.mybatis.BaseMybatisDao;

@Repository
public class MessageRecordDao<E> extends BaseMybatisDao<MessageRecord, String> {

	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.MessageRecordMapper";
	}
	
	public MessageRecord getByUserIdAndMessageId(int userId, String messageId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("messageId", messageId);
		return this.getSqlSession().selectOne(this.getMybatisMapperNamesapce() + ".getByUserIdAndMessageId", map);
	}

}
