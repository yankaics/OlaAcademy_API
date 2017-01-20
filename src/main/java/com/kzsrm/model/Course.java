package com.kzsrm.model;

public class Course {
    private Integer id;

    private String type;

    private String name;

    private String profile;

    private String address;
    
    private String teacherId;

    private String pid;
    
    private Integer playcount;
    
    private Integer subAllNum;
    
    private String totalTime;
    
    private Integer isBanner;
    
    private String bannerPic;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile == null ? null : profile.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid == null ? null : pid.trim();
    }

	public Integer getPlaycount() {
		return playcount;
	}

	public void setPlaycount(Integer playcount) {
		this.playcount = playcount;
	}

	public Integer getSubAllNum() {
		return subAllNum;
	}

	public void setSubAllNum(Integer subAllNum) {
		this.subAllNum = subAllNum;
	}

	public String getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}

	public Integer getIsBanner() {
		return isBanner;
	}

	public void setIsBanner(Integer isBanner) {
		this.isBanner = isBanner;
	}

	public String getBannerPic() {
		return bannerPic;
	}

	public void setBannerPic(String bannerPic) {
		this.bannerPic = bannerPic;
	}
	
	

}