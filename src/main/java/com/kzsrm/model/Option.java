package com.kzsrm.model;

public class Option {
    private Integer id;

    private Integer sid;

    private String type;

    private String content;

    private String isanswer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getIsanswer() {
        return isanswer;
    }

    public void setIsanswer(String isanswer) {
        this.isanswer = isanswer == null ? null : isanswer.trim();
    }
}