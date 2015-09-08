package com.rep.stu.dao;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.rep.core.HibernateSessionFactory;
import com.rep.stu.entity.StudentEntity;

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
//			String hql = "select count(*) from StudentEntity where username='"+username+"'";
			String hql = "select count(*) from StudentEntity where username=:usernameString";
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
	public StudentEntity signin(StudentEntity teaEntity) {
		//result初始化为yes
		teaEntity.setResult("yes");
		
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
//			String hql = "select tea from StudentEntity as tea where username='"+teaEntity.getUsername()+"' and password='"+teaEntity.getPassword()+"'";
			String hql = "select tea from StudentEntity as tea where username=:usernameString and password=:passwordString";
			teaEntity = (StudentEntity) session.createQuery(hql)
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
			teaEntity = new StudentEntity();
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
	public StudentEntity login(StudentEntity teaEntity) {
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
	 * 修改密码
	 * @param password
	 * @param phone
	 * @return
	 */
	public boolean modifyPass(String password, String phone) {
		int result;
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql = "update StudentEntity set password=:passwordString where phone=:phoneString";
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
			System.out.println("关闭资源……");
		}
		
		if(result==0)
			return false;
		return true;
	}

	
	/**
	 * 判断Phone是否存在
	 * @param phone
	 * @return
	 */
	public boolean existPhone(String phone) {
		long result;
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql = "select count(*) from StudentEntity where phone=:phoneString";
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
	
}
