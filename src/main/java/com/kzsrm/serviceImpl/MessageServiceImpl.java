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
import com.kzsrm.dao.MessageDao;
import com.kzsrm.model.Message;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.MessageService;

@Service
@Transactional
public class MessageServiceImpl extends BaseServiceMybatisImpl<Message, String>
		implements MessageService {
	@Resource
	private MessageDao<?> messageDao;

	@Override
	protected EntityDao<Message, String> getEntityDao() {
		return messageDao;
	}

	/**
	 * 消息列表
	 * 
	 * @param userId
	 * @author tianxiaopeng
	 * @return
	 */
	public List<Message> getMessageList(int userId,String messageId, int pageCount) {
			Map<String, Object> map = new HashMap<String, Object>();
			if(!StringUtils.isEmpty(messageId)){
				Message message = messageDao.getById(messageId);
				if(message!=null){
					map.put("createTime", message.getCreateTime());
				}else{
					map.put("createTime", new Date());
				}
			}else{
				map.put("createTime", new Date());
			}
			map.put("userId", userId);
			map.put("pageCount", pageCount);
		return messageDao.getMessageList(map);
	}
	
	/**
	 * 未读消息列表
	 * 
	 * @param userId
	 * @author tianxiaopeng
	 * @return
	 */
	public List<Message> getUnreadMessageList(int userId) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", userId);
		return messageDao.getUnreadMessageList(map);
	}
}
