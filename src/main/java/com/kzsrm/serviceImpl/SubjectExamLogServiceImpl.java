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
	 * 更新或保存答题记录(答题自动记录)
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
	
	/**
	 * 更新错题本（手动添加删除）
	 * @param subjectId
	 * @param type 1 添加错题 2 移除错题
	 * @return
	 */
	@Override
	public void updateWrongSet(String userId,String subjectId, int type) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userid", userId);
		map.put("sid", subjectId);
		if(type==1){
			List <SubjectExamLog> logList = subjectExamLogDao.getByParam(map);
			if(logList.size()>0){
				SubjectExamLog subjectLog = logList.get(0);
				subjectLog.setOid("0");
				subjectLog.setIsright("0");
				subjectExamLogDao.update(subjectLog);
			}else{
				SubjectExamLog subjectLog = new SubjectExamLog();
				subjectLog.setUserid(userId);
				subjectLog.setSid(subjectId);
				subjectLog.setOid("0");
				subjectLog.setIsright("0");
				subjectLog.setCreatetime(new Date());
				subjectExamLogDao.save(subjectLog);
			}
		}else if(type==2){
			List <SubjectExamLog> logList = subjectExamLogDao.getByParam(map);
			for(SubjectExamLog subjectLog : logList){
				subjectExamLogDao.deleteById(subjectLog.getId()+"");
			}
		}
	}
	
}
