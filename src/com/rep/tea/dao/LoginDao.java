package com.rep.tea.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.rep.core.HibernateSessionFactory;
import com.rep.tea.entity.SMSEntity;
import com.rep.tea.entity.TeacherEntity;

public class LoginDao {

	
	/**
	 * 判断该用户名是否已经被注册
	 * @param username
	 * @return
	 */
	public boolean isLogin(String username) {
		long result;
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql = "select count(*) from TeacherEntity where username=:usernameString";
			result = (Long) session.createQuery(hql)
					.setString("usernameString", username)
					.uniqueResult();
			session.getTransaction().commit();
//			HibernateSessionFactory.closeSession();
		} catch (HibernateException e) {
			result = -1;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		if(result!=0)
			return false;
		else
			return true;
	}

	/**
	 * 登陆－用户鉴权
	 * @param teaEntity
	 * @return
	 */
	public TeacherEntity signin(TeacherEntity teaEntity) {
		//result初始化为yes
		teaEntity.setResult("yes");
		
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql = "select tea from TeacherEntity as tea where username=:usernameString and password=:passwordString";
			teaEntity = (TeacherEntity) session.createQuery(hql)
					.setString("usernameString", teaEntity.getUsername())
					.setString("passwordString", teaEntity.getPassword())
					.uniqueResult();
			session.getTransaction().commit();
//			HibernateSessionFactory.closeSession();
			
		} catch (HibernateException e) {
			//username＋password存在多条
			teaEntity.setResult("no");
			teaEntity.setReason("该用户存在多个");
			return teaEntity;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		//username＋password不存在
		if(teaEntity==null){
			teaEntity = new TeacherEntity();
			teaEntity.setResult("no");
			teaEntity.setReason("用户名或密码不正确");
		}else{
			// 账户还未激活
			if (teaEntity.getState() == 0) {
				teaEntity.setResult("no");
				teaEntity.setReason("账号尚未激活");
			}
		}
		return teaEntity;
	}

	
	/**
	 * 注册
	 * @param teaEntity
	 * @return
	 */
	public TeacherEntity login(TeacherEntity teaEntity) {
		//设置注册时间、激活状态、教师类型
		teaEntity.setTime(new Timestamp(new Date().getTime()));
		teaEntity.setState(0);
		teaEntity.setType(0);
		
		//将用户信息存入数据库
		long tea_id;
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			tea_id = (long) session.save(teaEntity);
			session.getTransaction().commit();
//			HibernateSessionFactory.closeSession();
		} catch (HibernateException e) {
			teaEntity.setResult("no");
			teaEntity.setReason("出现未知异常");
			return teaEntity;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		//注册成功
		teaEntity.setResult("yes");
		teaEntity.setId(tea_id);
		return teaEntity;
	}

	
	/**
	 * 判断该用户tea_id和phone是否匹配
	 * @param tea_id
	 * @param phone
	 * @return
	 */
	public boolean matchPhone(String tea_id, String phone) {
		long count;
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql = "select count(*) from TeacherEntity where id=:idString and phone=:phoneString";
			count = (Long) session.createQuery(hql)
					.setString("idString", tea_id)
					.setString("phoneString", phone)
					.uniqueResult();
			session.getTransaction().commit();
//			HibernateSessionFactory.closeSession();
		} catch (HibernateException e) {
			return false;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		if(count!=1)
			return false;
		
		return true;
	}

	
	/**
	 * 修改密码
	 * @param password
	 * @param tea_id
	 * @return
	 */
	public boolean modifyPass(String password, String phone) {
		int result;
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql = "update TeacherEntity set password=:passwordString where phone=:phoneString";
			result = session.createQuery(hql)
			.setString("passwordString", password)
			.setString("phoneString", phone)
			.executeUpdate();
			session.getTransaction().commit();
//			HibernateSessionFactory.closeSession();
		} catch (HibernateException e) {
			return false;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		if(result==0)
			return false;
		
		return true;
	}

	
	/**
	 * 判断phone是否存在
	 * @param phone
	 * @return
	 */
	public boolean existPhone(String phone) {
		long result;
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql = "select count(*) from TeacherEntity where phone=:phoneString";
			result = (Long) session.createQuery(hql)
					.setString("phoneString", phone)
					.uniqueResult();
			session.getTransaction().commit();
//			HibernateSessionFactory.closeSession();
		} catch (HibernateException e) {
			result = -1;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		if(result!=0)
			return false;
		else
			return true;
	}

	
	/**
	 * 将验证码和手机号填入表SMS中
	 * @param phone
	 * @param code
	 * @return
	 */
	public boolean sendSMS(String phone, String code) {
		SMSEntity entity = new SMSEntity();
		entity.setCode(code);
		entity.setPhone(phone);
		entity.setTime(new Timestamp(new Date().getTime()));
		entity.setState(1);
		long id;
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			id = (long) session.save(entity);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			return false;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		return true;
	}

	
	/**
	 * 获取所有未发送的验证码(Android客户端使用)
	 * @return
	 */
	public List<SMSEntity> getAllUnSendAuthCode() {
		List<SMSEntity> list = new ArrayList<SMSEntity>();
		
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql = "select s from SMSEntity s where state=1";
			list =  session.createQuery(hql).list();
			session.getTransaction().commit();
		} catch (HibernateException e) {
			return null;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		return list;
	}

	
	/**
	 * 将某一条验证码设为已发送(Android客户端使用)
	 * @param id
	 * @return
	 */
	public boolean setHasSend(String id) {
		int result;
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql = "update SMSEntity set state=0 where id="+id;
			result = session.createQuery(hql).executeUpdate();
			session.getTransaction().commit();
		} catch (HibernateException e) {
			return false;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		if(result==0)
			return false;
		return true;
	}
	
}
