package com.kzsrm.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JsonConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kzsrm.model.CoinHistory;
import com.kzsrm.model.Exchange;
import com.kzsrm.model.User;
import com.kzsrm.service.CoinHistoryService;
import com.kzsrm.service.ExchangeService;
import com.kzsrm.service.UserService;
import com.kzsrm.utils.ComUtils;
import com.kzsrm.utils.MapResult;


@Controller
@RequestMapping("/exchange")
public class ExchangeController {

	JsonConfig jsonConf = ComUtils.jsonConfig(new String[] { "createTime" });

	private static Logger logger = LoggerFactory
			.getLogger(BroadcastController.class);

	@Resource
	private UserService userService;
	@Resource
	private ExchangeService exchangeService;
	@Resource
	private CoinHistoryService coinHistoryService;
	
	/**
	 * 欧拉币兑换
	 * type 1 课程题目 2 模拟题 
	 */
	@ResponseBody
	@RequestMapping(value = "/unlockSubject")
	public Map<String, Object> unlockSubject(@RequestParam(required = true) int userId,
			@RequestParam(required = true) int objectId,@RequestParam(required = true) String type) {
		try {
			User user = userService.selectUser(userId);
			int coin = Integer.parseInt(user.getCoin());
			if(coin<20){
				return MapResult.initMap(10001, "积分不足");
			}else{
				// 兑换试题
				Exchange exchange = new Exchange();
				exchange.setObjectId(objectId);
				exchange.setUserId(userId);
				exchange.setType(type);
				exchange.setCreateTime(new Date());
				exchangeService.insertData(exchange);
				// 欧拉币使用明细
				updateCoinHistory("解锁题目",user.getId(),6,-20);
				return MapResult.initMap();
			}
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	// 更新欧拉币使用明细
	private void updateCoinHistory(String name,int userId,int type,int dealNum){
		CoinHistory dailyAct = new CoinHistory();
		dailyAct.setUserId(userId);
		dailyAct.setType(type);
		dailyAct.setDealNum(dealNum);
		dailyAct.setName(name);
		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
		dailyAct.setDate(form.format(new Date()));
		coinHistoryService.insertData(dailyAct);
	}

}
