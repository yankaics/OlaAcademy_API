package com.kzsrm.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.BroadcastDao;
import com.kzsrm.model.Broadcast;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.BroadcastService;

@Service
@Transactional
public class BroadcastServiceImpl extends
		BaseServiceMybatisImpl<Broadcast, String> implements BroadcastService {
	@Resource
	private BroadcastDao<?> broadcastDao;

	@Override
	protected EntityDao<Broadcast, String> getEntityDao() {
		return broadcastDao;
	}

	/**
	 * 直播列表
	 * 
	 * @return
	 */
	@Override
	public List<Broadcast> getBroadcastList(String broadcastId, int pageCount) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(broadcastId)) {
			Broadcast broadcast = broadcastDao.getById(broadcastId);
			if (broadcast != null) {
				map.put("liveTime", broadcast.getLiveTime());
			}
		}

		map.put("pageCount", pageCount);
		return broadcastDao.getBroadcastList(map);
	}

}
