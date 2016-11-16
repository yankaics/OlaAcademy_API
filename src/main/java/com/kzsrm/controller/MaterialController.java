package com.kzsrm.controller;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kzsrm.model.Material;
import com.kzsrm.service.ExchangeService;
import com.kzsrm.service.MaterialService;
import com.kzsrm.utils.ComUtils;
import com.kzsrm.utils.MapResult;

@Controller
@RequestMapping("/material")
public class MaterialController {
	JsonConfig materialJC = ComUtils.jsonConfig(new String[] { "createTime" });

	private static Logger logger = LoggerFactory
			.getLogger(OlaCircleController.class);

	@Resource
	private MaterialService materialService;
	@Resource
	private ExchangeService exchangeService;

	@ResponseBody
	@RequestMapping(value = "/getMaterialList")
	public Map<String, Object> getMaterialList(
			@RequestParam(required = false) String userId,
			@RequestParam(required = false) String materialId,
			@RequestParam(required = true) String type,
			@RequestParam(defaultValue = "20") int pageSize) {
		try {
			Map<String, Object> ret = MapResult.initMap();
			List<Material> materialList = materialService.getMaterialList(
					materialId, type, pageSize);
			JSONArray materialArray = new JSONArray();
			for (Material material : materialList) {
				JSONObject jsonObj = JSONObject.fromObject(material, materialJC);
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				jsonObj.put("time", sdf.format(material.getCreateTime()));
				if(material.getPrice().equals("0")||exchangeService.getByParams(userId, material.getId()+"", "3").size()==1){
					jsonObj.put("status", 1);
				}else{
					jsonObj.put("status", 0);
				}
				materialArray.add(jsonObj);
			}
			ret.put("result", materialArray);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/updateBrowseCount")
	public Map<String, Object> updateBrowseCount(
			@RequestParam(required = true) String materialId) {
		try {
			Map<String, Object> ret = MapResult.initMap();
			materialService.updateBrowseCount(materialId);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
}
