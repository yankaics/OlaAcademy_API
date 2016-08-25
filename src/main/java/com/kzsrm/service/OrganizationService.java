package com.kzsrm.service;

import java.util.List;

import com.kzsrm.baseservice.BaseServiceMybatis;
import com.kzsrm.model.Organization;
import com.kzsrm.utils.CustomException;

public interface OrganizationService  extends BaseServiceMybatis<Organization, String> {

	
	/**
	 * 查询机构信息
	 * @param pid
	 * @param type
	 * @return
	 */
	Organization getOrganizationById(Integer orgId);
	
	/**
	 * 查询所有机构信息
	 * @param pid
	 * @param type
	 * @return
	 */
	List<Organization> getAllOrganization();
	
	/**
	 * 获取机构列表
	 * 
	 * @param pageIndex
	 *            页数
	 * @param pageSize
	 *            页大小
	 * @return
	 */
	List<Organization> getOrgList(int pageIndex, int pageSize);
	
	/**
	 * 更新关注次数信息
	 * @param orgId
	 * @throws CustomException 
	 */
	void updateAttendCount(String orgId) throws CustomException;
	

	/**
	 * 更新报到次数信息
	 * @param orgId
	 * @param type
	 * @throws CustomException 
	 */
	void updateCheckInCount(String orgId,String type) throws CustomException;
	
}
