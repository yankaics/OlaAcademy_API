package com.kzsrm.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kzsrm.service.CoinHistoryService;
import com.kzsrm.utils.MapResult;

@Controller
@RequestMapping("/coin")
public class CoinHistoryController {

	private static Logger logger = LoggerFactory
			.getLogger(CoinHistoryController.class);

	@Resource
	private CoinHistoryService coinHistoryService;

	/**
	 * 欧拉币明细
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getHistoryList")
	public Map<String, Object> getHistoryList(
			@RequestParam(required = true) int userId) {
		try {
			Map<String, Object> ret = MapResult.initMap();
			ret.put("result", coinHistoryService.getCoinHistoryList(userId,""));
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.initMap(9999, "服务器异常");
		}
	}
}
