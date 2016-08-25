package com.kzsrm.serviceImpl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.OlaCircleDao;
import com.kzsrm.dao.OrderInfoDao;
import com.kzsrm.dao.VideoDao;
import com.kzsrm.dao.VideoLogDao;
import com.kzsrm.model.OlaCircle;
import com.kzsrm.model.OrderInfo;
import com.kzsrm.model.Video;
import com.kzsrm.model.VideoLog;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.VideoService;
import com.kzsrm.utils.ComUtils;
import com.kzsrm.utils.CustomException;

@Service
@Transactional
public class VideoServiceImpl extends BaseServiceMybatisImpl<Video, String>
		implements VideoService {
	@Resource
	private VideoDao<?> videoDao;
	@Resource
	private OrderInfoDao<?> orderInfoDao;
	@Resource
	private VideoLogDao<?> videoLogDao;
	@Resource
	private OlaCircleDao<?> circleDao;

	@Override
	protected EntityDao<Video, String> getEntityDao() {
		return videoDao;
	}

	@Override
	public Video getVideoById(Integer id) {
		return videoDao.getById(id);
	}

	/**
	 * 获取知识点对应的视频
	 * 
	 * @param pointId
	 * @return
	 */
	@Override
	public Video getVideoByPoint(String pointId) {
		return videoDao.getVideoByPoint(pointId);
	}

	@Override
	public List<Video> getVideoListByPoint(String userId,String pointId) {
		List<Video> videoList = videoDao.getVideoListByPoint(pointId);
		//更新观看记录
		if(!StringUtils.isEmpty(userId)){
			//旧版本欧拉圈
			VideoLog vl = videoLogDao.getByUserIdAndCourseId(userId, pointId);
			if(vl==null){
				VideoLog videoLog = new VideoLog();
				videoLog.setCourseId(pointId);
				videoLog.setUserid(userId);
				videoLog.setCreatetime(new Date());
				if(videoList.size()>0){
					videoLog.setVid(videoList.get(0).getId()+"");;
				}
				
				videoLogDao.addVideoLog(videoLog);
				
			}else{
				vl.setCreatetime(new Date());
				videoLogDao.updateVideoLog(vl);
			}
			//新版本欧拉圈
			OlaCircle existCircle = circleDao.getByUserIdAndCourseId(userId, pointId);
			Date now = new Date();
			if (existCircle == null) {
				OlaCircle circle = new OlaCircle();
				circle.setUserId(Integer.parseInt(userId));
				circle.setCourseId(Integer.parseInt(pointId));
				circle.setType(1);
				circle.setCreateTime(new Date());
				if(videoList.size()>0){
					circle.setVideoId(videoList.get(0).getId());;
				}
				circleDao.save(circle);
			} else {
				existCircle.setCreateTime(now);
				circleDao.update(existCircle);
			}
		}
		return videoList;
	}

	/**
	 * 根据标签获取视频（模糊检索）
	 * 
	 * @param pointId
	 * @return
	 */
	@Override
	public List<Video> getVideoByTag(String keyword) {
		return videoDao.getVideoByTag(keyword);
	}

	/**
	 * 更新视频信息
	 * 
	 * @param videoId
	 * @param timeSpan
	 * @throws CustomException
	 */
	@Override
	public void updateVideoInfo(String videoId, String timeSpan)
			throws CustomException {
		int _timeSpan = ComUtils.parseInt(timeSpan);

		Video video = videoDao.getById(videoId);
		if (video == null)
			throw new CustomException("视频不存在");

		video.setPlayCount(video.getPlayCount() + 1);
		video.setTimeSpan(video.getTimeSpan() + _timeSpan);
		videoDao.update(video);
	}

	/**
	 * 获取试题相关的视频
	 * 
	 * @param subjectId
	 * @return
	 */
	@Override
	public List<Video> getVideoBySubject(String subjectId) {
		return videoDao.getVideoBySubject(subjectId);
	}

	/**
	 * 获取推荐视频
	 * 
	 * @param subjectIds
	 * @return
	 */
	@Override
	public Video getRecommendVideo(String subjectIds) {
		return null;/*
					 * Map<Integer, Integer> repeat = new HashMap<Integer,
					 * Integer>(); Integer maxCount = 0, maxPoint = null;
					 * 
					 * for (String sid : subjectIds.split(",")) { List<Point>
					 * plist = pointDao.getPoisBySub(sid); for (Point point :
					 * plist) { Integer pid = point.getId(); if
					 * (repeat.containsKey(pid)) { Integer count =
					 * repeat.get(pid) + 1; repeat.put(pid, count); if (count >
					 * maxCount){ maxCount = count; maxPoint = pid; } } else {
					 * repeat.put(pid, 1); if (maxCount == 0) { maxCount = 1;
					 * maxPoint = pid; } } } }
					 * 
					 * return videoDao.getVideoByPoint(maxPoint+"");
					 */
	}

	/**
	 * 获取视频集下的视频
	 * 
	 * @param gid
	 * @return
	 */
	@Override
	public List<Video> getVideosByGoods(String userId, String gid) {
		List<Video> videoList = videoDao.getVideosByGoods(gid);
		if(userId!=null&&!userId.equals("")){
			OrderInfo orderInfo = orderInfoDao.getOrderInfo(Integer.parseInt(userId), gid);
			if (orderInfo != null && orderInfo.getStatus() > 0) {
				for (int i = 0; i < videoList.size(); i++) {
					Video video = videoList.get(i);
					video.setIsfree(1);
					videoList.set(i, video);
				}
			}
		}
		return videoList;
	}

}
