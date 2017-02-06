package com.kzsrm.controller;

import java.util.ArrayList;
import java.util.HashMap;
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

import com.kzsrm.model.CheckInfo;
import com.kzsrm.model.Organization;
import com.kzsrm.model.Teacher;
import com.kzsrm.model.TeacherOrg;
import com.kzsrm.model.User;
import com.kzsrm.service.CheckInService;
import com.kzsrm.service.OrganizationService;
import com.kzsrm.service.TeacherOrgService;
import com.kzsrm.service.TeacherService;
import com.kzsrm.service.UserService;
import com.kzsrm.utils.ComUtils;
import com.kzsrm.utils.MapResult;
import com.kzsrm.utils.SendMail;

@Controller
@RequestMapping("/organization")
public class OrganizationController {
	private static Logger logger = LoggerFactory.getLogger(Organization.class);
	JsonConfig orgCf = ComUtils.jsonConfig(new String[]{});

	@Resource
	private UserService userService;

	@Resource
	private OrganizationService organizationService;

	@Resource
	private TeacherOrgService teacherOrgService;

	@Resource
	private TeacherService teacherService;

	@Resource
	private CheckInService checkinService;

	/**
	 * 机构列表（老版本）
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getAllOrganization")
	public Map<String, Object> getAllOrganization(
			@RequestParam(required = false) String userId) {
		try {
			Map<String, Object> ret = MapResult.initMap();
			List<Organization> organizationList = organizationService
					.getAllOrganization();
			JSONArray jsonArray = new JSONArray();
			for (Organization org : organizationList) {
				JSONObject jsonobj = JSONObject.fromObject(org, orgCf);
				if (!StringUtils.isEmpty(userId)) {
					User user = userService
							.selectUser(Integer.parseInt(userId));
					if (user != null) {
						if (checkinService.getInfoByUserPhoneAndOrg(
								user.getPhone(), org.getId()) != null) {
							jsonobj.put("checkedIn", "1");
						}else{
							jsonobj.put("checkedIn", "0");
						}
					}else{
						jsonobj.put("checkedIn", "0");
					}
				}else{
					jsonobj.put("checkedIn", "0");
				}
				jsonArray.add(jsonobj);
			}
			ret.put("result", jsonArray);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}
	
	/**
	 * 机构类型及列表 （机构 学校）
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getOrganizationInfo")
	public Map<String, Object> getOrganizationInfo(
			@RequestParam(required = false) String userId) {
		try {
			Map<String, Object> ret = MapResult.initMap();
			List<Organization> organizationList = organizationService
					.getAllOrganization();
			JSONArray orgArray = new JSONArray();
			JSONArray orgArray2 = new JSONArray();
			JSONArray schoolArray = new JSONArray();
			for (Organization org : organizationList) {
				JSONObject jsonobj = JSONObject.fromObject(org, orgCf);
				if(org.getType()==1){ //机构
					orgArray.add(jsonobj);
				}else if(org.getType()==2){
					orgArray2.add(jsonobj);
				}else if(org.getType()==3){
					schoolArray.add(jsonobj);
				}
			}
			JSONArray jsonArray = new JSONArray();
			JSONObject orgObj = new JSONObject();
			orgObj.put("optionName", "笔试辅导");
			orgObj.put("optionList", orgArray);
			jsonArray.add(orgObj);
			JSONObject orgObj2 = new JSONObject();
			orgObj2.put("optionName", "面试辅导");
			orgObj2.put("optionList", orgArray2);
			jsonArray.add(orgObj2);
			JSONObject schoolObj = new JSONObject();
			schoolObj.put("optionName", "学历提升");
			schoolObj.put("optionList", schoolArray);
			jsonArray.add(schoolObj);
			ret.put("result", jsonArray);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}

	/**
	 * 获取机构下所有教师
	 * 
	 * @param oid
	 *            机构id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getTeacherListByOrgId")
	public Map<String, Object> getTeacherListByOrgId(
			@RequestParam(required = true) String orgId) {
		try {
			Map<String, Object> ret = MapResult.initMap();
			List<TeacherOrg> organizationList = teacherOrgService
					.getListByOrgId(orgId);
			List<Teacher> teacherList = new ArrayList<Teacher>();
			for (TeacherOrg teacherOrg : organizationList) {
				Teacher t = teacherService.getTeacherById(teacherOrg.getTid());
				if (t != null) {
					teacherList.add(t);
				}
			}
			ret.put("result", teacherList);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}

	/**
	 * 向机构报名
	 * 
	 * @param userId
	 *            userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkin")
	public Map<String, Object> checkin(
			@RequestParam(required = true) String orgId,
			@RequestParam(required = true) String checkinTime,
			@RequestParam(required = true) String userPhone,
			@RequestParam(defaultValue = "1") int type,
			@RequestParam(required = false) String userLocal) {
		try {
			CheckInfo checkInfo = new CheckInfo();
			checkInfo.setOrgId(Integer.parseInt(orgId));
			checkInfo.setCheckinTime(checkinTime);
			checkInfo.setUserLocal(userLocal);
			checkInfo.setType(type);
			checkInfo.setUserPhone(userPhone);
			if (checkinService.getInfoByUserPhoneAndOrg(userPhone,
					Integer.parseInt(orgId)) == null) {
				if (checkinService.checkIn(checkInfo) == 1) {
					Organization org =  organizationService.getById(orgId);
					String mailInfo = "报名人手机号:"+userPhone+"\n 所报班级："+org.getName();
					User u = (User)userService.selectUniqueUser(userPhone).get("data");
					if(u!=null){
						mailInfo= "报名人:"+ (u.getRealName()==null?u.getName():u.getRealName())+"\n手机号:"+userPhone+"\n所报班级："+org.getName();
					}
					if(u!=null&&!StringUtils.isEmpty(u.getExamtype())){
						mailInfo+= "\n关注领域："+u.getExamtype();
					}
					if(u!=null&&!StringUtils.isEmpty(u.getLocal())){
						mailInfo+= "\n所在地区："+u.getLocal();
					}
					switch (org.getId()) {
					case 1:
						SendMail.sendMail("myofficer@qq.com", "renjincheng@126.com", "151621577@qq.com", "欧拉学院报名信息", mailInfo);
						break;
					case 2:
						SendMail.sendMail("2221650373@qq.com", "renjincheng@126.com", "contact@olaxueyuan.com", "欧拉学院报名信息", mailInfo);
						break;
					case 3:
					case 4:
					case 6:
						SendMail.sendMail("3396638086@qq.com", "renjincheng@126.com", "contact@olaxueyuan.com", "欧拉学院报名信息", mailInfo);
						break;
					case 7:
						SendMail.sendMail("895290962@qq.com", "renjincheng@126.com", "contact@olaxueyuan.com", "欧拉学院报名信息", mailInfo);
						break;
					}
					return MapResult.initMap();
				} else {
					return MapResult.initMap(-1, "数据存储错误");
				}
			} else {
				return MapResult.initMap(-1, "您已报名过");
			}

		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}

	/**
	 * 取消向机构报名
	 * 
	 * @param userId
	 *            userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/cancelCheckIn")
	public Map<String, Object> cancelCheckIn(
			@RequestParam(required = true) String checkId) {
		try {
			if (checkinService.deleteById(Integer.parseInt(checkId)) == 1) {
				return MapResult.initMap();
			} else {
				return MapResult.initMap(-1, "取消失败");
			}
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}

	/**
	 * 查看报名机构列表
	 * 
	 * @param userId
	 *            userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getListByPhone")
	public Map<String, Object> getListByPhone(
			@RequestParam(required = true) String userPhone) {
		try {

			Map<String, Object> ret = MapResult.initMap();
			List<CheckInfo> infoList = checkinService
					.getListByUserPhone(userPhone);
			List<HashMap<String, Object>> resultList = new ArrayList<HashMap<String, Object>>();
			for (CheckInfo checkInfo : infoList) {
				Organization org = organizationService
						.getOrganizationById(checkInfo.getOrgId());
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("orgId", org.getId());
				map.put("orgName", org.getName());
				map.put("orgPic", org.getLogo());
				map.put("checkId", checkInfo.getId());
				map.put("userPhone", checkInfo.getUserPhone());
				map.put("userLocal", checkInfo.getUserLocal());
				map.put("type", checkInfo.getType());
				map.put("checkinTime", checkInfo.getCheckinTime());
				resultList.add(map);
			}

			ret.put("result", resultList);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}

	/**
	 * 查看报名人信息
	 * 
	 * @param userId
	 *            userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getInfoByUserPhoneAndOrg")
	public Map<String, Object> getInfoByUserPhoneAndOrg(
			@RequestParam(required = true) String userPhone,
			@RequestParam(required = true) String orgId) {
		try {

			Map<String, Object> ret = MapResult.initMap();
			CheckInfo info = checkinService.getInfoByUserPhoneAndOrg(userPhone,
					Integer.parseInt(orgId));
			ret.put("result", info);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}

	/**
	 * 更新关注次数
	 * 
	 * @param orgId
	 *            orgId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateAttendCount")
	public Map<String, Object> updateAttendCount(
			@RequestParam(required = true) String orgId) {
		try {

			Map<String, Object> ret = MapResult.initMap();
			organizationService.updateAttendCount(orgId);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}

	/**
	 * 更新报到次数
	 * 
	 * @param orgId
	 *            orgId
	 * @param type
	 *            1 报到 0 取消报到
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateCheckInCount")
	public Map<String, Object> updateCheckInCount(
			@RequestParam(required = true) String orgId,
			@RequestParam(required = true) String type) {
		try {

			Map<String, Object> ret = MapResult.initMap();
			organizationService.updateCheckInCount(orgId, type);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
			return MapResult.failMap();
		}
	}

}
