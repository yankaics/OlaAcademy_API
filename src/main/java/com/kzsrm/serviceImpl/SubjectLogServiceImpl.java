package com.kzsrm.serviceImpl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.SubjectLogDao;
import com.kzsrm.model.Option;
import com.kzsrm.model.Subject;
import com.kzsrm.model.SubjectLog;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.SubjectLogService;

@Service
@Transactional
public class SubjectLogServiceImpl extends BaseServiceMybatisImpl<SubjectLog, String> implements SubjectLogService {
	@Resource
	private SubjectLogDao<?> subjectLogDao;
	
	@Override
	protected EntityDao<SubjectLog, String> getEntityDao() {
		return subjectLogDao;
	}

	/**
	 * 更新或保存答题记录
	 * @param opt
	 * @param sub
	 * @param userId
	 */
	public void saveOrUpdate(Option opt, Subject sub, String userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", sub.getId());
		map.put("userid", userId);
		List<SubjectLog> logList = subjectLogDao.getByParam(map);
		if (logList != null && logList.size() > 0 ) {
			SubjectLog slog = logList.get(0);
			slog.setIsright(opt.getIsanswer());
			slog.setOid(opt.getId()+"");
			subjectLogDao.update(slog);
		} else {
			SubjectLog slog = new SubjectLog();
			slog.setCreatetime(new Date());
			slog.setIsright(opt.getIsanswer());
			slog.setOid(opt.getId()+"");
			slog.setSid(sub.getId()+"");
			slog.setUserid(userId);
			subjectLogDao.save(slog);
		}
	}
	
}
