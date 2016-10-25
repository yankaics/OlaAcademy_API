package com.kzsrm.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kzsrm.model.SubjectWorkLog;
import com.kzsrm.mybatis.BaseMybatisDao;

@Repository
public class SubjectWorkLogDao<E> extends
		BaseMybatisDao<SubjectWorkLog, String> {

	public String getMybatisMapperNamesapce() {
		return "com.kzsrm.model.SubjectWorkLogMapper";
	}

	public List<SubjectWorkLog> getByParam(Map<String, Object> param) {
		return this.getSqlSession().selectList(
				this.getMybatisMapperNamesapce() + ".getByParam", param);
	}

	// 统计某套作业 某个用户已经题数
	public Integer getWorkHasDoneSubNum(Map<String, Object> param) {
		return this.getSqlSession().selectOne(
				this.getMybatisMapperNamesapce() + ".getWorkHasDoneSubNum",
				param);
	}

	// 统计某套作业 所有群成员数以及已经题数
	public Map<String, Long> getWorkHasDoneSubNumByGroup(Map<String, Object> param) {
		return this.getSqlSession().selectOne(
				this.getMybatisMapperNamesapce()
						+ ".getWorkHasDoneSubNumByGroup", param);
	}

	// 统计某套作业 所有群成员已经正确题数
	public Integer getWorkRightSubNumByGroup(Map<String, Object> param) {
		return this.getSqlSession().selectOne(
				this.getMybatisMapperNamesapce()
						+ ".getWorkRightSubNumByGroup", param);
	}

}
