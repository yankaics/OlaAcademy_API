package com.kzsrm.model;

import java.util.Date;

public class Comment {
	private int id;
	private int userId;
	private int postId; // courseId 或 olacircle中的帖子Id
	private int type; // 1 针对course的评论 2针对帖子的评论 
	private String toUserId; //被回复人Id
	private String content;
	private String imageIds;
	private String videoUrls;
	private String videoImgs;
	private String audioUrls;
	private String location;
	private Date createTime;
	private int praiseNumber;
	private int isRead;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getPostId() {
		return postId;
	}
	public void setPostId(int postId) {
		this.postId = postId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getToUserId() {
		return toUserId;
	}
	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImageIds() {
		return imageIds;
	}
	public void setImageIds(String imageIds) {
		this.imageIds = imageIds;
	}
	public String getVideoUrls() {
		return videoUrls;
	}
	public void setVideoUrls(String videoUrls) {
		this.videoUrls = videoUrls;
	}
	public String getVideoImgs() {
		return videoImgs;
	}
	public void setVideoImgs(String videoImgs) {
		this.videoImgs = videoImgs;
	}
	public String getAudioUrls() {
		return audioUrls;
	}
	public void setAudioUrls(String audioUrls) {
		this.audioUrls = audioUrls;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getPraiseNumber() {
		return praiseNumber;
	}
	public void setPraiseNumber(int praiseNumber) {
		this.praiseNumber = praiseNumber;
	}
	public int getIsRead() {
		return isRead;
	}
	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}
}
