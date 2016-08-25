package com.kzsrm.serviceImpl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.MessageRecordDao;
import com.kzsrm.model.MessageRecord;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.MessageRecordService;

@Service
@Transactional
public class MessageRecordServiceImpl extends
		BaseServiceMybatisImpl<MessageRecord, String> implements
		MessageRecordService {
	@Resource
	private MessageRecordDao<?> recordDao;

	@Override
	protected EntityDao<MessageRecord, String> getEntityDao() {
		return recordDao;
	}

	@Override
	public void addMessageRecord(int userId, String messageIds) {
		// TODO Auto-generated method stub
		String[] messageIdArray = messageIds.split(",");
		for(String messageId :messageIdArray){
			if(recordDao.getByUserIdAndMessageId(userId, messageId)==null){
				MessageRecord record = new MessageRecord();
				record.setUserId(userId);
				record.setMessageId(Integer.parseInt(messageId));
				recordDao.save(record);
			}
			
		}
	}
}