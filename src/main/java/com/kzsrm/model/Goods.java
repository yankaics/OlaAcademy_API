package com.kzsrm.model;

import java.util.Date;

public class Goods {
    private Integer id;

    private String name;

    private Long price;

    private String detail;

    private Integer attentionnum;

    private Integer paynum;

    private Integer status;

    private String type;

    private String url;

    private String image;

    private String org;

    private String suitto;

    private String leanstage;

    private Integer videonum;

    private Integer totaltime;
    
    private Date createTime;

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
        this.name = name == null ? null : name.trim();
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    public Integer getAttentionnum() {
        return attentionnum;
    }

    public void setAttentionnum(Integer attentionnum) {
        this.attentionnum = attentionnum;
    }

    public Integer getPaynum() {
        return paynum;
    }

    public void setPaynum(Integer paynum) {
        this.paynum = paynum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org == null ? null : org.trim();
    }

    public String getSuitto() {
        return suitto;
    }

    public void setSuitto(String suitto) {
        this.suitto = suitto == null ? null : suitto.trim();
    }

    public String getLeanstage() {
        return leanstage;
    }

    public void setLeanstage(String leanstage) {
        this.leanstage = leanstage == null ? null : leanstage.trim();
    }

    public Integer getVideonum() {
        return videonum;
    }

    public void setVideonum(Integer videonum) {
        this.videonum = videonum;
    }

    public Integer getTotaltime() {
        return totaltime;
    }

    public void setTotaltime(Integer totaltime) {
        this.totaltime = totaltime;
    }

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
    
}