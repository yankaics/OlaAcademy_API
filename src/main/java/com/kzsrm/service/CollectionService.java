package com.kzsrm.service;

import java.util.List;

import com.kzsrm.model.Collection;

public interface CollectionService {
	
	/**
	 * 收藏
	 * 
	 * @param collection
	 *            
	 * @return
	 */
	int insert(Collection collection);
	
	/**
	 * 查询用户收藏的视频
	 * 
	 * @param userId
	 *            id
	 * @return
	 */
	List<Collection> getByUserId(Integer userId);
	
	/**
	 * 查询视频收藏数
	 * 
	 * @param userId
	 *            id
	 * @return
	 */
	List<Collection> getByVideoId(Integer videoId);
	
	/**
	 * 查询用户收藏的视频
	 * 
	 * @param userId
	 *            id
	 *            type 1 课程库（course）视频  2 精品课（goods）视频
	 * @return
	 */
	Collection getByUserIdAndVideoId(Integer userId,Integer objectId,int type);
	
	/**
	 * 删除收藏
	 * 
	 * @param collection
	 *            
	 * @return
	 */
	int deleteByUserIdAndVideoId(Integer userId,Integer videoId);
	
	/**
	 * 删除该用户的所有收藏
	 * 
	 * @param collection
	 *            
	 * @return
	 */
	int deleteByUserId(Integer userId);

}
