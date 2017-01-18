package com.kzsrm.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kzsrm.model.Banner;
import com.kzsrm.model.Course;
import com.kzsrm.model.Goods;
import com.kzsrm.model.OlaCircle;
import com.kzsrm.model.User;
import com.kzsrm.service.BannerService;
import com.kzsrm.service.CourseService;
import com.kzsrm.service.GoodsService;
import com.kzsrm.service.OlaCircleService;
import com.kzsrm.service.SubjectLogService;
import com.kzsrm.service.UserService;
import com.kzsrm.utils.ComUtils;
import com.kzsrm.utils.MapResult;

@Controller
@RequestMapping("/home")
public class HomePageController {
	private static Logger logger = LoggerFactory
			.getLogger(HomePageController.class);
	
	JsonConfig conf = ComUtils.jsonConfig(new String[]{"createTime"});

	@Resource
	private UserService userService;
	@Resource
	private BannerService bannerService;
	@Resource
	private GoodsService goodsService;
	@Resource
	private CourseService courseService;
	@Resource
	private OlaCircleService circleService;
	@Resource
	private SubjectLogService subjectLogService;

	
	@ResponseBody
	@RequestMapping(value = "/getHomeList")
	public Map<String, Object> getHomeList(@RequestParam(required = false) String userId) {
		try {
			Map<String, Object> ret = MapResult.initMap();
			JSONObject jsonObj = new JSONObject();
			List<Banner> bannerList = bannerService.getBannerList(3);
			JSONArray bannerArray = new JSONArray();
			for(Banner banner : bannerList){
				JSONObject bannerObj = JSONObject.fromObject(banner, conf);
				if(banner.getType()==3){ //精品课
					bannerObj.put("commodity", JSONObject.fromObject(goodsService.getById(banner.getObjectId()+""),conf));
				}
				bannerArray.add(bannerObj);
			}
			List<Goods> goodsList = goodsService.getList("1", 1,
					2);
			List<Course> courseList = courseService.getRecentHotList(1);
			List<OlaCircle> questionList = circleService.getCircleList(userId,"", 2, "2");
			JSONArray questionArray = new JSONArray();
			for(OlaCircle circle : questionList){
				JSONObject question = new JSONObject();
				question.put("id", circle.getId());
				question.put("title", circle.getTitle());
				question.put("content", circle.getContent());
				question.put("number", circle.getReadNumber());
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				question.put("time", sdf.format(circle.getCreateTime()));
				questionArray.add(question);
			}
			if(TextUtils.isEmpty(userId)){
				jsonObj.put("studyDay", "1");
				jsonObj.put("finishCount", "0");
				jsonObj.put("defeatPercent", "0%");
			}else{
				User u = userService.selectUser(Integer.parseInt(userId));
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				String today = formatter.format(new Date());
				if(!today.equals(u.getLearntime())){
					int learndays = Integer.parseInt(u.getLearndays())+1;
					u.setLearntime(today);
					u.setLearndays(learndays+"");
					userService.updateUser(u);
					jsonObj.put("studyDay", learndays);
				}else{
					jsonObj.put("studyDay", u.getLearndays());
				}
				jsonObj.put("finishCount", subjectLogService.getTotalFinishCount(userId));
				Random random=new java.util.Random();
				jsonObj.put("defeatPercent", random.nextInt(70)+30+"%");
			}
			jsonObj.put("bannerList",bannerArray);
			jsonObj.put("questionList",questionArray);
			jsonObj.put("goodsList",goodsList);
			jsonObj.put("courseList",courseList);

			ret.put("result", jsonObj);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
}
