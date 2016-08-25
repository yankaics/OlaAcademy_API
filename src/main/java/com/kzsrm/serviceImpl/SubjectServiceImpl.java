package com.kzsrm.serviceImpl;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.OptionDao;
import com.kzsrm.dao.SubjectDao;
import com.kzsrm.model.Option;
import com.kzsrm.model.Subject;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.SubjectExamLogService;
import com.kzsrm.service.SubjectLogService;
import com.kzsrm.service.SubjectService;
import com.kzsrm.service.SubjectWorkLogService;

@Service
@Transactional
public class SubjectServiceImpl extends BaseServiceMybatisImpl<Subject, String> implements SubjectService {
	@Resource private SubjectDao<?> subjectDao;
	@Resource private OptionDao<?> optionDao;
	
	@Resource private SubjectLogService subjectLogService;
	@Resource private SubjectExamLogService subjectExamLogService;
	@Resource private SubjectWorkLogService subjectWorkLogService;

	@Override
	protected EntityDao<Subject, String> getEntityDao() {
		return subjectDao;
	}
	/**
	 * 获取视频对应的测试题
	 * @param videoId
	 * @return
	 */
	@Override
	public List<Subject> getSubjectByVideo(String videoId) {
		List<Subject> subList = subjectDao.getSubjectByVideo(videoId);
		for (Subject sub : subList)
			sub.setOptionList(optionDao.getOptionBySubject(sub.getId()));
		return subList;
	}
	/**
	 * 校验测试题的答案
	 * @param userId
	 * @param answer
	 * @return
	 */
	@Override
	@SuppressWarnings({ "static-access", "rawtypes" })
	public JSONArray checkAnswer(String userId, String answer, String type) {
		JSONArray ret = new JSONArray();
		JSONArray _jAnswerList = new JSONArray().fromObject(answer);
		Iterator iter = _jAnswerList.iterator();
		while (iter.hasNext()) {
			JSONObject ele = new JSONObject();
			JSONObject _jAnswer = new JSONObject().fromObject(iter.next());
			String no = _jAnswer.get("no") + "";
			String optId = _jAnswer.get("optId") + "";
			
			Option opt = optionDao.getById(optId);
			Subject sub = subjectDao.getById(opt.getSid() + "");
			
			int ac = sub.getAllcount();
			sub.setAllcount(ac + 1);
			if ("1".equals(opt.getIsanswer()))
				sub.setRightcount(sub.getRightcount() + 1);
			subjectDao.update(sub);
			
			ele.put("no", no);
			ele.put("isRight", opt.getIsanswer());
			ele.put("degree", sub.getDegree());
			ele.put("avgAcc", (sub.getRightcount() * 100 / sub.getAllcount()) + "%");
			
			ret.add(ele);
			if (StringUtils.isNotBlank(userId))
				// 记录做题日志
				if ("1".equals(type))  //考点
					subjectLogService.saveOrUpdate(opt, sub, userId);
				else if ("2".equals(type)) // 模考
					subjectExamLogService.saveOrUpdate(opt, sub, userId);
				else if ("3".equals(type)) // 作业
					subjectWorkLogService.saveOrUpdate(opt, sub, userId);
		}
		return ret;
	}
	/**
	 * 获取知识点对应的测试题
	 * @param videoId
	 * @return
	 */
	@Override
	public List<Subject> getSubjectByPoint(String pointId) {
		List<Subject> subList = subjectDao.getSubjectByPoint(pointId);
		for (Subject sub : subList)
			sub.setOptionList(optionDao.getOptionBySubject(sub.getId()));
		return subList;
	}
	/**
	 * 获取知识点对应的测试题的数量
	 * @param videoId
	 * @return
	 */
	@Override
	public Integer getSubNumByPoint(String pointId) {
		return subjectDao.getSubNumByPoint(pointId);
	}
	/**
	 * 获取模考对应的测试题
	 * @param examId
	 * @return
	 */
	@Override
	public List<Subject> getSubjectByExam(String examId) {
		List<Subject> subList = subjectDao.getSubjectByExam(examId);
		for (Subject sub : subList)
			sub.setOptionList(optionDao.getOptionBySubject(sub.getId()));
		return subList;
	}
	/**
	 * 获取作业对应的测试题
	 * @param homeworkId
	 * @return
	 */
	@Override
	public List<Subject> getSubjectByHomework(String homeworkId){
		List<Subject> subList = subjectDao.getSubjectByHomework(homeworkId);
		for (Subject sub : subList)
			sub.setOptionList(optionDao.getOptionBySubject(sub.getId()));
		return subList;
	}
	/**
	 * 获取知识点对应的测试题的数量
	 * @param videoId
	 * @return
	 */
	@Override
	public Integer getSubNumByHomework(String homeworkId){
		return subjectDao.getSubNumByHomework(homeworkId);
	}

}
