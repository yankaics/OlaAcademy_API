package com.kzsrm.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.BannerDao;
import com.kzsrm.model.Banner;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.BannerService;

@Service
@Transactional
public class BannerServiceImpl extends BaseServiceMybatisImpl<Banner, String>
		implements BannerService {
	@Resource
	private BannerDao<?> bannerDao;

	@Override
	protected EntityDao<Banner, String> getEntityDao() {
		return bannerDao;
	}

	public List<Banner> getBannerList(int pageSize) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pageSize", pageSize);
		return bannerDao.getBannerList(map);
	}
}
