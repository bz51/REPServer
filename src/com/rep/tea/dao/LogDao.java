package com.rep.tea.dao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.rep.core.HibernateSessionFactory;
import com.rep.stu.entity.StuLoginLogEntity;
import com.rep.tea.entity.TeaDoLogEntity;
import com.rep.tea.entity.TeaLoginLogEntity;

public class LogDao {

	/**
	 * 获取教师登陆日志
	 * @param tea_name
	 * @param type
	 * @param endTime 
	 * @param startTime 
	 * @return
	 */
	public List<TeaLoginLogEntity> getTeaLoginLog(String tea_name, String type, String startTime, String endTime) {
		//将type值为2转成不限
		if("2".equals(type))
			type = "";
		if(tea_name==null)
			tea_name = "";
		
		
		//查询
		List<TeaLoginLogEntity> list = new ArrayList<TeaLoginLogEntity>();
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql;
			if(startTime.equals("0")&&endTime.equals("0")){
				hql = "select log from TeaLoginLogEntity log where type like:typeString and tea_name like:tea_nameString";
				list = session.createQuery(hql)
						.setString("typeString", "%"+type+"%")
						.setString("tea_nameString", "%"+tea_name+"%")
						.list();
			}else{
				hql = "select log from TeaLoginLogEntity log where type like:typeString and tea_name like:tea_nameString and time between :startTimeString and  :endTimeString";
				list = session.createQuery(hql)
						.setString("typeString", "%"+type+"%")
						.setString("tea_nameString", "%"+tea_name+"%")
						.setString("startTimeString", ""+startTime+" 00:00:00")
						.setString("endTimeString", ""+endTime+" 23:59:59")
						.list();
			}
			session.getTransaction().commit();
		} catch (HibernateException e) {
			return null;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		return list;
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
		//将type值为2转成不限
		if("2".equals(type))
			type = "";
		if(tea_name==null)
			tea_name = "";
		
		List<TeaDoLogEntity> list = new ArrayList<TeaDoLogEntity>();
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql;
			if(startTime.equals("0")&&endTime.equals("0")){
				hql = "select log from TeaDoLogEntity log where type like:typeString and tea_name like:tea_nameString";
				list = session.createQuery(hql)
						.setString("typeString", "%"+type+"%")
						.setString("tea_nameString", "%"+tea_name+"%")
						.list();
			}else{
				hql = "select log from TeaDoLogEntity log where type like:typeString and tea_name like:tea_nameString and time between :startTimeString and :endTimeString";
				list = session.createQuery(hql)
						.setString("typeString", "%"+type+"%")
						.setString("tea_nameString", "%"+tea_name+"%")
						.setString("startTimeString", ""+startTime+" 00:00:00")
						.setString("endTimeString", ""+endTime+" 23:59:59")
						.list();
			}
			session.getTransaction().commit();
		} catch (HibernateException e) {
			return null;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		return list;
	}


	/**
	 * 添加教师操作日志
	 * @param tea_id
	 * @param username
	 * @param type
	 * @param content
	 * @return
	 */
	public boolean addTeaDoLog(long tea_id, String username, int type,String content) {
		TeaDoLogEntity teaDoLogEntity = new TeaDoLogEntity();
		teaDoLogEntity.setTea_id(tea_id);
		teaDoLogEntity.setContent(content);
		teaDoLogEntity.setState(1);
		teaDoLogEntity.setTea_name(username);
		teaDoLogEntity.setType(type);
		teaDoLogEntity.setTime(new Timestamp(new Date().getTime()));
		
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			session.save(teaDoLogEntity);
			session.getTransaction().commit();
//			HibernateSessionFactory.closeSession();
		} catch (HibernateException e) {
			return false;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		return true;
	}


	/**
	 * 添加教师登陆日志
	 * @param id
	 * @param username
	 * @param type
	 */
	public void addTeaLoginLog(long id, String username, int type) {
		TeaLoginLogEntity teaLoginLogEntity = new TeaLoginLogEntity();
		teaLoginLogEntity.setTea_id(id);
		teaLoginLogEntity.setState(1);
		teaLoginLogEntity.setTea_name(username);
		teaLoginLogEntity.setType(type);
		teaLoginLogEntity.setTime(new Timestamp(new Date().getTime()));
		
		Session session = HibernateSessionFactory.getSession();
		session.beginTransaction();
		session.save(teaLoginLogEntity);
		session.getTransaction().commit();
		HibernateSessionFactory.closeSession();
		
	}


	/**
	 * 添加学生登陆日志
	 * @param id
	 * @param username
	 * @param type
	 */
	public void addStuLoginLog(long id, String username, int type) {
		StuLoginLogEntity teaLoginLogEntity = new StuLoginLogEntity();
		teaLoginLogEntity.setStu_id(id);;
		teaLoginLogEntity.setState(1);
		teaLoginLogEntity.setStu_name(username);
		teaLoginLogEntity.setType(type);
		teaLoginLogEntity.setTime(new Timestamp(new Date().getTime()));
		
		Session session = HibernateSessionFactory.getSession();
		session.beginTransaction();
		session.save(teaLoginLogEntity);
		session.getTransaction().commit();
		HibernateSessionFactory.closeSession();
	}

}
