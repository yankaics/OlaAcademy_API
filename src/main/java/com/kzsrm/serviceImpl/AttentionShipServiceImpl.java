package com.kzsrm.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.AttentionShipDao;
import com.kzsrm.model.AttentionShip;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.AttentionShipService;


@Service
@Transactional
public class AttentionShipServiceImpl extends
		BaseServiceMybatisImpl<AttentionShip, String> implements AttentionShipService {
	@Resource
	private AttentionShipDao<?> shipDao;

	@Override
	protected EntityDao<AttentionShip, String> getEntityDao() {
		return shipDao;
	}

	@Override
	public void insertData(AttentionShip ship) {
		shipDao.save(ship);
	}

	@Override
	public List<AttentionShip> getByParams(int attendId, int attendedId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("attendId", attendId);
		map.put("attendedId", attendedId);
		return shipDao.getListByPrama(map);
	}

	@Override
	public void delete(String shipId) {
		shipDao.deleteById(shipId);
	}
}