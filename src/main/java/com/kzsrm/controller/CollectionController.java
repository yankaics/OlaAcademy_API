package com.kzsrm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kzsrm.model.Collection;
import com.kzsrm.model.Course;
import com.kzsrm.model.Goods;
import com.kzsrm.model.Video;
import com.kzsrm.service.CollectionService;
import com.kzsrm.service.CourseService;
import com.kzsrm.service.GoodsService;
import com.kzsrm.service.VideoService;
import com.kzsrm.utils.ApiCode;
import com.kzsrm.utils.MapResult;

@Controller
@RequestMapping("/collection")
public class CollectionController {
	private static Logger logger = LoggerFactory.getLogger(Collection.class);
	
	@Resource private VideoService videoService;
	@Resource private CollectionService collectionService;
	@Resource private CourseService courseService;
	@Resource private GoodsService goodsService;
	
	/**
	 * 收藏
	 * 
	 * @param userId
	 *            userId
	 * @param videoId
	 *             视频 ID
	 * @param courseId
	 *            1 课程 ID
	 * @param type
	 *            1 课程库course 2 精品课goods
	 * @param state
	 *            0 删除 1 收藏
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/collectionVideo")
	public Map<String, Object> collectionVideo(@RequestParam(required = true) String userId,
			@RequestParam(required = true) String videoId,
			@RequestParam(required = true) String courseId,
			@RequestParam(defaultValue="1") int type,
			@RequestParam(required = true) String state) {
		try {
			if(Integer.parseInt(state)==1){
				Collection c = collectionService.getByUserIdAndVideoId(Integer.parseInt(userId), Integer.parseInt(courseId),type);
				if(c!=null){
					return MapResult.initMap();
				}
				Collection collection = new Collection();
				collection.setVideoId(Integer.parseInt(videoId));
				collection.setUserId(Integer.parseInt(userId));
				collection.setCourseId(Integer.parseInt(courseId));
				collection.setType(type);
				if(collectionService.insert(collection)==1){
					return MapResult.initMap();
				}else{
					return MapResult.initMap(-1, "数据存储错误");
				}
			}else{
				if(collectionService.deleteByUserIdAndVideoId(Integer.parseInt(userId), Integer.parseInt(videoId))==1){
					return MapResult.initMap();
				}else{
					return MapResult.initMap(-1, "删除错误");
				}
			}
			
			
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	/**
	 * 获取用户收藏的课程或视频
	 * @param userId	userId
	 * @return
	 */
	@RequestMapping(value = "/getCollectionByUserId")
	@ResponseBody
	public Map<String, Object> getCollectionByUserId(@RequestParam(required = true) String userId) {
		try{
			if (StringUtils.isBlank(userId))
				return MapResult.initMap(ApiCode.PARG_ERR, "用户id为空");
			
			Map<String, Object> ret = MapResult.initMap();
			List<Collection> collectionList = collectionService.getByUserId(Integer.parseInt(userId));
			JSONArray result = new JSONArray();
			for(Collection c:collectionList){
				Video video = videoService.getVideoById(c.getVideoId());
				if(c.getType()==1){
					Course course = courseService.getCourseById(c.getCourseId()+"");
					if(video!=null&&course!=null){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("videoId", video.getId());
						jsonObj.put("videoName", video.getName());
						jsonObj.put("videoPic", video.getPic());
						jsonObj.put("videoUrl", video.getAddress());
						jsonObj.put("courseId", course.getId());
						jsonObj.put("coursePic", course.getAddress());
						jsonObj.put("totalTime", course.getTotalTime());
						jsonObj.put("subAllNum", course.getSubAllNum());
						jsonObj.put("type", c.getType());
						result.add(jsonObj);
					}
				}else{
					Goods course = goodsService.getById(c.getCourseId()+"");
					if(video!=null&&course!=null){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("videoId", video.getId());
						jsonObj.put("videoName", video.getName());
						jsonObj.put("videoPic", video.getPic());
						jsonObj.put("videoUrl", video.getAddress());
						jsonObj.put("courseId", course.getId());
						jsonObj.put("coursePic", course.getUrl());
						jsonObj.put("totalTime", course.getTotaltime()+"分钟");
						jsonObj.put("subAllNum", course.getVideonum());
						jsonObj.put("type", c.getType());
						result.add(jsonObj);
					}
				}
			}
			ret.put("result", result);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	/**
	 * 用户对视频的收藏状态
	 * @param userId	userId
	 * @return
	 */
	@RequestMapping(value = "/getColletionState")
	@ResponseBody
	public Map<String, Object> getColletionState(@RequestParam(required = false) String userId,
			@RequestParam(required = true) String collectionId,@RequestParam(required = true) int type) {
		try{
			if (StringUtils.isBlank(collectionId))
				return MapResult.initMap(ApiCode.PARG_ERR, "视频id为空");
			
			Map<String, Object> ret = MapResult.initMap();
			int collectionState = 0;
			if (!StringUtils.isBlank(userId)){
				Collection collection = collectionService.getByUserIdAndVideoId(Integer.parseInt(userId), Integer.parseInt(collectionId),type);
				if(collection!=null)
					collectionState = 1;
			}
			List<Collection> collectionList = collectionService.getByVideoId(Integer.parseInt(collectionId));
			Map<String,Integer> result = new HashMap<String, Integer>();
			result.put("isCollect", collectionState);
			result.put("collectCount", collectionList.size());
			ret.put("result", result);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	/**
	 * 删除用户收藏的所有视频
	 * @param userId	userId
	 * @return
	 */
	@RequestMapping(value = "/removeColletionByUserId")
	@ResponseBody
	public Map<String, Object> removeColletionByUserId(@RequestParam(required = true) String userId) {
		try{
			if (StringUtils.isBlank(userId))
				return MapResult.initMap(ApiCode.PARG_ERR, "用户id为空");
			int result = collectionService.deleteByUserId(Integer.parseInt(userId));
			if(result>0){
				return MapResult.initMap(10000, "删除成功");
			}else{
				return MapResult.initMap(10001, "删除失败");
			}
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}


}
