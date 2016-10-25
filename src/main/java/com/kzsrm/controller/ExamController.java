package com.kzsrm.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kzsrm.model.Examination;
import com.kzsrm.model.Subject;
import com.kzsrm.model.User;
import com.kzsrm.service.ExamService;
import com.kzsrm.service.ExchangeService;
import com.kzsrm.service.SubjectService;
import com.kzsrm.service.UserService;
import com.kzsrm.utils.ApiCode;
import com.kzsrm.utils.ComUtils;
import com.kzsrm.utils.DateUtil;
import com.kzsrm.utils.MapResult;

@Controller
@RequestMapping("/exam")
public class ExamController {
	private static Logger logger = LoggerFactory
			.getLogger(ExamController.class);
	JsonConfig examCf = ComUtils.jsonConfig(new String[] { "isfree" });

	@Resource
	private ExamService examService;
	@Resource
	private SubjectService subjectService;
	@Resource
	private UserService userService;
	@Resource
	private ExchangeService exchangeService;

	/**
	 * 模考列表
	 * 
	 * @param courseId
	 *            课程id
	 * @param type
	 *            1-题库，2-真题
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getExamList")
	public Map<String, Object> getExamList(
			@RequestParam(required = true) String courseId,
			@RequestParam(required = true) String type,
			@RequestParam(required = false) String userId) {
		try {
			if (StringUtils.isBlank(courseId))
				return MapResult.initMap(ApiCode.PARG_ERR, "课程id为空");

			Map<String, Object> ret = MapResult.initMap();
			JSONArray jsonArray = new JSONArray();
			List<Examination> examList = examService.getListByCour(courseId,
					type);

			for (int i = 0; i < examList.size(); i++) {
				JSONObject jsonObj = JSONObject.fromObject(examList.get(i), examCf);
				Examination exam = examList.get(i);
				if (!StringUtils.isEmpty(userId)) {
					User user = userService.selectUser(Integer.parseInt(userId));
					if (exam.getIsfree() == 0) {
						// 会员或者用欧拉币兑换
						if ((user.getVipTime() != null
								&& DateUtil.getDifferSec(user.getVipTime(),
										new Date()) < 0)||exchangeService.getByParams(userId, exam.getId()+"", "2").size()==1) {
							jsonObj.put("isfree", 1);
						} else {
							jsonObj.put("isfree", 0);
						}
					} else {
						jsonObj.put("isfree", 1);
					}
				}else{
					jsonObj.put("isfree", exam.getIsfree());
				}
				if(!StringUtils.isEmpty(userId)){
					jsonObj.put("progress", examService.getHasDoneProgress(exam.getId(), userId));
					jsonObj.put("rank", (int)(Math.random()*100)+1);
				}else{
					jsonObj.put("progress", 0);
					jsonObj.put("rank", (int)(Math.random()*100)+1);
				}
				
				jsonArray.add(jsonObj);
			}

			ret.put("result", jsonArray);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}

	/**
	 * 获取模考对应的测试题
	 * 
	 * @param examId
	 *            模考id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getExamSubList")
	public Map<String, Object> getExamSubList(
			@RequestParam(required = true) String examId) {
		try {
			if (StringUtils.isBlank(examId))
				return MapResult.initMap(ApiCode.PARG_ERR, "模考id为空");

			Map<String, Object> ret = MapResult.initMap();
			List<Subject> subList = subjectService.getSubjectByExam(examId);
			ret.put("result", subList);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}

}
