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
import com.kzsrm.dao.VideoLogDao;
import com.kzsrm.model.VideoLog;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.VideoLogService;

@Service
@Transactional
public class VideoLogServiceImpl extends BaseServiceMybatisImpl<VideoLog, String> implements VideoLogService {
	@Resource
	private VideoLogDao<?> videoLogDao;

	@Override
	protected EntityDao<VideoLog, String> getEntityDao() {
		return videoLogDao;
	}

	/**
	 * 记录视频观看记录
	 * @param userId
	 * @param videoId
	 * @param timeSpan
	 */
	@Override
	public void setVideoLog(String userId, String videoId, Integer timeSpan) {
		Date now = new Date();
		VideoLog vlog = getVideoLog(userId, videoId);
		if (vlog == null) {
			vlog = new VideoLog();
			vlog.setCreatetime(now);
			vlog.setTimespan(timeSpan);
			vlog.setUserid(userId);
			vlog.setVid(videoId);
			save(vlog);
		} else {
			vlog.setCreatetime(now);
			vlog.setTimespan(timeSpan);
			update(vlog);
		}
	}
	/**
	 * 获取视频观看记录
	 * @param userId
	 * @param videoId
	 * @return
	 */
	@Override
	public VideoLog getVideoLog(String userId, String videoId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("videoId", videoId);
		return videoLogDao.getVideoLog(map);
	}
	
	/**
	 * 获取视频观看记录
	 * @return
	 */
	@Override
	public List<VideoLog> getVideoList(String logId, int pageCount){
		Map<String, Object> map = new HashMap<String, Object>();
		if(!StringUtils.isEmpty(logId)){
			VideoLog videoLog = videoLogDao.getById(logId);
			if(videoLog!=null){
				map.put("createTime", videoLog.getCreatetime());
			}else{
				map.put("createTime", new Date());
			}
		}else{
			map.put("createTime", new Date());
		}
		
		map.put("pageCount", pageCount);
		return videoLogDao.getVideoList(map);
	}
	
}
