package com.kzsrm.serviceImpl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.OptionDao;
import com.kzsrm.model.Option;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.OptionService;

@Service
@Transactional
public class OptionServiceImpl extends BaseServiceMybatisImpl<Option, String> implements OptionService {
	@Resource
	private OptionDao<?> optionDao;

	@Override
	protected EntityDao<Option, String> getEntityDao() {
		return optionDao;
	}

	/**
	 * 获取试题答案
	 * @param subjectId
	 * @return
	 */
	public Option getSubjectAnswer(String subjectId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", subjectId);
		map.put("isanswer", 1);
		return optionDao.getRightOptionBySubject(map);
	}
}
