package com.rep.tea.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rep.stu.entity.StudentEntity;
import com.rep.tea.dao.AccountDao;
import com.rep.tea.entity.TeacherEntity;

public class AccountService {
	private AccountDao dao = new AccountDao();
	private LogService logService = new LogService();
	
	/**
	 * 获取未激活教师列表
	 * @return
	 */
	public List<TeacherEntity> getInactiveTeas() {
		return dao.getInactiveTeas();
	}
	
	
	/**
	 *  获取未激活学生列表
	 * @return
	 */
	public List<StudentEntity> getInactiveStus() {
		return dao.getInactiveStus();
	}


	/**
	 * 认证教师
	 * @param tea_id
	 * @return
	 */
	public boolean authorTea(String tea_id,String tea_name,String user_id,String user_name) {
		//进行认证
		boolean result = dao.authorTea(tea_id);
		
		//添加操作日志
		if(result){
			logService.addTeaDoLog(user_id, user_name+"认证通过了教师账户:"+tea_name);
		}
		
		return result;
	}


	/**
	 * 认证学生
	 * @param stu_id
	 * @return
	 */
	public boolean authorStu(String stu_id,String stu_name,String user_id,String user_name) {
		//进行认证
		boolean result = dao.authorStu(stu_id);
		
		//添加操作日志
		if(result){
			logService.addTeaDoLog(user_id, user_name+"认证通过了学生账户:"+stu_name);
		}
		
		return result;
	}


	/**
	 * 搜索教师信息
	 * @param type
	 * @param state
	 * @param tea_name
	 * @return
	 */
	public List<TeacherEntity> searchTea(String type, String state,
			String tea_name) {
		return dao.searchTea(type,state,tea_name);
	}


	/**
	 * 搜索学生信息
	 * @param type
	 * @param state
	 * @param tea_name
	 * @return
	 */
	public List<StudentEntity> searchStu(String type, String state,
			String stu_name) {
		return dao.searchStu(type,state,stu_name);
	}


	/**
	 * 修改教师账户信息
	 * @param tea_id
	 * @param tea_name
	 * @param type
	 * @param state
	 * @return
	 */
	public boolean updateTea(String tea_id, String tea_name, String type,String state,String user_id,String user_name) {
		//进行修改
		boolean result = dao.updateTea(tea_id,tea_name,type,state);

		//添加操作日志
		if (result) {
			logService.addTeaDoLog(user_id, user_name + "修改了教师账户:" + tea_name);
		}

		return result;
	}


	/**
	 * 修改学生账户信息
	 * @param stu_id
	 * @param stu_name
	 * @param type
	 * @param state
	 * @return
	 */
	public boolean updateStu(String stu_id, String stu_name, String type,String state,String user_id,String user_name) {
		//进行修改
		boolean result = dao.updateStu(stu_id,stu_name,type,state);

		// 添加操作日志
		if (result) {
			logService.addTeaDoLog(user_id, user_name + "修改了学生账户:" + stu_name);
		}

		return result;
	}
	
	
	/**
	 * 根据tea_id获取该教师的基本信息
	 * @param tea_id
	 * @return
	 */
	public TeacherEntity getTeacherById(long tea_id){
		if(tea_id<=0)
			return null;
		
		return dao.getTeacherById(tea_id);
	}


	/**
	 * 批量认证全部教师
	 * @param user_id
	 * @param user_name
	 * @return
	 */
	public boolean authorTeaAll(String user_id, String user_name) {
		// 进行认证
		boolean result = dao.authorTeaAll();

		// 添加操作日志
		if (result) {
			logService.addTeaDoLog(user_id, user_name + "批量认证所有教师账户");
		}

		return result;
	}


	/**
	 * 批量认证所有学生账户
	 * @param user_id
	 * @param user_name
	 * @return
	 */
	public boolean authorStuAll(String user_id, String user_name) {
		// 进行认证
		boolean result = dao.authorStuAll();

		// 添加操作日志
		if (result) {
			logService.addTeaDoLog(user_id, user_name + "批量认证所有学生账户");
		}

		return result;
	}


	
	/**
	 * 将ids和names且分
	 * @param tea_id
	 * @param tea_name
	 * @return
	 */
	public Map<String, String> splitIdsAndNames(String ids, String names) {
		System.out.println("ids:"+ids+",names:"+names);
		String[] id = ids.split("\\.");
		String[] name = names.split("\\.");
		
		System.out.println("ids:"+id.length+",names:"+name.length);
		
		//如果ids和names数量不匹配
		if(id.length!=name.length)
			return null;
		
		//将id和name放入map
		Map<String,String> map = new HashMap<String,String>();
		for(int i=0;i<id.length;i++){
			map.put(id[i], name[i]);
		}
		return map;
	}


	/**
	 * 认证某些教师账户
	 * @param map
	 * @param user_id
	 * @param user_name
	 * @return
	 */
	public boolean authorSomeTea(Map<String, String> map, String user_id,String user_name) {
		//进行认证
		boolean result = dao.authorSomeTea(map,user_id,user_name);
		
		// 添加操作日志
		if (result) {
			Collection<String> names = map.values();
			logService.addTeaDoLog(user_id, user_name + "批量认证了以下教师账户:"+names.toString());
		}
		
		return result;
	}


	
	/**
	 * 认证多个学生账户
	 * @param map
	 * @param user_id
	 * @param user_name
	 * @return
	 */
	public boolean authorSomeStu(Map<String, String> map, String user_id,String user_name) {
		//进行认证
		boolean result = dao.authorSomeStu(map,user_id,user_name);
		
		// 添加操作日志
		if (result) {
			Collection<String> names = map.values();
			logService.addTeaDoLog(user_id, user_name + "批量认证了以下学生账户:"+names.toString());
		}
		
		return result;
	}
}
