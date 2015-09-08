package com.rep.tea.service;

import java.util.List;

import com.rep.tea.dao.LoginDao;
import com.rep.tea.entity.SMSEntity;
import com.rep.tea.entity.TeacherEntity;

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
	public TeacherEntity signin(TeacherEntity teaEntity) {
		return dao.signin(teaEntity);
	}

	
	/**
	 * 注册
	 * @param teaEntity
	 * @return
	 */
	public TeacherEntity login(TeacherEntity teaEntity) {
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
		
		//进行注册
		else{
			return dao.login(teaEntity);
		}
	}

	
	/**
	 * 判断该用户tea_id和phone是否匹配
	 * @param tea_id
	 * @param phone
	 * @return
	 */
	public boolean matchPhone(String tea_id, String phone) {
		return dao.matchPhone(tea_id,phone);
	}

	
	/**
	 * 修改密码
	 * @param password
	 * @param tea_id
	 * @return
	 */
	public boolean modifyPass(String password, String phone) {
		return dao.modifyPass(password,phone);
	}

	
	/**
	 * 向该手机发送验证码
	 * @param phone
	 * @param code
	 * @return
	 */
	public boolean sendSMS(String phone, String code) {
		return dao.sendSMS(phone,code);
	}

	
	/**
	 * 获取所有未发送的验证码(Android客户端使用)
	 * @return
	 */
	public List<SMSEntity> getAllUnSendAuthCode() {
		return dao.getAllUnSendAuthCode();
	}

	
	/**
	 * 将某一条验证码设为已发送(Android客户端使用)
	 * @param id
	 * @return
	 */
	public boolean setHasSend(String id) {
		return dao.setHasSend(id);
	}
	
	
}
