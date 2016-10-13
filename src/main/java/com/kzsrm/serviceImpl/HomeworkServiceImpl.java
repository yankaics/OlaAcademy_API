package com.kzsrm.serviceImpl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.HomeworkDao;
import com.kzsrm.dao.HomeworkSubjectDao;
import com.kzsrm.dao.SubjectWorkLogDao;
import com.kzsrm.model.Homework;
import com.kzsrm.model.HomeworkSubject;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.HomeworkService;

@Service
@Transactional
public class HomeworkServiceImpl extends
		BaseServiceMybatisImpl<Homework, String> implements HomeworkService {
	@Resource
	private HomeworkDao<?> homeworkDao;
	@Resource
	private HomeworkSubjectDao<?> homeworkSubjectDao;
	@Resource
	private SubjectWorkLogDao<?> subjectWorkLogDao;

	@Override
	protected EntityDao<Homework, String> getEntityDao() {
		return homeworkDao;
	}
	
	/**
	 * 发布作业
	 * 
	 * @return
	 */
	@Override
	public void createHomework(String groupId, String name, String subjectIds) {
		String[] subjectIdArray = subjectIds.split(",");
		Date now = new Date();
		Homework homework = new Homework();
		homework.setGroupId(groupId);
		homework.setName(name);
		homework.setCreateTime(now);
		homeworkDao.save(homework);
		// sql实现插入是获取Id
		int homeworkId = homework.getId();
		for(String subjectId : subjectIdArray){
			if(!TextUtils.isEmpty(subjectId)){
				HomeworkSubject workSub = new HomeworkSubject();
				workSub.setHomeworkId(homeworkId);
				workSub.setSubjectId(Integer.parseInt(subjectId));
				workSub.setCreateTime(now);
				homeworkSubjectDao.save(workSub);
			}
		}
	}

	/**
	 * 作业列表
	 * 
	 * @return
	 */
	@Override
	public List<Homework> getHomeworkList(String userId, String homeworkId, int pageCount,int type) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(homeworkId)) {
			Homework homework = homeworkDao.getById(homeworkId);
			if (homework != null) {
				map.put("createTime", homework.getCreateTime());
			}
		}
		map.put("userId", userId);
		map.put("pageCount", pageCount);
		if(type==2){
			return homeworkDao.getTeacherHomeworkList(map);
		}else{
			return homeworkDao.getStudentHomeworkList(map);
		}
	}
	
	/**
	 * 已答题数量
	 * 
	 * @return
	 */
	@Override
	public Integer getHasDoneSubNum(int homeworkId, String userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("homeworkId", homeworkId);
		map.put("userId", userId);
		return subjectWorkLogDao.getWorkHasDoneSubNum(map);
	}

}
