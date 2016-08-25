package com.kzsrm.service;

import java.util.List;

import com.kzsrm.model.Keyword;

public interface KeywordService {
	
	/**
	 * 查询推荐的搜索关键词
	 * @return
	 */
	List<Keyword> getKeywordList();

}
