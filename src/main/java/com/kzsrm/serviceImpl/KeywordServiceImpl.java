package com.kzsrm.serviceImpl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.KeywordDao;
import com.kzsrm.model.Keyword;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.KeywordService;

@Service
@Transactional
public class KeywordServiceImpl extends BaseServiceMybatisImpl<Keyword, String> implements KeywordService {
	@Resource
	private KeywordDao<?> keywordDao;

	@Override
	protected EntityDao<Keyword, String> getEntityDao() {
		return keywordDao;
	}

	/**
	 * 查询推荐的搜索关键词
	 * @param pid
	 * @param type
	 * @return
	 */
	@Override
	public List<Keyword> getKeywordList() {
		return keywordDao.getKeywordList();
	}

}

