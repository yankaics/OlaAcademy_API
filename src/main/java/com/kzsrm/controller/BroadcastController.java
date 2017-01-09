package com.kzsrm.controller;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kzsrm.model.Broadcast;
import com.kzsrm.model.User;
import com.kzsrm.service.BroadcastService;
import com.kzsrm.service.TestService;
import com.kzsrm.service.UserService;
import com.kzsrm.utils.ComUtils;
import com.kzsrm.utils.MapResult;

@Controller
@RequestMapping("/broadcast")
public class BroadcastController {
	
	JsonConfig broadcastJC = ComUtils.jsonConfig(new String[]{"liveTime"});

	private static Logger logger = LoggerFactory
			.getLogger(BroadcastController.class);

	@Resource
	private UserService userService;
	@Resource
	private BroadcastService broadcastService;

	/**
	 * 直播列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getBroadcastList")
	public Map<String, Object> getBroadcastList(
			@RequestParam(required = false) String broadcastId,
			@RequestParam(defaultValue = "20") int pageSize) {
		try {
			Map<String, Object> ret = MapResult.initMap();
			List<Broadcast> broadcastList = broadcastService.getBroadcastList(
					broadcastId, pageSize);
			JSONArray liveList = new JSONArray();
			for (Broadcast broadcast : broadcastList) {
				User user = userService.selectUser(broadcast.getUserId());
				JSONObject jsonObj = JSONObject.fromObject(broadcast, broadcastJC);
				jsonObj.put("userName", user.getName());
				jsonObj.put("userAvatar", user.getAvator());
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				jsonObj.put("time", sdf.format(broadcast.getLiveTime()));
				liveList.add(jsonObj);
			}
			ret.put("result", liveList);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	@Autowired  
    private TestService redisTestService;

	@ResponseBody
	@RequestMapping(value = "/getTimestampTest") 
    public void getTimestampTest() throws InterruptedException{  
        System.out.println("第一次调用：" + redisTestService.getTimestamp("param"));
        Thread.sleep(2000);
        System.out.println("2秒之后调用：" + redisTestService.getTimestamp("param"));
        Thread.sleep(11000);
        System.out.println("再过11秒之后调用：" + redisTestService.getTimestamp("param"));
    } 

}
