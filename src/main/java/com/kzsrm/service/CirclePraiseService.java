package com.kzsrm.service;

import java.util.List;

import com.kzsrm.baseservice.BaseServiceMybatis;
import com.kzsrm.model.CirclePraise;

public interface CirclePraiseService extends BaseServiceMybatis<CirclePraise, String> {

	/**
	 * 点赞
	 */
	void praiseCircle(String userId, String  circleId);
	
	// 某个帖子的点赞数
	Integer queryPraiseNumber(String  circleId);
	
	//是否点赞
	boolean queryIsPraised(String userId,String circleId);
	
	/**
	 * 点赞列表
	 * @param praiseId 最后一条点赞Id
	 * @author tianxiaopeng
	 * @return
	 */
	List<CirclePraise> getPraiseList(String userId,String praiseId,int pageSize);
	
	/**
	 * 未读点赞
	 * 
	 * @param userId
	 * @author tianxiaopeng
	 * @return
	 */
	Integer getPraiseCount(int userId);
	
	/**
	 * 更新自己所发帖的点赞的阅读状态
	 * @author tianxiaopeng
	 * @return
	 */
	void updateReadState(String userId,String circleId);

}
