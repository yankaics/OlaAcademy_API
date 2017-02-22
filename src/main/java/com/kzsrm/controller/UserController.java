package com.kzsrm.controller;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.alibaba.fastjson.JSONObject;
import com.kzsrm.model.CoinHistory;
import com.kzsrm.model.User;
import com.kzsrm.model.Yzm;
import com.kzsrm.service.CoinHistoryService;
import com.kzsrm.service.UserService;
import com.kzsrm.utils.ApiCode;
import com.kzsrm.utils.ConfigUtil;
import com.kzsrm.utils.DateUtil;
import com.kzsrm.utils.JavaSmsApi;
import com.kzsrm.utils.MD5;
import com.kzsrm.utils.MapResult;
import com.kzsrm.utils.SendMail;
import com.kzsrm.utils.Tools;

@SuppressWarnings("deprecation")
@Controller
@RequestMapping("/user")
public class UserController extends SimpleFormController {
	
	@Resource
	private UserService userService;
	@Resource
	private CoinHistoryService dailyService;
	
	private static Logger logger = LoggerFactory.getLogger(User.class);

	/**
	 * 用户注册
	 */
	@RequestMapping(value = "/reg")
	@ResponseBody
	public Map<String, Object> reg(HttpServletRequest httpServletRequest, User us,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "passwd", required = false) String passwd,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "status", required = false) String status) throws ParseException {
		// 查询是否已经注册过
		Map<String, Object> map = null;
		try {
			map = userService.selectUniqueUser(phone);
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
		// 获取最新验证码
		Yzm yzmList = null;
		try {
			if (map.get("data") == null) {
				yzmList = userService.getYzm(phone);
				if (yzmList == null) {
					return MapResult.initMap(ApiCode.PARG_ERR, "没有发送验证码");
				} else {
					Date d = yzmList.getRegtime();
					String date = Tools.ymdhms.format(d);
					boolean isCodeInvalid = false;
					User u = new User();
					try {
						isCodeInvalid = Tools.codeInvalid(date, 1);
					} catch (ParseException e) {
						logger.error("", e);
						e.printStackTrace();
					}
					if (isCodeInvalid == true) {
						return MapResult.initMap(ApiCode.PARG_ERR, "验证码已过期");
					} else {
						code = code.trim();
						System.out.println("code  " + code + "|||");
						if (!yzmList.getYzm().trim().equals(code)) {
							return MapResult.initMap(ApiCode.PARG_ERR, "验证码输入有误");
						}
						u.setPhone(phone);
						u.setName("小欧"+phone.substring(7, 11));
						u.setRegtime(new Date());
						u.setPasswd(MD5.md5(passwd));
						u.setVipTime(new Date());
						u.setSignIntime(new Date());
						u.setSignIndays("1");
						u.setCoin("20");
						u.setStatus(status);
						u.setIsActive(1);// 学生
						Map<String, Object> maps = userService.insertUser(u);
						Map<String, Object> result = MapResult.initMap();
						if (Integer.parseInt(maps.get("data").toString()) == 1) {
							updateCoinHistroty("新用户注册",u.getId(),3,20); //注册获得欧拉币
							updateCoinHistroty("每日签到",u.getId(),1,5); //签到获得欧拉币
							result = MapResult.initMap();
						} else {
							result = MapResult.failMap();
						}
						result.put("user", u);
						return result;
					}
				}
			} else {
				return MapResult.initMap(ApiCode.CODE_EXIST, "用户已存在");
			}
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.initMap(ApiCode.PARG_ERR, "参数错误");
		}
	}
	
	private void updateCoinHistroty(String name,int userId,int type,int dealNum){
		if(type==1&&dailyService.getCheckInStatus(userId)==1)
			return;
		CoinHistory dailyAct = new CoinHistory();
		dailyAct.setUserId(userId);
		dailyAct.setType(type);
		dailyAct.setDealNum(dealNum);
		dailyAct.setName(name);
		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
		dailyAct.setDate(form.format(new Date()));
		dailyService.insertData(dailyAct);
	}

	/**
	 * 用户登录
	 */
	@RequestMapping(value = "/login")
	@ResponseBody
	public Map<String, Object> login(HttpServletRequest httpServletRequest, User user,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "passwd", required = false) String passwd) throws ParseException {

		Map<String, Object> maps = null;
		User users = null;
		try {
			users = userService.selByEmailOrMobile(phone);
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
		if (users == null) {
			return MapResult.initMap(ApiCode.PARG_ERR, "没有此用户");
		}
		User u = new User();
		u.setPhone(phone);
		u.setId(users.getId());
		u.setPasswd(MD5.md5(passwd.trim()));
		try {
			maps = userService.login(u);
		} catch (Exception e) {
			return MapResult.failMap();
		}
		if (maps.get("data")==null) {
			return MapResult.initMap(ApiCode.PARG_ERR, "用户名或密码错误");
		} else {
			return maps;
		}
	}
	
	/**
	 * 第三方登陆
	 * @return 已绑定手机，返回用户基本信息
	 * 		   未绑定手机，返回成功后，app端进入绑定手机号页面
	 */
	@RequestMapping(value = "/loginBySDK")
	@ResponseBody
	public Map<String, Object> loginBySDK(@RequestParam(required = true) String sourceId,
			@RequestParam(required = true) String source) throws ParseException {
		Map<String, Object> result = MapResult.initMap();
		result.put("data", userService.getUserByThirdId(source, sourceId));
		return result;
	}
	
	/**
	 * 第三方登陆绑定手机号
	 */
	@RequestMapping(value = "/bindPhoneWithSDK")
	@ResponseBody
	public Map<String, Object> bindPhoneWithSDK(
			@RequestParam(required = true) String phone,@RequestParam(required = true) String code,
			@RequestParam(required = false) String name,@RequestParam(required = false) String avator,
			@RequestParam(required = false) String sex,@RequestParam(required = true) String source,
			@RequestParam(required = true) String sourceId) throws ParseException {
		
		// 获取最新验证码
		Yzm yzmList = null;
		try {
			Map<String, Object> map = userService.selectUniqueUser(phone);
			if (map.get("data") == null) { // 不存在 － 注册新账号
				yzmList = userService.getYzm(phone);
				if (yzmList == null) {
					return MapResult.initMap(ApiCode.PARG_ERR, "没有发送验证码");
				} else {
					Date d = yzmList.getRegtime();
					String date = Tools.ymdhms.format(d);
					boolean isCodeInvalid = false;
					User u = new User();
					try {
						isCodeInvalid = Tools.codeInvalid(date, 1);
					} catch (ParseException e) {
						logger.error("", e);
						e.printStackTrace();
					}
					if (isCodeInvalid == true) {
						return MapResult.initMap(ApiCode.PARG_ERR, "验证码已过期");
					} else {
						code = code.trim();
						System.out.println("code  " + code + "|||");
						if (!yzmList.getYzm().trim().equals(code)) {
							return MapResult.initMap(ApiCode.PARG_ERR, "验证码输入有误");
						}
						u.setPhone(phone);
						u.setAvator(avator);
						u.setSex(sex);
						u.setName(TextUtils.isEmpty(name)?("小欧"+phone.substring(7, 11)):name);
						u.setRegtime(new Date());
						u.setPasswd(MD5.md5("123456"));
						u.setVipTime(new Date());
						u.setSignIntime(new Date());
						u.setSignIndays("1");
						u.setCoin("20");
						u.setIsActive(1);// 学生
						if(source.equals("wechat")){
							u.setWechatId(sourceId);
						}else if(source.equals("QQ")){
							u.setQqId(sourceId);
						}else if(source.equals("sinaMicroblog")){
							u.setSinaId(sourceId);
						}
						Map<String, Object> maps = userService.insertUser(u);
						Map<String, Object> result = MapResult.initMap();
						if (Integer.parseInt(maps.get("data").toString()) == 1) {
							updateCoinHistroty("新用户注册",u.getId(),3,20); //注册获得欧拉币
							updateCoinHistroty("每日签到",u.getId(),1,5); //签到获得欧拉币
							result = MapResult.initMap();
						} else {
							result = MapResult.failMap();
						}
						result.put("data", u);
						return result;
					}
				}
			} else { // 存在 绑定
				Map<String, Object> result = MapResult.initMap();
				User user = (User)map.get("data");
				if(source.equals("wechat")){
					user.setWechatId(sourceId);
				}else if(source.equals("QQ")){
					user.setQqId(sourceId);
				}else if(source.equals("sinaMicroblog")){
					user.setSinaId(sourceId);
				}
				userService.updateUser(user);
				result.put("data", user);
				return result;
			}
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.initMap(ApiCode.PARG_ERR, "参数错误");
		}
	}
	
	
	/**
	 * 登出
	 */
	@RequestMapping(value = "/loginOut")
	@ResponseBody
	public Map<String, Object> loginOut(HttpServletRequest httpServletRequest) throws ParseException {
		httpServletRequest.getSession().removeAttribute("user");
		return MapResult.initMap();
	}

	/**
	 * 根据id修改用户信息
	 */
	@RequestMapping(value = "/update")
	@ResponseBody
	public Map<String, Object> update(HttpServletRequest request, User user,
			@RequestParam(value = "id", required = false) String id) {
		try {
			user.setId(Integer.parseInt(id));
			return userService.updateUser(user);
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	/**
	 * 根据id修改用户信息
	 */
	@RequestMapping(value = "/updateInfo")
	@ResponseBody
	public Map<String, Object> updateInfo(HttpServletRequest request,
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "avator", required = false) String avator,
			@RequestParam(value = "sex", required = false) String sex,
			@RequestParam(value = "local", required = false) String local,
			@RequestParam(value = "realName", required = false) String realName,
			@RequestParam(value = "examType", required = false) String examType,
			@RequestParam(value = "descript", required = false) String descript) {
		try {
			User user = userService.selectUser(Integer.parseInt(id));
			user.setId(Integer.parseInt(id));
			user.setAvator(avator);
			user.setName(name);
			user.setSex(sex);
			user.setSign(descript);
			user.setExamtype(examType);
			user.setRealName(realName);
			//首次完善资料赠送50欧拉币
			if(dailyService.validateFirstPay(Integer.parseInt(id), 8)==1){
				updateCoinHistroty("完善个人资料", Integer.parseInt(id), 8, 50);
			}
			user.setLocal(local);
			return userService.updateUser(user);
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}

	/**
	 * 根据id查询用户的信息
	 */
	@RequestMapping(value = "/queryUser")
	@ResponseBody
	public Map<String, Object> queryUser(int id) {
		Map<String, Object> result = MapResult.initMap();
		try {
			User user = userService.selectUser(id);
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("id", user.getId());
			jsonObj.put("name", user.getName());
			jsonObj.put("realName", user.getRealName());
			jsonObj.put("phone", user.getPhone());
			jsonObj.put("avator", user.getAvator());
			jsonObj.put("age", user.getAge());
			jsonObj.put("sex", user.getSex());
			jsonObj.put("sign", user.getSign());
			jsonObj.put("signInDays", user.getSignIndays());
			jsonObj.put("coin", user.getCoin());
			jsonObj.put("examtype", user.getExamtype());
			jsonObj.put("isActive", user.getIsActive());
			jsonObj.put("vipTime", DateUtil.getDifferSec(new Date(), user.getVipTime())>0?DateUtil.getDifferSec(new Date(), user.getVipTime()):0);
			result.put("data", jsonObj);
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
		return result;
	}
	
	/**
	 * 老师列表
	 */
	@RequestMapping(value = "/getTeacherList")
	@ResponseBody
	public Map<String, Object> getTeacherList() {
		Map<String, Object> result = MapResult.initMap();
		try {
			List<User> userList = userService.getTeacherList();
			JSONArray userArray = new JSONArray();
			for(User user:userList){
				net.sf.json.JSONObject userObj = new net.sf.json.JSONObject();
				userObj.put("id", user.getId());
				userObj.put("avator", user.getAvator());
				userObj.put("name", user.getName());
				userObj.put("sign",user.getSign());
				userObj.put("local", user.getLocal());
				userObj.put("phone", user.getPhone());
				userArray.add(userObj);
			}
			result.put("result", userArray);
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
		return result;
	}

	/**
	 * 发送手机验证码
	 */
	@RequestMapping(value = "getYzmByPhone")
	@ResponseBody
	public Map<String, Object> getYzmByPhone(HttpServletRequest httpServletRequest,
			@RequestParam(value = "mobile", required = false) String mobile) throws IOException, URISyntaxException {
		return this.phoneYam(mobile);
	}

	/**
	 * 发送手机验证码
	 */
	public Map<String, Object> phoneYam(String mobile) throws IOException, URISyntaxException {
		String apikey = ConfigUtil.getStringValue("sms.key");
		String code = SendMail.getCode();
		System.out.println(JavaSmsApi.getUserInfo(apikey));
		String text = "【欧拉学院】" + code + "（欧拉联考验证码，十分钟内有效）";
		String result = JavaSmsApi.sendSms(apikey, text, mobile);
		System.out.println(result);
		/*
		 * JSONObject jo = new JSONObject(); jo.put("code", 0); jo.put("msg",
		 * "OK"); JSONObject jo1 = new JSONObject(); jo1.put("nick", "蚂蚁课堂");
		 * jo1.put("gmt_created", "2015-07-29 15:15:24"); jo1.put("mobile",
		 * "13240406688"); jo1.put("email", "BD@antkt.com");
		 * jo1.put("ip_whitelist", "223.72.133.2"); jo1.put("api_version",
		 * "v1"); jo1.put("alarm_balance", 100); jo1.put("emergency_contact",
		 * ""); jo1.put("emergency_mobile", ""); jo1.put("balance", 981);
		 * jo.put("user", jo1);
		 */
		JSONObject jo = JSONObject.parseObject(result);
		String msg = jo.getString("msg");
		if (msg.equals("OK")) {
			User u = new User();
			u.setPhone(mobile);
			u.setYzm(code);
			u.setRegtime(new Date());
			int res = 0;
			try {
				res = userService.insertYZM(u);
			} catch (Exception e) {
				return MapResult.failMap();
			}
			if (res == 1)
				return MapResult.initMap();
			else
				return MapResult.failMap();
		} else {
			return MapResult.failMap();
		}
	}

	// 日期处理
	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		CustomDateEditor dateEditor = new CustomDateEditor(Tools.ymd, true);
		binder.registerCustomEditor(Date.class, dateEditor);
		super.initBinder(request, binder);
	}

	/**
	 * 修改密码
	 */
	@RequestMapping(value = "modifypasswd")
	@ResponseBody
	public Map<String, Object> modifypasswd(HttpServletRequest httpServletRequest, User user,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "passwd", required = false) String passwd,
			@RequestParam(value = "code", required = false) String code)
					throws IOException, URISyntaxException, MessagingException {
		// 查询是否已经注册过
		Map<String, Object> map = null;
		try {
			map = userService.selectUniqueUser(phone);
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
		// 获取最新验证码
		Yzm yzmList = null;
		try {
			yzmList = userService.getYzm(phone);
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}

		if (yzmList != null) {
			Date d = yzmList.getRegtime();
			String date = Tools.ymdhms.format(d);
			boolean isCodeInvalid = false;
			try {
				isCodeInvalid = Tools.codeInvalid(date, 1);
			} catch (ParseException e) {
				logger.error("", e);
				e.printStackTrace();
			}

			if (isCodeInvalid == true) {
				return MapResult.initMap(ApiCode.PARG_ERR, "验证码已过期");
			} else {
				User u = new User();
				if (code.trim().equals(yzmList.getYzm().trim())) {
					passwd = MD5.md5(passwd);
					u.setPasswd(passwd);
					u.setPhone(phone);
					try {
						// 用户不存在 注册
						if (map.get("data") == null) {
							u.setName("小欧"+phone.substring(7, 11));
							u.setRegtime(new Date());
							u.setRegtime(new Date());
							u.setSignIntime(new Date());
							u.setSignIndays("1");
							u.setCoin("20");
							u.setIsActive(1);// 学生
							Map<String, Object> maps = userService.insertUser(u);
							if (Integer.parseInt(maps.get("data").toString()) == 1) {
								updateCoinHistroty("新用户注册",u.getId(),3,20); //注册获得欧拉币
								updateCoinHistroty("每日签到",u.getId(),1,5); //签到获得欧拉币
							}
						} else {
							userService.updateUser(u);
						}
					} catch (Exception e) {
						logger.error("", e);
						return MapResult.failMap();
					}
				} else {
					return MapResult.initMap(ApiCode.PARG_ERR, "验证码错误");
				}
			}
			return MapResult.initMap();
		} else {
			return MapResult.failMap();
		}
	}

	/**
	 * 定时器（执行方法暂时为空，无实际意义）
	 */
	@RequestMapping(value = "doit")
	@ResponseBody
	public void doit() {
		try {
			int result = userService.batchQuartz();
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	/**
	 * 上传图片
	 */
	@RequestMapping("fileUpload")
	@ResponseBody
	public Map<String, Object> fileUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
		String filePath = "";
		String fileName = "";
		if (!file.isEmpty()) {
			if ((file.getOriginalFilename() != null) && (file.getOriginalFilename().length() > 0)) {    
	            int dot = file.getOriginalFilename().lastIndexOf('.');    
	            if ((dot >-1) && (dot < (file.getOriginalFilename().length() - 1))) {    
	                fileName = System.currentTimeMillis()+file.getOriginalFilename().substring(dot);    
	            }    
	        }    
			try {
				filePath = request.getSession().getServletContext().getRealPath("/") + file.getOriginalFilename();
				file.transferTo(new File("/usr/tomcat/webapps/upload/"+fileName));
				System.out.println("filePath  " + filePath);
				// file.transferTo(dest);
			} catch (Exception e) {
				return MapResult.failMap();
			}
		}
		Map<String, Object> ret = MapResult.initMap();
		ret.put("result", fileName);
		return ret;
	}

}