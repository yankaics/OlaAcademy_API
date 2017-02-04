package com.kzsrm.serviceImpl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.AuthInfoDao;
import com.kzsrm.model.AuthInfo;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.AuthInfoService;

@Service
@Transactional
public class AuthInfoServiceImpl extends BaseServiceMybatisImpl<AuthInfo, String>
		implements AuthInfoService {
	@Resource
	private AuthInfoDao<?> authDao;

	@Override
	protected EntityDao<AuthInfo, String> getEntityDao() {
		return authDao;
	}
	
	@Override
	public AuthInfo queryAuthInfo(String userId) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		return authDao.getByUserId(map);
	}

	@Override
	public void submitAuthInfo(String userId, String name, String phone, String email, String profile,String domain) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		AuthInfo authInfo = authDao.getByUserId(map);
		if(authInfo!=null){ //更新认证信息
			authInfo.setName(name);
			authInfo.setPhone(phone);
			authInfo.setProfile(profile);
			authInfo.setEmail(email);
			authInfo.setDomain(domain);
			authInfo.setStatus(1);
			authInfo.setCreateTime(new Date());
			authDao.update(authInfo);
		}else{
			AuthInfo auth = new AuthInfo();
			auth.setUserId(Integer.parseInt(userId));
			auth.setName(name);
			auth.setPhone(phone);
			auth.setProfile(profile);
			auth.setEmail(email);
			auth.setDomain(domain);
			auth.setStatus(1);
			auth.setCreateTime(new Date());
			authDao.save(auth);
		}
	}
	
	@Override
	public void cancelAuth(String userId) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		AuthInfo authInfo = authDao.getByUserId(map);
		if(authInfo!=null){
			authInfo.setStatus(0);
			authInfo.setCreateTime(new Date());
			authDao.update(authInfo);
		}
	}

}
