package com.kzsrm.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kzsrm.model.Goods;
import com.kzsrm.model.Video;
import com.kzsrm.service.GoodsService;
import com.kzsrm.service.OrderInfoService;
import com.kzsrm.service.VideoService;
import com.kzsrm.utils.ComUtils;
import com.kzsrm.utils.MapResult;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

@Controller
@RequestMapping("/goods")
public class GoodsController {
	private static Logger logger = LoggerFactory.getLogger(GoodsController.class);
	JsonConfig goodsCf = ComUtils.jsonConfig(new String[]{"status"});
	
	@Resource private GoodsService goodsService;
	@Resource private VideoService videoService;
	@Resource private OrderInfoService orderService;

	/**
	 * 商品列表
	 * @param type			1-名师教程，2-体系课程
	 * @param pageIndex		页数
	 * @param pageSize		页大小
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getGoodsList")
	public Map<String, Object> getGoodsList(
			@RequestParam(defaultValue="1") int pageIndex,
			@RequestParam(defaultValue="20") int pageSize,
			@RequestParam(required = true) String type) {
		try{
			Map<String, Object> ret = MapResult.initMap();
			List<Goods> goodsList = goodsService.getList(type, pageIndex, pageSize);
			
			ret.put("result", JSONArray.fromObject(goodsList, goodsCf));
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	/**
	 * 已购商品列表
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getBuyGoodsList")
	public Map<String, Object> getBuyGoodsList(
			@RequestParam(required = true) String userId) {
		try{
			Map<String, Object> ret = MapResult.initMap();
			List<Goods> goodsList = goodsService.getBuyList(Integer.parseInt(userId), 1);
			ret.put("result", JSONArray.fromObject(goodsList, goodsCf));
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	/**
	 * 商品订单购买状态
	 * @param pageSize		页大小
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getOrderStatus")
	public Map<String, Object> getOrderStatus(@RequestParam(required = true) String userId,
			@RequestParam(required = true) String gid) {
		try{
			Map<String, Object> ret = MapResult.initMap();
			ret.put("result", orderService.getOrderStatus(userId, gid));
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	/**
	 * 商品（视频集）下的视频列表
	 * @param pageSize		页大小
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getVideoList")
	public Map<String, Object> getVideoList(@RequestParam(required = false) String userId,
			@RequestParam(required = true) String gid) {
		try{
			Map<String, Object> ret = MapResult.initMap();
			List<Video> videoList = videoService.getVideosByGoods(userId, gid);
			
			ret.put("result", videoList);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
}
