package com.kzsrm.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kzsrm.baseservice.BaseServiceMybatisImpl;
import com.kzsrm.dao.HomeworkDao;
import com.kzsrm.dao.SubjectWorkLogDao;
import com.kzsrm.model.Homework;
import com.kzsrm.mybatis.EntityDao;
import com.kzsrm.service.HomeworkService;

@Service
@Transactional
public class HomeworkServiceImpl extends
		BaseServiceMybatisImpl<Homework, String> implements HomeworkService {
	@Resource
	private HomeworkDao<?> homeworkDao;
	@Resource
	private SubjectWorkLogDao<?> subjectWorkLogDao;

	@Override
	protected EntityDao<Homework, String> getEntityDao() {
		return homeworkDao;
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
