package com.kzsrm.serviceImpl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.WatchRecordDao;
import com.kzsrm.model.WatchRecord;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.WatchRecordService;

@Service
@Transactional
public class WatchRecordServiceImpl extends
		BaseServiceMybatisImpl<WatchRecord, String> implements WatchRecordService {
	
	@Resource
	private WatchRecordDao<?> recordDao;

	@Override
	protected EntityDao<WatchRecord, String> getEntityDao() {
		return recordDao;
	}

	@Override
	public WatchRecord getUserWatchRecord(String userId, String type,
			String objectId) {
		List<WatchRecord> recordList = recordDao.getListByParams(userId, objectId, type);
		if(recordList.size()>0){
			return recordList.get(0);
		}
		return null;
	}

	@Override
	public void recordPlayProgress(int userId, int type, String objectId,
			int currentIndex,String duration) {
		List<WatchRecord> recordList = recordDao.getListByParams(userId+"", objectId, type+"");
		if(recordList.size()==0){ //插入
			WatchRecord record = new WatchRecord();
			record.setUserId(userId);
			record.setType(type);
			record.setCurrentTime(duration);
			record.setCurrentIndex(currentIndex);
			record.setObjectId(objectId);
			record.setCreateTime(new Date());
			recordDao.save(record);
		}else{ //更新
			WatchRecord record = recordList.get(0);
			record.setCurrentIndex(currentIndex);
			record.setCurrentTime(duration);
			record.setCreateTime(new Date());
			recordDao.update(record);
		}
	}
}
