package com.kzsrm.model;

public class Examination {
    private Integer id;

    private Integer cid;

    private String name;
    
    private String source;

    private int learnNum;		
    
    private Integer target;

    private Integer degree;

    private Integer coverpoint;

    private String type;
    
    private int isfree;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public int getLearnNum() {
		return learnNum;
	}

	public void setLearnNum(int learnNum) {
		this.learnNum = learnNum;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }

    public Integer getDegree() {
        return degree;
    }

    public void setDegree(Integer degree) {
        this.degree = degree;
    }

    public Integer getCoverpoint() {
        return coverpoint;
    }

    public void setCoverpoint(Integer coverpoint) {
        this.coverpoint = coverpoint;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

	public int getIsfree() {
		return isfree;
	}

	public void setIsfree(int isfree) {
		this.isfree = isfree;
	}
    
}