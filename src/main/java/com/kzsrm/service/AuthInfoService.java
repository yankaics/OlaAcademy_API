package com.kzsrm.service;

import com.kzsrm.baseservice.BaseServiceMybatis;
import com.kzsrm.model.AuthInfo;

public interface AuthInfoService extends BaseServiceMybatis<AuthInfo, String> {
	
	/**
	 * 获取认证信息
	 */
	AuthInfo queryAuthInfo(String userId);

	/**
	 * 提交认证信息
	 */
	void submitAuthInfo(String userId, String name, String phone, String email,String profile);
	
	/**
	 * 取消认证
	 */
	void cancelAuth(String userId);
}
