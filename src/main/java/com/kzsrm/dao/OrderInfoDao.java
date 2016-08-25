package com.kzsrm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.OrderInfo;
import com.kzsrm.mybatis.BaseMybatisDao;

@Repository
public class OrderInfoDao<E> extends BaseMybatisDao<OrderInfo, String> {

	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.OrderInfoMapper";
	}

	public int createOrderInfo(OrderInfo orderInfo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", orderInfo.getUserId());
		map.put("tradeNo", orderInfo.getTradeNo());
		map.put("goodsId", orderInfo.getGoodsId());
		map.put("type", orderInfo.getType());
		map.put("status", orderInfo.getStatus());
		map.put("createTime", orderInfo.getCreateTime());
		return this.getSqlSession().insert(
				this.getMybatisMapperNamesapce() + ".insert", map);
	}
	
	public OrderInfo getOrderInfoByTradeNo(String tradeNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tradeNo", tradeNo);
		return this.getSqlSession().selectOne(
				this.getMybatisMapperNamesapce() + ".getByTradeNo", map);
	}
	
	public OrderInfo getOrderInfo(int userId,String goodsId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("goodsId", goodsId);
		return this.getSqlSession().selectOne(
				this.getMybatisMapperNamesapce() + ".getByUserAndGoods", map);
	}
	
	public List<OrderInfo> getOrderList(int userId,int status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("status", status);
		return this.getSqlSession().selectList(
				this.getMybatisMapperNamesapce() + ".getByUserAndStatus", map);
	}
	
	public int updateOrderInfo(OrderInfo orderInfo) {
		return this.getSqlSession().update(
				this.getMybatisMapperNamesapce() + ".updateByPrimaryKeySelective", orderInfo);
	}

}
