package com.kzsrm.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kzsrm.model.DailyAct;
import com.kzsrm.model.User;
import com.kzsrm.service.DailyActService;
import com.kzsrm.service.UserService;
import com.kzsrm.utils.MapResult;

@Controller
@RequestMapping("/dailyact")
public class DailyActController {

	private static Logger logger = LoggerFactory
			.getLogger(DailyActController.class);

	@Resource
	private DailyActService dailyService;
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
			if(lastSignIn!=null){
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				jsonObj.put("lastSignIn", formatter.format(lastSignIn));
			}
			jsonObj.put("signInDays", userService.selectUser(userId).getLearntime());
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
			Map<String, Object> ret = MapResult.initMap();
			// 最近一次签到
			DailyAct act = dailyService.getLastestByUser(userId);
			// 更改连续签到时间
			if(act!=null){
				User user = userService.selectUser(userId);
				int continueDays = Integer.parseInt(user.getLearntime()); //连续签到
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date date = formatter.parse(act.getDate());
				int compareTo = date.compareTo(new Date());
				if(compareTo==-1){
					continueDays++;
				}
				user.setLearntime(continueDays+""); //连续签到
				user.setLogintime(new Date()); // 签到日期
				userService.updateUser(user);
			}
			if(dailyService.getCheckInStatus(userId)==0){
				DailyAct dailyAct = new DailyAct();
				dailyAct.setUserId(userId);
				dailyAct.setCheckin(1);
				SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
				dailyAct.setDate(form.format(new Date()));
				dailyService.signIn(dailyAct);
			}
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
}
