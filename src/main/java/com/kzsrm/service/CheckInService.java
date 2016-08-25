package com.kzsrm.service;

import java.util.List;

import com.kzsrm.baseservice.BaseServiceMybatis;
import com.kzsrm.model.CheckInfo;

public interface CheckInService extends BaseServiceMybatis<CheckInfo, String> {

	/**
	 * 向机构报名
	 * 
	 * @param checkInfo
	 * @return
	 */
	int checkIn(CheckInfo checkInfo);
	
	/**
	 * 查看已报到机构列表
	 * 
	 * @param orgId
	 * @return
	 */
	List<CheckInfo> getListByUserPhone(String userPhone);
	
	/**
	 * 查看已报到人的信息
	 * 
	 * @param userId
	 * @return
	 */
	public CheckInfo getInfoByUserPhoneAndOrg(String userPhone, Integer orgId);
	
	/**
	 * 删除收藏
	 * 
	 * @param checkId
	 *            
	 * @return
	 */
	 int deleteById(Integer checkId);

}