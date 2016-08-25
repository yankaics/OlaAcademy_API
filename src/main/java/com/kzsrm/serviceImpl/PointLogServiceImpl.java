package com.kzsrm.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.PointLogDao;
import com.kzsrm.model.PointLog;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.PointLogService;

@Service
@Transactional
public class PointLogServiceImpl extends BaseServiceMybatisImpl<PointLog, String> implements PointLogService {
	@Resource
	private PointLogDao<?> pointLogDao;

	@Override
	protected EntityDao<PointLog, String> getEntityDao() {
		return pointLogDao;
	}
	/**
	 * 判断当前知识点用户是否已学
	 * @param pid		知识点id
	 * @param userId	用户
	 * @return
	 */
	@Override
	public String checkIsLearn(String pid, String userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pid", pid);
		map.put("userId", userId);
		List<PointLog> ret = pointLogDao.getLearned(map);
		if (ret.size() > 0 )
			return "1";
		return "0";
	}
	/**
	 * 获取当前用户某个知识点的正确率
	 * @param pid		知识点id
	 * @param userId	用户
	 * @return
	 */
	@Override
	public Double getAccuracy(String pid, String userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pid", pid);
		map.put("userId", userId);
		List<PointLog> ret = pointLogDao.getLearned(map);
		if (ret.size() > 0 )
			return ret.get(0).getAccuracy();
		return 0D;
	}

}
