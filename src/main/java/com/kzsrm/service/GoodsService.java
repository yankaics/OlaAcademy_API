package com.kzsrm.service;

import java.util.List;

import com.kzsrm.baseservice.BaseServiceMybatis;
import com.kzsrm.model.Goods;

public interface GoodsService  extends BaseServiceMybatis<Goods, String> {
	/**
	 * 获取商品列表
	 * @param type			类型
	 * @param pageIndex		页数
	 * @param pageSize		页大小
	 * @return
	 */
	List<Goods> getList(String type, int pageIndex, int pageSize);
	
	/**
	 * 获取已购商品列表
	 * @param userId
	 * @return
	 */
	List<Goods> getBuyList(int userId,int status);

}
