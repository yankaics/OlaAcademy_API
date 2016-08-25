package com.kzsrm.controller;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

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
import com.kzsrm.model.Sign;
import com.kzsrm.model.User;
import com.kzsrm.model.Yzm;
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
						Sign sign = new Sign();
						sign.setPhone(phone);
						sign.setSignNum(100);
						this.userService.insertSign(sign);
						u.setPhone(phone);
						u.setName("小欧"+phone.substring(7, 11));
						u.setRegtime(new Date());
						u.setPasswd(MD5.md5(passwd));
						u.setRegtime(new Date());
						u.setStatus(status);
						u.setIsActive(1);// 未激活
						Map<String, Object> maps = userService.insertUser(u);
						Map<String, Object> result = MapResult.initMap();
						if (Integer.parseInt(maps.get("data").toString()) == 1) {
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
			@RequestParam(value = "descript", required = false) String descript) {
		try {
			User user = new User();
			user.setId(Integer.parseInt(id));
			user.setAvator(avator);
			user.setName(name);
			user.setSex(sex);
			user.setSign(descript);
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
			jsonObj.put("phone", user.getPhone());
			jsonObj.put("avator", user.getAvator());
			jsonObj.put("age", user.getAge());
			jsonObj.put("sex", user.getSex());
			jsonObj.put("sign", user.getSign());
			jsonObj.put("vipTime", DateUtil.getDifferSec(new Date(), user.getVipTime())>0?DateUtil.getDifferSec(new Date(), user.getVipTime()):0);
			result.put("data", jsonObj);
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
						if (map.get("data") == null) {
							u.setIsActive(2);
							userService.insertUser(u);
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
	 * 签到
	 */
	@RequestMapping(value = "userSignIn")
	@ResponseBody
	public Map<String, Object> userSignIn(HttpServletRequest httpServletRequest,
			@RequestParam(value = "uid", required = false) int uid,
			@RequestParam(value = "phone", required = false) String phone) throws ParseException {
		// 查询该用户签到
		Sign listSign = userService.getSign(phone);
		if (listSign != null) {
			Sign s = new Sign();
			s.setPhone(phone);
			s.setUid(uid);
			// 最后签到和当前差天数
			System.out.println(listSign.getLastSignDay() + "    " + new Date());
			int diffDay = DateUtil.getDifferSec(listSign.getLastSignDay(), new Date());
			if (diffDay == 0) {
				return MapResult.initMap(ApiCode.PARG_ERR, "今天已经签到啦");
			} else if (diffDay == 1) {
				System.out.println(listSign.getSignNum());
				int status = listSign.getSignNum();
				int signTotalNum = listSign.getSignTotalNum();
				if (signTotalNum == 0) {
					signTotalNum = 1;
				}
				if (status == 0) {
					status += 2;
					s.setSignNum(listSign.getSignNum() + 2);
				} else {
					status += 1;
					s.setSignNum(listSign.getSignNum() + 1);
				}
				int totalSignNum = 0;
				if (status == 1) {
					totalSignNum += 1;
				} else if (status == 2) {
					totalSignNum += 2;
				} else if (status == 3) {
					totalSignNum += 4;
				} else if (status == 4) {
					totalSignNum += 8;
				} else if (status == 5) {
					totalSignNum += 10;
				} else if (status == 6) {
					totalSignNum += 30;
				} else if (status > 6) {
					totalSignNum += 10;
				}
				if (status % 10 == 0) {
					totalSignNum += 30;
				}
				System.out.println("连续打卡赠送的币    " + totalSignNum);
				s.setAntCoin(listSign.getAntCoin() + totalSignNum);
				s.setSignTotalNum(listSign.getSignTotalNum() + 1);
				userService.updateSign(s);
				return MapResult.initMap(ApiCode.PARG_ERR, "签到成功");
			} else {
				s.setAntCoin(1);
				s.setSignNum(0);
				s.setSignTotalNum(listSign.getSignTotalNum() + 1);
				userService.updateSign(s);
				return MapResult.initMap(ApiCode.PARG_ERR, "签到过期  已经清零");
			}
		} else {
			try {
				Sign si = new Sign();
				si.setPhone(phone);
				si.setUid(uid);
				si.setSignNum(1);
				si.setSignTotalNum(1);
				boolean insertSign = userService.insertSign(si);
				if (insertSign == true) {
					return MapResult.initMap(ApiCode.PARG_ERR, "签到成功");
				} else {
					return MapResult.initMap(ApiCode.PARG_ERR, "签到失败");
				}
			} catch (Exception e) {
				logger.error("", e);
				return MapResult.failMap();
			}
		}
	}

	/**
	 * 每天扣除100蚂蚁币
	 */
	@RequestMapping(value = "doit")
	@ResponseBody
	public void doit() {
		try {
			int result = userService.batchQuartz();
			System.out.println("定时扣除蚂蚁币执行任务返回结果  " + result);
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

	/**
	 * 发送邮箱验证码
	 * 
	 * 成功返回true 失败返回false
	 * 
	 * @return
	 * @throws MessagingException
	 *//*
		 * @RequestMapping(value = "getYzmByEmail")
		 * 
		 * @ResponseBody public Map<String, Object>
		 * getYzmByEmail(HttpServletRequest httpServletRequest, User user,
		 * 
		 * @RequestParam(value = "email", required = false) String email) throws
		 * MessagingException { return emailZym(email, user); }
		 */

	/**
	 * 发送邮件验证码
	 * 
	 * @param email
	 * @param user
	 * @return
	 * @throws MessagingException
	 */
	/*
	 * public Map<String, Object> emailZym(String email, User user) throws
	 * MessagingException { if (!email.contains("@")) { return
	 * MapResult.initMap(ApiCode.PARG_ERR, "邮箱格式错误"); } StringBuffer sb = new
	 * StringBuffer("点击下面链接激活账号，2小时后失效，否则重新注册账号，链接只能使用一次，请尽快激活！"); String code =
	 * SendMail.getCode(); sb.append(
	 * "<a href=\"http://localhost:8080/kzsrm/user/mailActive?email=");
	 * sb.append(email); sb.append("&yzm="); sb.append(code);
	 * sb.append("\"></br>http://localhost:8080/kzsrm/user/mailActive?email=");
	 * sb.append(email); sb.append("&yzm="); sb.append(code); sb.append("</a>");
	 * boolean flag = false; Map<String, Object> map = MapResult.initMap(); if
	 * (email != null) { flag = SendMail.sendMail(email, "", "", "",
	 * sb.toString()); } // 添加验证码 User u = new User(); if (email != null) {
	 * u.setEmail(user.getEmail()); } u.setYzm(code); u.setRegtime(new Date());
	 * int res = 0; try { res = userService.insertYZM(u); } catch (Exception e)
	 * { return MapResult.failMap(); } if (flag == true && res == 1) { return
	 * map; } else { map = MapResult.initMap(ApiCode.PARG_ERR, "参数错误"); } return
	 * map; }
	 */
	/*	*//**
			 * 邮箱激活验证码
			 * 
			 * @param httpServletRequest
			 * @param phone
			 * @param email
			 * @return
			 *//*
			 * @RequestMapping(value = "mailActive")
			 * 
			 * @ResponseBody public Map<String, Object>
			 * mailActive(HttpServletRequest httpServletRequest, User user,
			 * 
			 * @RequestParam(value = "phone", required = false) String phone,
			 * 
			 * @RequestParam(value = "email", required = false) String email,
			 * 
			 * @RequestParam(value = "yzm", required = false) String yzm) { User
			 * u = new User(); // 获取验证码发送时间 Yzm yzmList =
			 * userService.getYzm(phone); if (yzmList == null) { return
			 * MapResult.initMap(ApiCode.PARG_ERR, "没有发送验证码"); } String str =
			 * yzmList.getYzm(); boolean isCodeInvalid = false; String time =
			 * Tools.ymdhms.format(yzmList.getRegtime()); if (phone != null) {
			 * try { isCodeInvalid = Tools.codeInvalid(time, 1); } catch
			 * (ParseException e) { logger.error("", e); e.printStackTrace(); }
			 * } else if (email != null) { try { isCodeInvalid =
			 * Tools.codeInvalid(time, 0); } catch (ParseException e) {
			 * logger.error("", e); e.printStackTrace(); } } if (isCodeInvalid
			 * == true) { return MapResult.initMap(ApiCode.PARG_ERR, "验证码已过期");
			 * } else { if (str.trim().equals(yzm.trim())) { u.setEmail(email);
			 * u.setPhone(phone); u.setIsActive(2);// 已激活 Sign sign = new
			 * Sign(); // sign. sign.setEmail(email); sign.setPhone(phone);
			 * sign.setSignNum(100);// 注册成功即送100蚂蚁币
			 * userService.insertSign(sign); return userService.updateUser(u); }
			 * else { return MapResult.initMap(ApiCode.PARG_ERR, "验证码错误"); } } }
			 */

	/**
	 * 查询手机验证码是否过期
	 * 
	 * @param httpServletRequest
	 * @param phone
	 * @param yzm
	 *//*
		 * @RequestMapping(value = "phoneIsInvalid")
		 * 
		 * @ResponseBody public Map<String, Object>
		 * phoneIsInvalid(HttpServletRequest httpServletRequest,
		 * 
		 * @RequestParam(value = "phone", required = false) String phone,
		 * 
		 * @RequestParam(value = "yzm", required = false) String yzm) {
		 * 
		 * if (phone == null || yzm == null) { return
		 * MapResult.initMap(ApiCode.PARG_ERR, "参数错误"); }
		 * 
		 * // 获取验证码发送时间 Yzm yzmList = userService.getYzm(phone); if (yzmList ==
		 * null) { return MapResult.initMap(ApiCode.PARG_ERR, "没有发送验证码"); }
		 * String str = yzmList.getYzm(); boolean isCodeInvalid = false; String
		 * time = Tools.ymdhms.format(yzmList.getRegtime()); if (phone != null)
		 * { try { isCodeInvalid = Tools.codeInvalid(time, 1); } catch
		 * (ParseException e) { logger.error("", e); e.printStackTrace(); } } if
		 * (isCodeInvalid == true) { return MapResult.initMap(ApiCode.PARG_ERR,
		 * "验证码已过期"); } else { if (str.trim().equals(yzm.trim())) { return
		 * MapResult.initMap(); } else { return
		 * MapResult.initMap(ApiCode.PARG_ERR, "验证码错误"); } } }
		 */

	/*	*//**
			 * 用户唯一性验证(手机和邮箱)
			 * 
			 * @param httpServletRequest
			 * 			@return{"message":"成功","data":"true","apicode":10000}
			 *            true 有此用户 false不存在该用户
			 *//*
			 * @RequestMapping(value = "userUnique")
			 * 
			 * @ResponseBody public Map<String, Object>
			 * userUnique(HttpServletRequest httpServletRequest,
			 * 
			 * @RequestParam(value = "email", required = false) String email,
			 * 
			 * @RequestParam(value = "mobile", required = false) String mobile)
			 * { try { Map<String, Object> user =
			 * userService.selectUniqueUser(email, mobile); Map<String, Object>
			 * map = MapResult.initMap(); if (user.get("data") == null) {
			 * map.put("data", "false"); } else { map.put("data", "true"); }
			 * return map; } catch (Exception e) { logger.error("", e); return
			 * MapResult.failMap(); } }
			 */

}