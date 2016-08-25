package com.kzsrm.serviceImpl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.SubjectExamLogDao;
import com.kzsrm.model.Option;
import com.kzsrm.model.Subject;
import com.kzsrm.model.SubjectExamLog;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.SubjectExamLogService;

@Service
@Transactional
public class SubjectExamLogServiceImpl extends BaseServiceMybatisImpl<SubjectExamLog, String> implements SubjectExamLogService {
	@Resource
	private SubjectExamLogDao<?> subjectExamLogDao;
	
	@Override
	protected EntityDao<SubjectExamLog, String> getEntityDao() {
		return subjectExamLogDao;
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
		List<SubjectExamLog> logList = subjectExamLogDao.getByParam(map);
		if (logList != null && logList.size() > 0 ) {
			SubjectExamLog slog = logList.get(0);
			slog.setIsright(opt.getIsanswer());
			slog.setOid(opt.getId()+"");
			subjectExamLogDao.update(slog);
		} else {
			SubjectExamLog slog = new SubjectExamLog();
			slog.setCreatetime(new Date());
			slog.setIsright(opt.getIsanswer());
			slog.setOid(opt.getId()+"");
			slog.setSid(sub.getId()+"");
			slog.setUserid(userId);
			subjectExamLogDao.save(slog);
		}
	}
	
}
