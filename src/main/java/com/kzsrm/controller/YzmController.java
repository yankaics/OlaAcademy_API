package com.kzsrm.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.kzsrm.model.User;
import com.kzsrm.service.UserService;

@Controller
@RequestMapping("/yzm")
public class YzmController {

	DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

	@Resource
	private UserService userService;

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(User.class);

}
