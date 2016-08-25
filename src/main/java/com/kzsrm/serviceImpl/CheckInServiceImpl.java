package com.kzsrm.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.CheckInfoDao;
import com.kzsrm.model.CheckInfo;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.CheckInService;

@Service
@Transactional
public class CheckInServiceImpl extends BaseServiceMybatisImpl<CheckInfo, String> implements CheckInService {
	@Resource
	private CheckInfoDao<?> checkinfoDao;

	@Override
	protected EntityDao<CheckInfo, String> getEntityDao() {
		return checkinfoDao;
	}

	/**
	 * 向机构报名
	 */
	@Override
	public int checkIn(CheckInfo checkInfo) {
		// TODO Auto-generated method stub
		return checkinfoDao.checkInToOrganization(checkInfo);
	}
	
	/**
	 * 查看已报到机构列表
	 * 
	 * @param orgId
	 * @return
	 */
	public List<CheckInfo> getListByUserPhone(String userPhone){
		return checkinfoDao.getByUserPhone(userPhone);
	}
	
	/**
	 * 查看已报到人的信息
	 * 
	 * @param orgId
	 * @return
	 */
	public CheckInfo getInfoByUserPhoneAndOrg(String userPhone, Integer orgId){
		return checkinfoDao.getInfoByUserPhoneAndOrg(userPhone,orgId);
	}
	
	/**
	 * 删除报名
	 * 
	 * @param checkId
	 *            
	 * @return
	 */
	@Override
	public int deleteById(Integer checkId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", checkId);
		return checkinfoDao.deleteById(map);
	}
}
