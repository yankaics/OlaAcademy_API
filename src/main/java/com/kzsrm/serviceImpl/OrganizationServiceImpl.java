package com.kzsrm.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.OrganizationDao;
import com.kzsrm.model.Organization;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.OrganizationService;
import com.kzsrm.utils.CustomException;

@Service
@Transactional
public class OrganizationServiceImpl extends BaseServiceMybatisImpl<Organization, String> implements OrganizationService {
	@Resource
	private OrganizationDao<?> organizationDao;

	@Override
	protected EntityDao<Organization, String> getEntityDao() {
		return organizationDao;
	}

	/**
	 * 查询机构信息
	 * @param pid
	 * @param type
	 * @return
	 */
	@Override
	public Organization getOrganizationById(Integer orgId) {
		// TODO Auto-generated method stub
		return  organizationDao.getOrganizationById(orgId);
	}
	/**
	 * 查询机构信息
	 * @param pid
	 * @param type
	 * @return
	 */
	@Override
	public List<Organization> getAllOrganization(){
		return organizationDao.getAllOrganization();
	}
	
	/**
	 * 获取机构列表
	 * 
	 * @param pageIndex
	 *            页数
	 * @param pageSize
	 *            页大小
	 * @return
	 */
	public List<Organization> getOrgList(int pageIndex, int pageSize) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pageIndex", (pageIndex - 1) * pageSize);
		map.put("pageSize", pageSize);
		return organizationDao.getOrgList(map);
	}

	/**
	 * 更新关注次数信息
	 * @param orgId
	 * @throws CustomException 
	 */
	@Override
	public void updateAttendCount(String orgId) throws CustomException {
		
		Organization org = organizationDao.getOrganizationById(Integer.parseInt(orgId));
		if (org == null)
			throw new CustomException("机构不存在");
		
		org.setAttendCount(org.getAttendCount() + 1);
		organizationDao.update(org);
	}
	
	/**
	 * 更新报到次数信息
	 * @param orgId
	 * @param type
	 * @throws CustomException 
	 */
	@Override
	public void updateCheckInCount(String orgId,String type) throws CustomException {
		
		Organization org = organizationDao.getOrganizationById(Integer.parseInt(orgId));
		if (org == null)
			throw new CustomException("机构不存在");
		if(Integer.parseInt(type)==1){
			org.setCheckinCount(org.getCheckinCount() + 1);
		}else{
			org.setCheckinCount(org.getCheckinCount() - 1);
		}
		
		organizationDao.update(org);
	}
	
}
