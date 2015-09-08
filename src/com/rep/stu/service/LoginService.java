package com.rep.stu.service;

import com.rep.stu.dao.LoginDao;
import com.rep.stu.entity.StudentEntity;

public class LoginService {
	private LoginDao dao = new LoginDao();

	/**
	 * 判断该用户名是否已经被注册
	 * @param username
	 * @return
	 */
	public boolean isLogin(String username) {
		return dao.isLogin(username);
	}

	/**
	 * 登陆－用户鉴权
	 * @param teaEntity
	 * @return
	 */
	public StudentEntity signin(StudentEntity teaEntity) {
		return dao.signin(teaEntity);
	}

	
	/**
	 * 注册
	 * @param teaEntity
	 * @return
	 */
	public StudentEntity login(StudentEntity teaEntity) {
		//判断username是否已被注册
		if(!dao.isLogin(teaEntity.getUsername())){
			teaEntity.setResult("no");
			teaEntity.setReason("该用户名已经被注册");
			return teaEntity;
		}
		
		//判断phone是否存在
		if(!dao.existPhone(teaEntity.getPhone())){
			teaEntity.setResult("no");
			teaEntity.setReason("该号码已经存在");
			return teaEntity;
		}
		
		else{//进行注册
			return dao.login(teaEntity);
		}
	}

	
	/**
	 * 修改密码
	 * @param password
	 * @param phone
	 * @return
	 */
	public boolean modifyPass(String password, String phone) {
		return dao.modifyPass(password,phone);
	}
	
	
}
