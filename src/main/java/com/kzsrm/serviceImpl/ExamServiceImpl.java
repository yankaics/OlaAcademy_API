package com.kzsrm.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.ExaminationDao;
import com.kzsrm.dao.SubjectDao;
import com.kzsrm.dao.SubjectExamLogDao;
import com.kzsrm.model.Examination;
import com.kzsrm.model.Subject;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.ExamService;

@Service
@Transactional
public class ExamServiceImpl extends BaseServiceMybatisImpl<Examination, String> implements ExamService {
	@Resource
	private ExaminationDao<?> examinationDao;
	@Resource
	private SubjectDao<?> subjectDao;
	@Resource
	private SubjectExamLogDao<?> subjectExamLogDao;

	@Override
	protected EntityDao<Examination, String> getEntityDao() {
		return examinationDao;
	}

	/**
	 * 获取模考列表
	 * @param cid	课程id
	 * @return
	 */
	@Override
	public List<Examination> getListByCour(String cid, String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cid", cid);
		map.put("type", type);
		return examinationDao.getListByCour(map);
	}
	
	@Override
	public Integer getHasDoneProgress(int eid, String userid){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("eid", eid);
		map.put("userid", userid);
		List<Subject> subList = subjectDao.getSubjectByExam(eid+"");
		return subjectExamLogDao.getExamHasDoneSubNum(map)*100/subList.size();
	}

}
