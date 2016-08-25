package com.kzsrm.service;

import java.util.List;

import com.kzsrm.baseservice.BaseServiceMybatis;
import com.kzsrm.model.VideoLog;

public interface VideoLogService  extends BaseServiceMybatis<VideoLog, String> {
	/**
	 * 记录视频观看记录
	 * @param userId
	 * @param videoId
	 * @param timeSpan
	 */
	void setVideoLog(String userId, String videoId, Integer timeSpan);
	/**
	 * 获取视频观看记录
	 * @param userId
	 * @param videoId
	 * @return
	 */
	VideoLog getVideoLog(String userId, String videoId);
	
	/**
	 * 获取视频观看记录
	 * @return
	 */
	List<VideoLog> getVideoList(String logId, int pageCount);

}
