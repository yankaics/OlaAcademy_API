package com.kzsrm.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kzsrm.model.CoinHistory;
import com.kzsrm.model.User;
import com.kzsrm.service.CoinHistoryService;
import com.kzsrm.service.UserService;
import com.kzsrm.utils.MapResult;

@Controller
@RequestMapping("/dailyact")
public class DailyActController {

	private static Logger logger = LoggerFactory
			.getLogger(DailyActController.class);

	@Resource
	private CoinHistoryService dailyService;
	@Resource
	private UserService userService;

	/**
	 * 获取当天签到状态
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getCheckinStatus")
	public Map<String, Object> getCheckinStatus(
			@RequestParam(required = true) int userId) {
		try {
			Map<String, Object> ret = MapResult.initMap();
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("status", dailyService.getCheckInStatus(userId));
			Date lastSignIn = userService.selectUser(userId).getLogintime();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			if(lastSignIn!=null){
				jsonObj.put("lastSignIn", formatter.format(lastSignIn));
			}
			User u = userService.selectUser(userId);
			jsonObj.put("signInDays", u.getLearntime());
			jsonObj.put("coin", u.getCoin());
			jsonObj.put("profileTask", dailyService.validateFirstPay(userId, 8)==1?0:1);
			jsonObj.put("vipTask", dailyService.validateFirstPay(userId, 4)==1?0:1);
			jsonObj.put("courseTask", dailyService.validateFirstPay(userId, 5)==1?0:1);
			List<CoinHistory> historyList = dailyService.getCoinHistoryList(userId,formatter.format(new Date()));
			int todayCoin=0;
			for(CoinHistory history : historyList){
				if(history.getDealNum()>0)
					todayCoin+=history.getDealNum();
			}
			jsonObj.put("todayCoin", todayCoin);
			ret.put("result", jsonObj);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	/**
	 * 签到
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkin")
	public Map<String, Object> checkin(
			@RequestParam(required = true) int userId) {
		try {
			// 是否已签到
			int status = dailyService.getCheckInStatus(userId);
			if(status==1){
				return MapResult.initMap(10001, "已签到");
			}
			Map<String, Object> ret = MapResult.initMap();
			// 最近一次签到
			CoinHistory act = dailyService.getLastestByUser(userId);
			// 更改连续签到时间
			if(act!=null){
				User user = userService.selectUser(userId);
				int continueDays = Integer.parseInt(user.getLearntime()); //连续签到
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date date = formatter.parse(act.getDate());
				long interval = new Date().getTime() - date.getTime();
				if(interval>86400000&&interval<=172800000){
					continueDays++;
				}else if(interval>172800000){
					continueDays=1;
				}
				user.setLearntime(continueDays+""); //连续签到
				user.setLogintime(new Date()); // 签到日期
				userService.updateUser(user);
			}
			if(status==0){
				CoinHistory dailyAct = new CoinHistory();
				dailyAct.setUserId(userId);
				dailyAct.setType(1);
				dailyAct.setDealNum(5);
				dailyAct.setName("每日签到");
				SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
				dailyAct.setDate(form.format(new Date()));
				dailyService.insertData(dailyAct);
			}
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	/**
	 * 分享 (老版本 赠送2积分)
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/share")
	public Map<String, Object> share(
			@RequestParam(required = true) int userId) {
		try {
			// 分享次数
			int shareNum = dailyService.getDailyShareCount(userId);
			if(shareNum>=5){
				return MapResult.initMap(10001, "每日分享最多赠送10欧拉币");
			}
			Map<String, Object> ret = MapResult.initMap();
			// 欧拉币明细
			CoinHistory dailyAct = new CoinHistory();
			dailyAct.setUserId(userId);
			dailyAct.setType(2);
			dailyAct.setDealNum(2);
			dailyAct.setName("每日分享");
			SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
			dailyAct.setDate(form.format(new Date()));
			dailyService.insertData(dailyAct);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	/**
	 * 分享 (老版本 赠送2积分)
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/dailyShare")
	public Map<String, Object> dailyShare(
			@RequestParam(required = true) int userId) {
		try {
			// 分享次数
			int shareNum = dailyService.getDailyShareCount(userId);
			if(shareNum>=2){
				return MapResult.initMap(10001, "每日分享最多赠送10欧拉币");
			}
			Map<String, Object> ret = MapResult.initMap();
			// 欧拉币明细
			CoinHistory dailyAct = new CoinHistory();
			dailyAct.setUserId(userId);
			dailyAct.setType(2);
			dailyAct.setDealNum(5);
			dailyAct.setName("每日分享");
			SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
			dailyAct.setDate(form.format(new Date()));
			dailyService.insertData(dailyAct);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
}
