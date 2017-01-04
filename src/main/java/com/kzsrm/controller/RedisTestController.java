package com.kzsrm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/redis")
public class RedisTestController {
	@ResponseBody
	@RequestMapping(value = "/getSettUnitBySettUnitIdTest")
	public void getSettUnitBySettUnitIdTest() {  
	}  
}