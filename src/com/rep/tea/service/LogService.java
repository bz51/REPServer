package com.rep.tea.service;

import java.util.List;

import com.rep.tea.dao.LogDao;
import com.rep.tea.entity.TeaDoLogEntity;
import com.rep.tea.entity.TeaLoginLogEntity;
import com.rep.tea.entity.TeacherEntity;

public class LogService {
	private LogDao dao = new LogDao();
	
	/**
	 * 获取教师登陆日志
	 * @param tea_name
	 * @param type
	 * @param endTime 
	 * @param startTime 
	 * @return
	 */
	public List<TeaLoginLogEntity> getTeaLoginLog(String tea_name, String type, String startTime, String endTime) {
		return dao.getTeaLoginLog(tea_name,type,startTime,endTime);
	}

	
	/**
	 * 获取教师操作日志
	 * @param tea_name
	 * @param type
	 * @param endTime 
	 * @param startTime 
	 * @return
	 */
	public List<TeaDoLogEntity> getTeaDoLog(String tea_name, String type, String startTime, String endTime) {
		return dao.getTeaDoLog(tea_name,type,startTime,endTime);
	}


	/**
	 * 添加教师操作日志
	 * @param tea_id
	 * @param content
	 * @return
	 */
	public boolean addTeaDoLog(String tea_id, String content) {
		//根据tea_id搜索该教师的基本信息
		AccountService service = new AccountService();
		TeacherEntity teacherEntity = service.getTeacherById(Long.parseLong(tea_id));
		
		//若获取teacherEntity失败
		if(teacherEntity==null)
			return false;
		
		//若获取teacherEntity成功，则添加该教师的操作日志
		return dao.addTeaDoLog(teacherEntity.getId(),teacherEntity.getUsername(),teacherEntity.getType(),content);
	}


	/**
	 * 添加教师登陆日志
	 * @param id
	 * @param username
	 * @param type
	 */
	public void addTeaLoginLog(long id, String username, int type) {
		dao.addTeaLoginLog(id,username,type);
	}


	/**
	 * 添加学生登陆日志
	 * @param id
	 * @param username
	 * @param type
	 */
	public void addStuLoginLog(long id, String username, int type) {
		dao.addStuLoginLog(id,username,type);
	}

}
