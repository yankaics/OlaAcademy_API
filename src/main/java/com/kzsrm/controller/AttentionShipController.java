package com.kzsrm.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kzsrm.model.AttentionShip;
import com.kzsrm.service.AttentionShipService;
import com.kzsrm.utils.MapResult;

@Controller
@RequestMapping("/attention")
public class AttentionShipController {

	private static Logger logger = LoggerFactory
			.getLogger(AttentionShipController.class);

	@Resource
	private AttentionShipService shipService;

	/**
	 * 添加关注
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/attendUser")
	public Map<String, Object> attendUser(
			@RequestParam(required = true) int attendId,
			@RequestParam(required = true) int attendedId,
			@RequestParam(required = true) int type) {
		try {
			Map<String, Object> ret = MapResult.initMap();
			List<AttentionShip> shipList = shipService.getByParams(attendId, attendedId);
			if(type==1){
				if(shipList.size()>0){
					return ret;
				}else{
					AttentionShip attention = new AttentionShip();
					attention.setAttendId(attendId);
					attention.setAttendedId(attendedId);
					attention.setDefriend(0);
					attention.setCreateTime(new Date());
					shipService.insertData(attention);
				}
			}else if(type==2){
				if(shipList.size()>0){
					shipService.delete(shipList.get(0).getId()+"");
				}
			}
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
}
