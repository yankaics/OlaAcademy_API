package com.kzsrm.model;


//报名信息
public class CheckInfo {
	
	private Integer id;

    private Integer orgId;

    private String userPhone;

    private String userLocal;

    private Integer type;  //报名方式
    
    private String checkinTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserLocal() {
		return userLocal;
	}

	public void setUserLocal(String userLocal) {
		this.userLocal = userLocal;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCheckinTime() {
		return checkinTime;
	}

	public void setCheckinTime(String checkinTime) {
		this.checkinTime = checkinTime;
	}

}
