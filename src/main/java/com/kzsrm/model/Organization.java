package com.kzsrm.model;

public class Organization {
	
	private Integer id;
	
	private String name;
	
	private String org;
	
	private String logo;

    private Integer type;  // 1:address为图片地址  2:address为视频地址

    private String profile;

    private String address;  // 视频或图片地址

    private Integer checkinCount; //报名数
    
    private Integer attendCount; //关注数

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getCheckinCount() {
		return checkinCount;
	}

	public void setCheckinCount(Integer checkinCount) {
		this.checkinCount = checkinCount;
	}

	public Integer getAttendCount() {
		return attendCount;
	}

	public void setAttendCount(Integer attendCount) {
		this.attendCount = attendCount;
	}
}
