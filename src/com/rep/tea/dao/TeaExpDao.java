package com.rep.tea.dao;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.rep.core.HibernateSessionFactory;
import com.rep.stu.entity.StateEntity;
import com.rep.stu.entity.StuExpEntity;
import com.rep.tea.entity.TeaExpEntity;

public class TeaExpDao {

	/**
	 * 查询所有未删除实验
	 * @return
	 */
	public List<TeaExpEntity> getExpTea() {
		List<TeaExpEntity> list = new ArrayList<TeaExpEntity>();
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql = "select e from TeaExpEntity e where state=1";
			list = session.createQuery(hql).list();
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
	 * 按关键字搜索实验
	 * @param keyword 
	 * @return
	 */
	public List<TeaExpEntity> serchExpTea(String keyword) {
		List<TeaExpEntity> list = new ArrayList<TeaExpEntity>();
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql = "select e from TeaExpEntity e where state=1 and name like:keywordString";
			list = session.createQuery(hql)
					.setString("keywordString", "%"+keyword+"%")
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
	 * 按学生姓名关键字搜索学生实验信息
	 * @param keyword
	 * @return
	 */
	public List<StuExpEntity> serchExpStu(String keyword) {
		List<StuExpEntity> list = new ArrayList<StuExpEntity>();
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql = "select e from StuExpEntity e where state=1 and stu_name like:keywordString";
			list = session.createQuery(hql)
					.setString("keywordString", "%"+keyword+"%")
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
	 * 创建实验
	 * @param name
	 * @param content
	 * @param tea_id
	 * @param tea_name
	 * @return
	 */
	public boolean createExpTea(String name, String content, String tea_id,String tea_name) {
		TeaExpEntity entity = new TeaExpEntity();
		// 设置参数
		entity.setTime(new Timestamp(new Date().getTime()));
		entity.setState(1);
		entity.setName(name);
		entity.setContent(content);
		entity.setTea_name(tea_name);
		entity.setTea_id(Long.parseLong(tea_id));

		// 将实体存入数据库
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
		
		return true;
	}

	
	/**
	 * 获取实验箱们的状态
	 * @return
	 */
	public List<StateEntity> getState() {
		List<StateEntity> list = new ArrayList<StateEntity>();
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql = "select e from StateEntity e";
			list = session.createQuery(hql).list();
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
	 * 获取实时监控信息
	 * @return
	 */
	public String getCurInfo(long lastTime) {
		//查看文件mtime
		File file = new File("~/Desktop/data");
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				return null;
			}
		file.lastModified();
		
		//获取文件内容并发送
		return null;
	}

	
	/**
	 * 查看某一个实验的学生完成情况
	 * @param exp_id
	 * @return
	 */
	public List<StuExpEntity> getExpTeaByExpId(String exp_id) {
		List<StuExpEntity> list = new ArrayList<StuExpEntity>();
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql = "select e from StuExpEntity e where state=1 and exp_id=:exp_idString";
			list = session.createQuery(hql)
					.setString("exp_idString", exp_id)
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
	 * 获取某一个实验的已完成人数、进行中人数
	 * @param exp_id
	 * @return
	 */
	public Map<String, String> getExpState(String exp_id) {
		String finish;
		String unfinish;
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql1 = "select count(*) from StuExpEntity e where exp_id=:exp_idString and finish=1";
			finish = session.createQuery(hql1)
					.setString("exp_idString", exp_id)
					.uniqueResult()+"";
			String hql2 = "select count(*) from StuExpEntity e where exp_id=:exp_idString and finish=0";
			unfinish = session.createQuery(hql2)
					.setString("exp_idString", exp_id)
					.uniqueResult()+"";
			session.getTransaction().commit();
//			HibernateSessionFactory.closeSession();
		} catch (HibernateException e) {
			return null;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("finish", finish);
		map.put("unfinish", unfinish);
		return map;
	}

	
	/**
	 * 获取实验箱的使用频率
	 * @param start_month
	 * @param end_month
	 * @return
	 */
	public List<String> getBoxUseCount(String start_month, String end_month) {
		List<String> list = new ArrayList<String>();
		
		try {
			int start = Integer.parseInt(start_month);
			int end = Integer.parseInt(end_month);
			System.out.println("start:"+start);
			System.out.println("end:"+end);
			Session session = HibernateSessionFactory.getSession();
			while (start <= end) {
				System.out.println("当前月份："+start);
				session.beginTransaction();
				String hql = "select count(*) from StuExpEntity e where start_time between '2015-"+start+"-01 00:00:00' and '2015-"+(start+1)+"-01 23:59:59'";
				long count = (long) session.createQuery(hql).uniqueResult();
				System.out.println(start+"月："+count);
				session.getTransaction().commit();
				
				list.add(count+"");
				++start;
			}
		} catch (HibernateException e) {
			return null;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		return list;
	}

	
	/**
	 * 在state表中创建N个箱子的记录，并将箱子的状态设置为忙碌,IP:192.168.2.xxx
	 * exp_id=2,exp_name=数据结构的实现,stu_id=999,stu_name=tester
	 * @param count
	 * @return
	 */
	public boolean startNExp_setState(String count_old) {
		int count = Integer.parseInt(count_old);
		
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			for(int i=0;i<count;i++){
				StateEntity entity = new StateEntity();
				entity.setBusy(1);
				entity.setExp_id(2+"");
				entity.setExp_name("数据结构的实现");
				entity.setIp("192.168.2."+(i+1));
				entity.setOpen(1);
				entity.setSshPass("testPass");
				entity.setSshUser("tester");
				entity.setStu_id("999");
				entity.setStu_name("tester");
				session.save(entity);
				System.out.println("已插入第"+i+"条");
			}
			session.getTransaction().commit();
		} catch (HibernateException e) {
			return false;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		return true;
	}

	
	/**
	 * 在stu_exp表中生成N条学生开始实验的记录，exp_id=2,exp_name=数据结构的实现,exp_id=2,exp_name=数据结构的实现,stu_id=999,stu_name=tester
	 * @param count
	 * @return
	 */
	public boolean startNExp_setStuExp(String count_old) {
		int count = Integer.parseInt(count_old);
		
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			for(int i=0;i<count;i++){
				System.out.println("i="+(i+1));
				StuExpEntity entity = new StuExpEntity();
				entity.setExp_id(2);
				entity.setExp_name("数据结构的实现");
				entity.setFinish(0);
				entity.setStart_time(new Timestamp(new Date().getTime()));
				
				//--------
//				String str_date="2015-4-11";
//				Date date;
//				  DateFormat dateFormat = DateFormat.getDateInstance();
//				  try {
//					date = dateFormat.parse(str_date);
//					entity.setStart_time(new Timestamp(date.getTime()));
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}

				//--------
				
				entity.setState(1);
				entity.setStu_id(999);
				entity.setStu_name("tester");
				session.save(entity);
			}
			session.getTransaction().commit();
		} catch (HibernateException e) {
			return false;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		return true;
	}

	/**
	 * 在state表中删除stu_name是test的N个箱子的记录
	 * @return
	 */
	public boolean endNExp_setState() {
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql = "delete StateEntity where stu_name='tester'";
			session.createQuery(hql).executeUpdate();
			session.getTransaction().commit();
		} catch (HibernateException e) {
			return false;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		return true;
	}
	
	/**
	 * 在stu_exp表中stu_name=rester的学生的实验记录修改为：finish=1
	 * @return
	 */
	public boolean endNExp_setStuExp() {
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql = "update StuExpEntity set finish=1 where stu_name='tester'";
			session.createQuery(hql).executeUpdate();
			session.getTransaction().commit();
		} catch (HibernateException e) {
			return false;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		return true;
	}
	
	public static void main(String[] arugs){
		new TeaExpDao().startNExp_setStuExp(8000+"");
//		new TeaExpDao().endNExp_setState();
	}
}
