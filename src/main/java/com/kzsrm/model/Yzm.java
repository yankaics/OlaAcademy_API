package com.kzsrm.model;

import java.util.Date;

/**
 * 验证码表
 * 
 * @author wwy
 *
 */
public class Yzm {
	private int id;
	private String phone;
	private String email;
	private String yzm;
	private Date regtime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getYzm() {
		return yzm;
	}

	public void setYzm(String yzm) {
		this.yzm = yzm;
	}

	public Date getRegtime() {
		return regtime;
	}

	public void setRegtime(Date regtime) {
		this.regtime = regtime;
	}

	@Override
	public String toString() {
		return "[id=" + id + ", phone=" + phone + ", email=" + email + ", yzm=" + yzm + ", regtime=" + regtime
				+ "]";
	}

}
