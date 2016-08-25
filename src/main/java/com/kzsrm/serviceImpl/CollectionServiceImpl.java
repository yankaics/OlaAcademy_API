package com.kzsrm.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.CollectionDao;
import com.kzsrm.model.Collection;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.CollectionService;

@Service
@Transactional
public class CollectionServiceImpl extends BaseServiceMybatisImpl<Collection, String> implements CollectionService {
	@Resource
	private CollectionDao<?> colletionDao;

	@Override
	protected EntityDao<Collection, String> getEntityDao() {
		return colletionDao;
	}

	/**
	 * 添加收藏
	 * 
	 * @param collection
	 *            
	 * @return
	 */
	@Override
	public int insert(Collection collection){
		return colletionDao.insert(collection);
	}
	
	/**
	 * 删除收藏
	 * 
	 * @param collection
	 *            
	 * @return
	 */
	@Override
	public int deleteByUserIdAndVideoId(Integer userId,Integer videoId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("videoId", videoId);
		return colletionDao.deleteByUserIdAndVideoId(map);
	}
	
	/**
	 * 删除该用户的所有收藏
	 * 
	 * @param collection
	 *            
	 * @return
	 */
	@Override
	public int deleteByUserId(Integer userId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		return colletionDao.deleteByUserId(map);
	}
	
	/**
	 * 查询用户收藏的视频
	 * @param userId
	 * @return
	 */
	@Override
	public List<Collection> getByUserId(Integer userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		return colletionDao.getByUserId(map);
	}
	
	/**
	 * 查询视频收藏数
	 * @param userId
	 * @return
	 */
	@Override
	public List<Collection> getByVideoId(Integer videoId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("videoId", videoId);
		return colletionDao.getByVideoId(map);
	}
	
	/**
	 * 查询用户收藏的视频
	 * 
	 * @param userId
	 *            id
	 * @return
	 */
	@Override
	public Collection getByUserIdAndVideoId(Integer userId,Integer courseId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("courseId", courseId);
		return colletionDao.getByUserIdAndVideoId(map);
	}

}
