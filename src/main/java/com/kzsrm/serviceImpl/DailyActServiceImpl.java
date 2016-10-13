package com.kzsrm.serviceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.DailyActDao;
import com.kzsrm.model.DailyAct;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.DailyActService;

@Service
@Transactional
public class DailyActServiceImpl extends
		BaseServiceMybatisImpl<DailyAct, String> implements DailyActService {
	@Resource
	private DailyActDao<?> dailyDao;

	@Override
	protected EntityDao<DailyAct, String> getEntityDao() {
		return dailyDao;
	}

	@Override
	public int getCheckInStatus(int userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		map.put("date", sdf.format(new Date()));
		DailyAct dailyAct = dailyDao.getActByPramas(map);
		if (dailyAct != null){
			return dailyAct.getCheckin();
		}
		else{
			return 0;
		}
	}
	
	@Override
	public DailyAct getLastestByUser(int userId) {
		return dailyDao.getLastestByUser(userId);
	}

	@Override
	public void signIn(DailyAct dailyAct) {
		dailyDao.signIn(dailyAct);
	}

}
