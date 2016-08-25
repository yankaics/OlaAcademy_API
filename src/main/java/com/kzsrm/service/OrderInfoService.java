package com.kzsrm.service;

import com.kzsrm.baseservice.BaseServiceMybatis;
import com.kzsrm.model.OrderInfo;

public interface OrderInfoService extends BaseServiceMybatis<OrderInfo, String> {

	/**
	 * 创建订单信息
	 * 
	 * @param checkInfo
	 * @return
	 */
	int createOrderInfo(OrderInfo orderInfo);
	
	/**
	 * 根据订单号查询订单信息
	 * 
	 * @param tradeNo
	 * @return
	 */
	OrderInfo getInfoByTradeNo(String tradeNo);
	
	/**
	 * 更新订单状态
	 * 
	 * @param checkInfo
	 * @return
	 */
	int updateOrderInfo(OrderInfo orderInfo);
	
	/**
	 * 获取订单状态
	 * 
	 * @param 
	 * @return
	 */
	int getOrderStatus(String userId,String goodsId);
}
