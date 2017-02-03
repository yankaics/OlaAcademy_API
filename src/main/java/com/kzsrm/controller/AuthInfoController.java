package com.kzsrm.controller;

import java.text.SimpleDateFormat;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kzsrm.model.AuthInfo;
import com.kzsrm.service.AuthInfoService;
import com.kzsrm.utils.ComUtils;
import com.kzsrm.utils.MapResult;

@Controller
@RequestMapping("/auth")
public class AuthInfoController {
	
	JsonConfig authJC = ComUtils.jsonConfig(new String[]{"createTime"});

	private static Logger logger = LoggerFactory
			.getLogger(AuthInfoController.class);

	@Resource
	private AuthInfoService authService;
	
	/**
	 * 获取认证信息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryAuthInfo")
	public Map<String, Object> queryAuthInfo(
			@RequestParam(required = true) String userId) {
		try {
			Map<String, Object> ret = MapResult.initMap();
			AuthInfo authInfo = authService.queryAuthInfo(userId);
			if(authInfo==null){
				ret.put("authInfo", new AuthInfo());
			}else{
				JSONObject jsonObj = JSONObject.fromObject(authInfo, authJC);
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				jsonObj.put("createTime", sdf.format(authInfo.getCreateTime()));
				ret.put("authInfo", jsonObj);
			}
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}

	/**
	 * 认证老师
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/submitAuthInfo")
	public Map<String, Object> submitAuthInfo(
			@RequestParam(required = true) String userId,@RequestParam(required = true) String name,
			@RequestParam(required = true) String phone,@RequestParam(required = true) String email,
			@RequestParam(required = true) String profile) {
		try {
			Map<String, Object> ret = MapResult.initMap();
			authService.submitAuthInfo(userId, name, phone, email, profile);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	/**
	 * 取消认证申请
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/cancelAuth")
	public Map<String, Object> cancelAuth(
			@RequestParam(required = true) String userId) {
		try {
			Map<String, Object> ret = MapResult.initMap();
			authService.cancelAuth(userId);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
}
