package com.kzsrm.serviceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.util.TextUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.CoinHistoryDao;
import com.kzsrm.model.CoinHistory;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.CoinHistoryService;

@Service
@Transactional
public class CoinHistoryServiceImpl extends
		BaseServiceMybatisImpl<CoinHistory, String> implements CoinHistoryService {
	@Resource
	private CoinHistoryDao<?> dailyDao;

	@Override
	protected EntityDao<CoinHistory, String> getEntityDao() {
		return dailyDao;
	}

	@Override
	public int getCheckInStatus(int userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		map.put("date", sdf.format(new Date()));
		map.put("type","1");
		List<CoinHistory> historyList = dailyDao.getActByPramas(map);
		if (historyList.size() > 0){
			return 1;
		}
		else{
			return 0;
		}
	}
	
	// 每日分享次数
	@Override
	public int getDailyShareCount(int userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		map.put("date", sdf.format(new Date()));
		map.put("type","2");
		List<CoinHistory> historyList = dailyDao.getActByPramas(map);
		return historyList.size();
	}
	
	// 欧拉币明细记录
	@Override
	public List<CoinHistory> getCoinHistoryList(int userId,String date) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		if(!TextUtils.isEmpty(date)){
			map.put("date", date);
		}
		return dailyDao.getActByPramas(map);
	}
	
	/*
	 * 是否第一次购买
	 * @param type 4 购买会员 5 购买课程 8 完善资料
	 */
	@Override
	public int validateFirstPay(int userId,int type) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("type",type);
		List<CoinHistory> historyList = dailyDao.getActByPramas(map);;
		if (historyList.size() == 0){
			return 1; //第一次购买
		}
		else{
			return 0;
		}
	}
	
	@Override
	public CoinHistory getLastestByUser(int userId) {
		return dailyDao.getLastestByUser(userId);
	}

	@Override
	public void insertData(CoinHistory history) {
		dailyDao.insertData(history);
	}

}
