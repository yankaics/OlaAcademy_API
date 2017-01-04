package com.kzsrm.serviceImpl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.OlaCircleDao;
import com.kzsrm.model.OlaCircle;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.OlaCircleService;

@Service
@Transactional
public class OlaCircleServiceImpl extends BaseServiceMybatisImpl<OlaCircle, String> implements OlaCircleService {
	@Resource
	private OlaCircleDao<?> circleDao;

	@Override
	protected EntityDao<OlaCircle, String> getEntityDao() {
		return circleDao;
	}

	/**
	 * 添加观看记录或发帖
	 * @param userId
	 * @param videoId
	 * @param timeSpan
	 */
	@Override
	public void addOlaCircle(OlaCircle circle) {
		circleDao.save(circle);
	}
	
	/**
	 * 欧拉圈列表
	 * @return
	 */
	@Override
	public List<OlaCircle> getCircleList(String userId, String circleId, int pageCount,String type){
		Map<String, Object> map = new HashMap<String, Object>();
		if(!StringUtils.isEmpty(circleId)){
			OlaCircle videoLog = circleDao.getById(circleId);
			if(videoLog!=null){
				map.put("createTime", videoLog.getCreateTime());
			}else{
				map.put("createTime", new Date());
			}
		}else{
			map.put("createTime", new Date());
		}
		
		map.put("pageCount", pageCount);
		if(!TextUtils.isEmpty(type)){
			map.put("type", type);
		}
		if(!TextUtils.isEmpty(userId)){
			map.put("userId", userId);
		}
		return circleDao.getVideoList(map);
	}
	
	/**
	 * 所发贴列表
	 * @return
	 */
	@Override
	public List<OlaCircle> getDeployPostList(String userId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", "2");
		map.put("userId", userId);
		return circleDao.getDeployPost(map);
	}
	
	/**
	 * 所回答帖子列表
	 * @return
	 */
	@Override
	public List<OlaCircle> getReplyPostList(String userId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		return circleDao.getReplyPost(map);
	}
	
	/**
	 * 对帖子点赞
	 */
	@Override
	public void praiseCirclePost(OlaCircle circle) {
		int praiseNumber = circle.getPraiseNumber()+1;
		circle.setPraiseNumber(praiseNumber);
		circleDao.update(circle);
	}
	
	/**
	 * 浏览量
	 */
	@Override
	public void updateReadNumber(OlaCircle circle) {
		int readNumber = circle.getReadNumber()+1;
		circle.setReadNumber(readNumber);
		circleDao.update(circle);
	}
	
}
