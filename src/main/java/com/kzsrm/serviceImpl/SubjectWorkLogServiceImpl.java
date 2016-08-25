package com.kzsrm.serviceImpl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.SubjectWorkLogDao;
import com.kzsrm.model.Option;
import com.kzsrm.model.Subject;
import com.kzsrm.model.SubjectWorkLog;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.SubjectWorkLogService;

@Service
@Transactional
public class SubjectWorkLogServiceImpl extends
		BaseServiceMybatisImpl<SubjectWorkLog, String> implements
		SubjectWorkLogService {
	@Resource
	private SubjectWorkLogDao<?> subjectWorkLogDao;

	@Override
	protected EntityDao<SubjectWorkLog, String> getEntityDao() {
		return subjectWorkLogDao;
	}

	/**
	 * 更新或保存答题记录
	 * 
	 * @param opt
	 * @param sub
	 * @param userId
	 */
	public void saveOrUpdate(Option opt, Subject sub, String userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", sub.getId());
		map.put("userid", userId);
		List<SubjectWorkLog> logList = subjectWorkLogDao.getByParam(map);
		if (logList != null && logList.size() > 0) {
			SubjectWorkLog slog = logList.get(0);
			slog.setIsright(opt.getIsanswer());
			slog.setOid(opt.getId() + "");
			subjectWorkLogDao.update(slog);
		} else {
			SubjectWorkLog slog = new SubjectWorkLog();
			slog.setCreatetime(new Date());
			slog.setIsright(opt.getIsanswer());
			slog.setOid(opt.getId() + "");
			slog.setSid(sub.getId() + "");
			slog.setUserid(userId);
			subjectWorkLogDao.save(slog);
		}
	}

}
