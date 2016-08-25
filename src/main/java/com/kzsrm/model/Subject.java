package com.kzsrm.model;

import java.util.List;

public class Subject {
    private Integer id;
    
    private String article;

    private String question;

    private String type;

    private Integer degree;

    private String hint;

    private Integer allcount;

    private Integer rightcount;

    private Integer avgtime;
    
    private String pic;
    
    private String hintpic;
    
    private String videourl;
    
    private List<Option> optionList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question == null ? null : question.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Integer getDegree() {
        return degree;
    }

    public void setDegree(Integer degree) {
        this.degree = degree;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint == null ? null : hint.trim();
    }

	public Integer getAllcount() {
		return allcount == null ? 0 : allcount;
	}

	public void setAllcount(Integer allcount) {
		this.allcount = allcount;
	}

	public Integer getRightcount() {
		return rightcount == null ? 0 : rightcount;
	}

	public void setRightcount(Integer rightcount) {
		this.rightcount = rightcount;
	}

	public Integer getAvgtime() {
		return avgtime == null ? 0 : avgtime;
	}

	public void setAvgtime(Integer avgtime) {
		this.avgtime = avgtime;
	}

	public List<Option> getOptionList() {
		return optionList;
	}

	public void setOptionList(List<Option> optionList) {
		this.optionList = optionList;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getHintpic() {
		return hintpic;
	}

	public void setHintpic(String hintpic) {
		this.hintpic = hintpic;
	}

	public String getVideourl() {
		return videourl;
	}

	public void setVideourl(String videourl) {
		this.videourl = videourl;
	}

}