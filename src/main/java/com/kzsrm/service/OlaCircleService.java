package com.kzsrm.service;

import java.util.List;

import com.kzsrm.baseservice.BaseServiceMybatis;
import com.kzsrm.model.OlaCircle;

public interface OlaCircleService  extends BaseServiceMybatis<OlaCircle, String> {
	/**
	 * 添加观看记录或发帖
	 * @param userId
	 * @param videoId
	 * @param timeSpan
	 */
	void addOlaCircle(OlaCircle circle);
	
	/**
	 * 欧拉圈列表
	 * @return
	 */
	List<OlaCircle> getCircleList(String userId, String circleId, int pageCount,String type);
	
	/**
	 * 所发贴列表
	 * @return
	 */
	List<OlaCircle> getDeployPostList(String userId);
	
	/**
	 * 所回答帖子列表
	 * @return
	 */
	List<OlaCircle> getReplyPostList(String userId);
	
	/**
	 * 对帖子点赞
	 */
	void praiseCirclePost(OlaCircle circle);
	
	/**
	 * 浏览量
	 */
	void updateReadNumber(OlaCircle circle);

}

