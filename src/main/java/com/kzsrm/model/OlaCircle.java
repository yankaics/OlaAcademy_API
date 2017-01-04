package com.kzsrm.model;

import java.util.Date;

public class OlaCircle {
	
	private Integer id;
	private Integer userId;
	private String title;
	private String content;
	private String imageGids;
	private String location;
	private Integer courseId;
	private Integer videoId;
	private Integer praiseNumber;
	private Integer readNumber;
	private Integer commentNumber;
	private Integer type;
	private String assignUser; //指定回答用户ID
	private Integer isPublic; // 是否公开
	private Date createTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImageGids() {
		return imageGids;
	}
	public void setImageGids(String imageGids) {
		this.imageGids = imageGids;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Integer getCourseId() {
		return courseId;
	}
	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}
	public Integer getVideoId() {
		return videoId;
	}
	public void setVideoId(Integer videoId) {
		this.videoId = videoId;
	}
	public Integer getPraiseNumber() {
		return praiseNumber;
	}
	public void setPraiseNumber(Integer praiseNumber) {
		this.praiseNumber = praiseNumber;
	}
	public Integer getReadNumber() {
		return readNumber;
	}
	public void setReadNumber(Integer readNumber) {
		this.readNumber = readNumber;
	}
	public Integer getCommentNumber() {
		return commentNumber;
	}
	public void setCommentNumber(Integer commentNumber) {
		this.commentNumber = commentNumber;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getAssignUser() {
		return assignUser;
	}
	public void setAssignUser(String assignUser) {
		this.assignUser = assignUser;
	}
	public Integer getIsPublic() {
		return isPublic;
	}
	public void setIsPublic(Integer isPublic) {
		this.isPublic = isPublic;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
