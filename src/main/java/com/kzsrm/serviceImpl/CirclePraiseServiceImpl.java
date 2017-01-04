package com.kzsrm.serviceImpl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.CirclePraiseDao;
import com.kzsrm.model.CirclePraise;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.CirclePraiseService;

@Service
@Transactional
public class CirclePraiseServiceImpl extends
		BaseServiceMybatisImpl<CirclePraise, String> implements
		CirclePraiseService {
	@Resource
	private CirclePraiseDao<?> praiseDao;

	@Override
	protected EntityDao<CirclePraise, String> getEntityDao() {
		return praiseDao;
	}

	@Override
	public void praiseCircle(String userId, String  circleId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("circleId", circleId);
		List<CirclePraise> praiseList = praiseDao.getListByParams(map);
		if(praiseList.size()>0){
			CirclePraise praise = praiseList.get(0);
			praiseDao.deleteById(praise.getId()+"");
		}else{
			CirclePraise praise = new CirclePraise();
			praise.setCircleId(circleId);
			praise.setIsRead(0);
			praise.setUserId(userId);
			praise.setCreateTime(new Date());
			praiseDao.save(praise);
		}
	}
	
	@Override
	public Integer queryPraiseNumber(String  circleId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("circleId", circleId);
		List<CirclePraise> praiseList = praiseDao.getListByParams(map);
		return praiseList.size();
	}
	
	@Override
	public boolean queryIsPraised(String userId,String circleId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("circleId", circleId);
		map.put("userId", userId);
		List<CirclePraise> praiseList = praiseDao.getListByParams(map);
		return praiseList.size()>0?true:false;
	}
	
	/**
	 * 点赞列表
	 * @param praiseId 最后一条点赞Id
	 * @author tianxiaopeng
	 * @return
	 */
	public List<CirclePraise> getPraiseList(String userId,String praiseId,int pageSize){
		Map<String, Object> map = new HashMap<String, Object>();
		if(!StringUtils.isEmpty(praiseId)){
			CirclePraise praise = praiseDao.getById(praiseId);
			if(praise!=null){
				map.put("createTime", praise.getCreateTime());
			}else{
				map.put("createTime", new Date());
			}
		}else{
			map.put("createTime", new Date());
		}
		map.put("userId", userId);
		map.put("pageSize", pageSize);
		return praiseDao.getPraiseList(map);
	}
	
	/**
	 * 未读点赞数
	 * 
	 * @param userId
	 * @author tianxiaopeng
	 * @return
	 */
	public Integer getPraiseCount(int userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		return praiseDao.getPraiseCount(map);
	}
	
	/**
	 * 更新自己所发帖的点赞的阅读状态
	 * @author tianxiaopeng
	 * @return
	 */
	public void updateReadState(String userId,String circleId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("circleId", circleId);
		praiseDao.updatePraiseState(map);
	}
}
