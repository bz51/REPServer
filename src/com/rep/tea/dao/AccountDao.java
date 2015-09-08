package com.rep.tea.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.rep.core.HibernateSessionFactory;
import com.rep.stu.entity.StudentEntity;
import com.rep.tea.entity.TeacherEntity;

public class AccountDao {

	/**
	 * 获取未激活教师列表
	 * @return
	 */
	public List<TeacherEntity> getInactiveTeas() {
		List<TeacherEntity> list = new ArrayList<TeacherEntity>();
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql = "select tea from TeacherEntity tea where state=0";
			list = session.createQuery(hql).list();
			session.getTransaction().commit();
//			HibernateSessionFactory.closeSession();
		} catch (HibernateException e) {
			//查询发生异常
			return null;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		return list;
	}

	
	/**
	 * 获取未激活学生列表
	 * @return
	 */
	public List<StudentEntity> getInactiveStus() {
		List<StudentEntity> list = new ArrayList<StudentEntity>();
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql = "select stu from StudentEntity stu where state=0";
			list = session.createQuery(hql).list();
			session.getTransaction().commit();
//			HibernateSessionFactory.closeSession();
		} catch (HibernateException e) {
			//查询发生异常
			return null;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		return list;
	}


	/**
	 * 认证教师
	 * @param tea_id
	 * @return
	 */
	public boolean authorTea(String tea_id) {
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql = "update TeacherEntity set state=1 where id=:tea_idString";
			session.createQuery(hql)
			.setString("tea_idString", tea_id)
			.executeUpdate();
			session.getTransaction().commit();
//			HibernateSessionFactory.closeSession();
		} catch (HibernateException e) {
			//查询发生异常
			return false;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		return true;
	}
	
	
	/**
	 * 认证学生
	 * @param stu_id
	 * @return
	 */
	public boolean authorStu(String stu_id) {
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql = "update StudentEntity set state=1 where id=:stu_idString";
			session.createQuery(hql)
			.setString("stu_idString", stu_id)
			.executeUpdate();
			session.getTransaction().commit();
//			HibernateSessionFactory.closeSession();
		} catch (HibernateException e) {
			//查询发生异常
			return false;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		return true;
	}


	/**
	 * 搜索教师信息
	 * @param type
	 * @param state
	 * @param tea_name
	 * @return
	 */
	public List<TeacherEntity> searchTea(String type, String state,String tea_name) {
		//将type、state值为2转成不限
		if("2".equals(type))
			type = "";
		if("3".equals(state))
			state = "";
		if(tea_name==null)
			tea_name = "";
		
		List<TeacherEntity> list = new ArrayList<TeacherEntity>();
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql = "select tea from TeacherEntity tea where type like:typeString and state like:stateString and username like :tea_nameString";
			list = session.createQuery(hql)
					.setString("typeString", "%"+type+"%")
					.setString("stateString", "%"+state+"%")
					.setString("tea_nameString", "%"+tea_name+"%")
					.list();
//			String hql = "select tea from TeacherEntity tea where type like'%"+type+"%' and state like'%"+state+"%' and username like'%"+tea_name+"%'";
//			list = session.createQuery(hql)
//					.list();
			session.getTransaction().commit();
//			HibernateSessionFactory.closeSession();
		} catch (HibernateException e) {
			return null;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		return list;
	}


	/**
	 * 搜索学生信息
	 * @param type
	 * @param state
	 * @param tea_name
	 * @return
	 */
	public List<StudentEntity> searchStu(String type, String state,String stu_name) {
		//将type、state值为2转成不限
		if("3".equals(type))
			type = "";
		if("3".equals(state))
			state = "";
		if(stu_name==null)
			stu_name = "";
		
		List<StudentEntity> list = new ArrayList<StudentEntity>();
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql = "select stu from StudentEntity stu where type like:typeString and state like:stateString and username like:stu_nameString";
			list = session.createQuery(hql)
					.setString("typeString", "%"+type+"%")
					.setString("stateString", "%"+state+"%")
					.setString("stu_nameString", "%"+stu_name+"%")
					.list();
			session.getTransaction().commit();
//			HibernateSessionFactory.closeSession();
		} catch (HibernateException e) {
			return null;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		return list;
	}


	/**
	 * 修改教师账户信息
	 * @param tea_id
	 * @param tea_name
	 * @param type
	 * @param state
	 * @return
	 */
	public boolean updateTea(String tea_id, String tea_name, String type,String state) {
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql = "update TeacherEntity tea set tea.username=?,tea.type=?,tea.state=? where tea.id =?";
			Query q = session.createQuery(hql);
			q.setString(0,tea_name);
			q.setString(1,type);
			q.setString(2,state);
			q.setString(3,tea_id);
			q.executeUpdate();
			session.getTransaction().commit();
//			HibernateSessionFactory.closeSession();
		} catch (HibernateException e) {
			//查询发生异常
			return false;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		return true;
	}


	/**
	 * 修改学生账户信息
	 * @param stu_id
	 * @param stu_name
	 * @param type
	 * @param state
	 * @return
	 */
	public boolean updateStu(String stu_id, String stu_name, String type,String state) {
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql = "update StudentEntity stu set stu.username=?,stu.type=?,stu.state=? where stu.id =?";
			Query q = session.createQuery(hql);
			q.setString(0,stu_name);
			q.setString(1,type);
			q.setString(2,state);
			q.setString(3,stu_id);
			q.executeUpdate();
			session.getTransaction().commit();
//			HibernateSessionFactory.closeSession();
		} catch (HibernateException e) {
			// 查询发生异常
			return false;
		}finally{
			HibernateSessionFactory.closeSession();
		}

		return true;
	}


	/**
	 * 根据tea_id获取该教师的基本信息
	 * @param tea_id
	 * @return
	 */
	public TeacherEntity getTeacherById(long tea_id) {
		TeacherEntity teacherEntity;//不必初始化！若根据某一id查不到这个老师，说明客户端提交的id有问题，直接返回null
		
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql = "select tea from TeacherEntity tea where id=:tea_idString";
			teacherEntity = (TeacherEntity) session.createQuery(hql).setLong("tea_idString", tea_id).uniqueResult();
			session.getTransaction().commit();
//			HibernateSessionFactory.closeSession();
		} catch (HibernateException e) {
			return null;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		return teacherEntity;
	}


	
	/**
	 * 批量认证全部教师
	 * @return
	 */
	public boolean authorTeaAll() {
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql = "update TeacherEntity set state=1 where state=0";
			session.createQuery(hql).executeUpdate();
			session.getTransaction().commit();
//			HibernateSessionFactory.closeSession();
		} catch (HibernateException e) {
			//查询发生异常
			return false;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		return true;
	}


	/**
	 * 批量认证所有学生账户
	 * @return
	 */
	public boolean authorStuAll() {
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql = "update StudentEntity set state=1 where state=0";
			session.createQuery(hql).executeUpdate();
			session.getTransaction().commit();
//			HibernateSessionFactory.closeSession();
		} catch (HibernateException e) {
			//查询发生异常
			return false;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		return true;
	}


	/**
	 * 认证某些教师账户
	 * @param map
	 * @param user_id
	 * @param user_name
	 * @return
	 */
	public boolean authorSomeTea(Map<String, String> map, String user_id,String user_name) {
		String[] ids = new String[map.size()];
		ids = map.keySet().toArray(ids);
		
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<ids.length;i++){
			if(i<ids.length-1)
				sb.append("id="+ids[i]+" or ");
			else
				sb.append("id="+ids[i]);
		}
		System.out.println(sb.toString());
		
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql = "update TeacherEntity set state=1 where "+sb.toString();
			session.createQuery(hql).executeUpdate();
			session.getTransaction().commit();
//			HibernateSessionFactory.closeSession();
		} catch (HibernateException e) {
			//查询发生异常
			return false;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		return true;
	}


	
	/**
	 * 认证某些教师账户
	 * @param map
	 * @param user_id
	 * @param user_name
	 * @return
	 */
	public boolean authorSomeStu(Map<String, String> map, String user_id,String user_name) {
		String[] ids = new String[map.size()];
		ids = map.keySet().toArray(ids);
		
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<ids.length;i++){
			if(i<ids.length-1)
				sb.append("id="+ids[i]+" or ");
			else
				sb.append("id="+ids[i]);
		}
		System.out.println(sb.toString());
		
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql = "update StudentEntity set state=1 where "+sb.toString();
			session.createQuery(hql).executeUpdate();
			session.getTransaction().commit();
//			HibernateSessionFactory.closeSession();
		} catch (HibernateException e) {
			//查询发生异常
			return false;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		return true;
	}

}
