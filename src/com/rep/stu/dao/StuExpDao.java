package com.rep.stu.dao;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.jcraft.jsch.JSchException;
import com.rep.core.HibernateSessionFactory;
import com.rep.core.SSHTool;
import com.rep.stu.entity.StateEntity;
import com.rep.stu.entity.StuExpEntity;
import com.rep.tea.entity.TeaExpEntity;

public class StuExpDao {

	/**
	 * 更新学生实验表
	 * @param exp_name
	 * @param exp_id
	 * @param stu_name
	 * @param stu_id
	 * @return
	 */
	public boolean updatestuExp(String exp_name, String exp_id, String stu_name,String stu_id) {
		StuExpEntity entity = new StuExpEntity();
		//设置
		entity.setStart_time(new Timestamp(new Date().getTime()));
		entity.setState(1);
		entity.setExp_id(Long.parseLong(exp_id));
		entity.setExp_name(exp_name);
		entity.setFinish(0);
		entity.setStu_id(Long.parseLong(stu_id));
		entity.setStu_name(stu_name);
		
		//将用户信息存入数据库
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			session.save(entity);
			session.getTransaction().commit();
//			HibernateSessionFactory.closeSession();
		} catch (HibernateException e) {
			return false;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		//注册成功
		return true;
	}

	
	/**
	 * 结束实验
	 * @param exp_name
	 * @param exp_id
	 * @param stu_name
	 * @param stu_id
	 * @return
	 */
	public boolean endExp(String exp_name, String exp_id, String stu_name,String stu_id) {
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql = "update StuExpEntity set finish=1,end_time=current_timestamp() where exp_id=:exp_idString and exp_name=:exp_nameString and stu_id=:stu_idString and stu_name=:stu_nameString";
			session.createQuery(hql)
			.setString("exp_nameString", exp_name)
			.setString("exp_idString", exp_id)
			.setString("stu_nameString", stu_name)
			.setString("stu_idString", stu_id)
			.executeUpdate();
			session.getTransaction().commit();
//			HibernateSessionFactory.closeSession();
		} catch (HibernateException e) {
			//更新发生异常
			return false;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		return true;
	}


	/**
	 * 连通性测试＋更新实验箱状态表
	 * @return
	 */
	public StateEntity connectTest(String exp_name, String exp_id, String stu_name,String stu_id) {
		//获取所有箱子的IP
		List<String> ipList = null;
		Session session = HibernateSessionFactory.getSession();
		session.beginTransaction();
		String hql = "select ip from StateEntity";
		ipList = session.createQuery(hql).list();
		session.getTransaction().commit();
		HibernateSessionFactory.closeSession();
		
		//与每一个箱子进行连通性测试＋将结果存入数据库
		for(String ip : ipList){
			System.out.println(ip);
			String result;
			try {
				//如果是115开头的ip，用户名、密码使用root、Bz220382
				if(ip.startsWith("115"))
					result = SSHTool.exec(ip, "root", "Bz220382", 22, "cd /var");
				//如果是192开头的ip，用户名、密码使用sdbox
				else
					result = SSHTool.exec(ip, "sdbox", "sdbox", 22, "cd /var");
				System.out.println("ssh result:"+result);
				//将连通性测试结果存入数据库
				if(result.equals("yes"))
					result = "1";
				else
					result = "0";
				Session session2 = HibernateSessionFactory.getSession();
				session2.beginTransaction();
				String hql2 = "update StateEntity set open="+result+" where ip='"+ip+"'";
				session2.createQuery(hql2).executeUpdate();
				session2.getTransaction().commit();
//				HibernateSessionFactory.closeSession();
			} catch (JSchException | IOException e) {
				e.printStackTrace();
			}finally{
				HibernateSessionFactory.closeSession();
			}
		}
		
		
		//查数据库，找一个“开机＋空闲”的箱子
		List<StateEntity> entityList = new ArrayList<StateEntity>();
		Session session3 = HibernateSessionFactory.getSession();
		session3.beginTransaction();
		String hql3 = "select s from StateEntity s where open=1 and busy=0";
		entityList = session3.createQuery(hql3).list();
		session3.getTransaction().commit();
		HibernateSessionFactory.closeSession();
		
		System.out.println("entityList.size:"+entityList.size());
		//找到了“开机＋空闲”的箱子，返回一个出去，并将stu_id、stu_name、exp_id、exp_name填入表中
		if(entityList.size()>0){
			//将stu_id、stu_name、exp_id、exp_name填入表中
			Session session4 = HibernateSessionFactory.getSession();
			session4.beginTransaction();
			String hql4 = "update StateEntity set busy=1,stu_id='"+stu_id+"',stu_name='"+stu_name+"',exp_id='"+exp_id+"',exp_name='"+exp_name+"' where id="+entityList.get(0).getId();
			session4.createQuery(hql4).executeUpdate();
			session4.getTransaction().commit();
			HibernateSessionFactory.closeSession();
			return entityList.get(0);
		}
		else
			return null;
	}


	/**
	 * 更新表state，将busy设为空闲，并将学生、实验的信息删掉
	 * @param ip
	 * @return
	 */
	public boolean updateState(String ip) {
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql = "update StateEntity set busy=0,stu_id=null,stu_name=null,exp_id=null,exp_name=null where ip='"+ip+"'";
			session.createQuery(hql).executeUpdate();
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
	 * 获取当前登录学生所做过的所有实验
	 * @param stu_id
	 * @return
	 */
	public List<StuExpEntity> getMyExp(String stu_id) {
		List<StuExpEntity> list = new ArrayList<StuExpEntity>();
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql = "select e from StuExpEntity e where stu_id=:stu_idString";
			list = session.createQuery(hql)
					.setString("stu_idString", stu_id)
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

}
