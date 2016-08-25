package com.kzsrm.model;

import java.util.Date;

public class SubjectWorkLog {

	private Integer id;

	private String sid;

	private String userid;

	private String isright;

	private String oid;

	private Date createtime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getIsright() {
		return isright;
	}

	public void setIsright(String isright) {
		this.isright = isright;
	}
}