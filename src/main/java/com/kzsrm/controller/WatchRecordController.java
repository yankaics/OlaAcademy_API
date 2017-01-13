package com.kzsrm.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kzsrm.service.WatchRecordService;
import com.kzsrm.utils.MapResult;

@Controller
@RequestMapping("/watchrecord")
public class WatchRecordController {

	private static Logger logger = LoggerFactory
			.getLogger(WatchRecordController.class);

	@Resource
	private WatchRecordService recordService;

	/**
	 * 记录观看进度
	 * 
	 * @param currentIndex 当前播放视频的序号
	 * @param duration 当前播放视频的时长 秒
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/recordPlayProgress")
	public Map<String, Object> recordPlayProgress(
			@RequestParam(required = true) int userId,
			@RequestParam(required = true) String objectId,
			@RequestParam(required = true) int type,
			@RequestParam(required = true) int currentIndex,
			@RequestParam(required = true) String duration) {
		try {
			recordService.recordPlayProgress(userId,type,objectId,currentIndex,duration);
			return  MapResult.initMap();
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
}
