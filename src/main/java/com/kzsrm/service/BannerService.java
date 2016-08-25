package com.kzsrm.service;

import java.util.List;

import com.kzsrm.baseservice.BaseServiceMybatis;
import com.kzsrm.model.Banner;

public interface BannerService extends BaseServiceMybatis<Banner, String> {

	List<Banner> getBannerList(int pageSize);

}
