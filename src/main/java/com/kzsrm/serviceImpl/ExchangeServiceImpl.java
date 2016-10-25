package com.kzsrm.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.ExchangeDao;
import com.kzsrm.model.Exchange;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.ExchangeService;

@Service
@Transactional
public class ExchangeServiceImpl extends
		BaseServiceMybatisImpl<Exchange, String> implements ExchangeService {
	@Resource
	private ExchangeDao<?> exchangeDao;

	@Override
	protected EntityDao<Exchange, String> getEntityDao() {
		return exchangeDao;
	}

	/**
	 * 积分兑换
	 * 
	 * @param userId
	 * @param objectId
	 * @param createTime
	 */
	@Override
	public void insertData(Exchange exchange) {
		exchangeDao.save(exchange);
	}

	/**
	 * 是否已兑换
	 * 
	 * @return
	 */
	@Override
	public List<Exchange> getByParams(String userId, String objectId,String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("objectId", objectId);
		map.put("type", type);
		return exchangeDao.getByPrarms(map);
	}

}
