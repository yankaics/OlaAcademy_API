package com.kzsrm.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

/**
 * 
 * @author wwy
 *
 */
public class User {
	private int id;
	private String name;
	private String sex;
	private int age;
	private String phone;
	private String passwd;
	private String email;
	@DateTimeFormat(iso = ISO.DATE_TIME, pattern = "yyyy-MM-dd")
	private Date birthday;
	private String qq;
	private String local;
	private Date regtime;
	private String avator;
	private String sign;
	private String level;
	private String honor;
	private String status;
	private String examtype;
	private String yzm;
	private int isActive;
	private String realName;
	private String coin;
	private Date vipTime;
	private Date signIntime;  //上次签到时间
	private String signIndays; // 连续签到天数
	private String learntime;  //上次学习时间
	private String learndays; // 累计学习天数 允许间断

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public String getYzm() {
		return yzm;
	}

	public void setYzm(String yzm) {
		this.yzm = yzm;
	}

	public String getHonor() {
		return honor;
	}

	public void setHonor(String honor) {
		this.honor = honor;
	}

	public String getExamtype() {
		return examtype;
	}

	public void setExamtype(String examtype) {
		this.examtype = examtype;
	}

	public String getAvator() {
		return avator;
	}

	public void setAvator(String avator) {
		this.avator = avator;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public Date getRegtime() {
		return regtime;
	}

	public void setRegtime(Date regtime) {
		this.regtime = regtime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCoin() {
		return coin;
	}

	public void setCoin(String coin) {
		this.coin = coin;
	}

	public Date getVipTime() {
		return vipTime;
	}

	public void setVipTime(Date vipTime) {
		this.vipTime = vipTime;
	}
	
	public Date getSignIntime() {
		return signIntime;
	}

	public void setSignIntime(Date signIntime) {
		this.signIntime = signIntime;
	}

	public String getSignIndays() {
		return signIndays;
	}

	public void setSignIndays(String signIndays) {
		this.signIndays = signIndays;
	}

	public String getLearntime() {
		return learntime;
	}

	public void setLearntime(String learntime) {
		this.learntime = learntime;
	}

	public String getLearndays() {
		return learndays;
	}

	public void setLearndays(String learndays) {
		this.learndays = learndays;
	}

	@Override
	public String toString() {
		return "[id=" + id + ", name=" + name + ", sex=" + sex + ", age=" + age + ", phone=" + phone + ", passwd="
				+ passwd + ", email=" + email + ", birthday=" + birthday + ", qq=" + qq + ", sign=" + sign
				+ ", regtime=" + regtime + ", signIntime=" + signIntime + ", status=" + status + ", avator=" + avator
				+ ", level=" + level + ", yzm=" + yzm + ", honor=" + honor + ", signIndays=" + signIndays + ", examtype="
				+ examtype + ", isActive="+isActive+",realName="+realName+",coin="+coin+"]";
	}
}
