package com.kzsrm.service;

import com.kzsrm.baseservice.BaseServiceMybatis;
import com.kzsrm.model.WatchRecord;

public interface WatchRecordService extends BaseServiceMybatis<WatchRecord, String> {

	WatchRecord getUserWatchRecord(String userId, String type, String objectId);
	
	void recordPlayProgress(int userId, int type, String objectId,int currentIndex,String duration);

}
