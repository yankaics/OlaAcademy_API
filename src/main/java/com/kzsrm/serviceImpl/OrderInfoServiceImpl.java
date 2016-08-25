package com.kzsrm.serviceImpl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.OrderInfoDao;
import com.kzsrm.model.OrderInfo;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.OrderInfoService;

@Service
@Transactional
public class OrderInfoServiceImpl extends
		BaseServiceMybatisImpl<OrderInfo, String> implements OrderInfoService {
	@Resource
	private OrderInfoDao<?> orderinfoDao;

	@Override
	protected EntityDao<OrderInfo, String> getEntityDao() {
		return orderinfoDao;
	}

	/**
	 * 创建订单信息
	 */
	@Override
	public int createOrderInfo(OrderInfo orderInfo) {
		// TODO Auto-generated method stub
		return orderinfoDao.createOrderInfo(orderInfo);
	}
	
	/**
	 * 根据订单号查询订单信息
	 * 
	 * @param tradeNo
	 * @return
	 */
	@Override
	public OrderInfo getInfoByTradeNo(String tradeNo){
		return orderinfoDao.getOrderInfoByTradeNo(tradeNo);
	}
	
	/**
	 * 更新订单状态
	 * 
	 * @param checkInfo
	 * @return
	 */
	@Override
	public int updateOrderInfo(OrderInfo orderInfo){
		return orderinfoDao.updateOrderInfo(orderInfo);
	}

	@Override
	public int getOrderStatus(String userId, String goodsId) {
		OrderInfo orderInfo = orderinfoDao.getOrderInfo(Integer.parseInt(userId), goodsId);
		if(orderInfo!=null&&orderInfo.getStatus()>0){
			return 1;
		}
		return 0;
	}
}
