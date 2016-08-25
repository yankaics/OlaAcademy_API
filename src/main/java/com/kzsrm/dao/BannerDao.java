package com.kzsrm.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.Banner;
import com.kzsrm.mybatis.BaseMybatisDao;

@Repository
public class BannerDao<E> extends BaseMybatisDao<Banner, String> {

	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.BannerMapper";
	}

	public List<Banner> getBannerList(Map<String, Object> map) {
		return this.getSqlSession().selectList(
				this.getMybatisMapperNamesapce() + ".getBannerList", map);
	}

}
