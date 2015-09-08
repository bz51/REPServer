package com.rep.tea.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.rep.core.HibernateSessionFactory;
import com.rep.stu.entity.StateEntity;
import com.rep.stu.entity.StuDoLogEntity;
import com.rep.stu.entity.StuExpEntity;
import com.rep.stu.entity.StuLoginLogEntity;
import com.rep.stu.entity.StudentEntity;
import com.rep.tea.entity.TeaDoLogEntity;
import com.rep.tea.entity.TeaExpEntity;
import com.rep.tea.entity.TeaLoginLogEntity;
import com.rep.tea.entity.TeacherEntity;

public class BackupDao {
	
	/**
	 * 备份数据库所有的表
	 * @param lists 
	 * @return
	 */
	public boolean backUpNow(String[] lists) {
		//将lists放入List中
		List<String> list = new ArrayList<String>();
		for(String s : lists)
			list.add(s);
		
		if(list.contains("1")){
			this.backUpStudentEntity();
			System.out.println("已备份1……");
		}
		if(list.contains("2")){
			this.backUpTeacherEntity();
			System.out.println("已备份2……");
		}
		if(list.contains("3")){
			this.backUpTeaLoginLogEntity();
			System.out.println("已备份3……");
		}
		if(list.contains("4")){
			this.backUpTeaDoLogEntity();
			System.out.println("已备份4……");
		}
		if(list.contains("5")){
			this.backUpStuExpEntity();
			System.out.println("已备份5……");
		}
		if(list.contains("6")){
			this.backUpTeaExpEntity();
			System.out.println("已备份6……");
		}
		if(list.contains("7")){
			this.backUpStateEntity();
			System.out.println("已备份7……");
		}
		
		return true;
		
//		boolean result1 = this.backUpStateEntity();
//		boolean result2 = this.backUpStudentEntity();
//		boolean result3 = this.backUpStuDoLogEntity();
//		boolean result4 = this.backUpStuExpEntity();
//		boolean result5 = this.backUpStuLoginLogEntity();
//		boolean result6 = this.backUpTeacherEntity();
//		boolean result7 = this.backUpTeaDoLogEntity();
//		boolean result8 = this.backUpTeaExpEntity();;
//		boolean result9 = this.backUpTeaLoginLogEntity();
//		
//		if(result1&&result2&&result3&&result4&&result5&&result6&&result7&&result8&&result9)
//			return true;
//		
//		return false;
			
	}
	
	/**
	 * 备份TeacherEntity
	 */
	private boolean backUpTeacherEntity(){
		Session session = HibernateSessionFactory.getSession();
		session.beginTransaction();
		//获取数据
		String hql = "select t from TeacherEntity t";
		List<TeacherEntity> list = session.createQuery(hql).list();
		session.getTransaction().commit();
		HibernateSessionFactory.closeSession();
		
		//存到文件中
		if(list==null)
			return false;
		
		try {
			//创建新文件夹
			File file = new File("/tea_user");
			if(!file.exists()){
//				file.mkdirs();
				file.createNewFile();
				System.out.println(file.exists());
			}
			
			//循环list，将数据写入文件
			BufferedWriter buf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			
			String str;
			for(TeacherEntity t : list){
				str=t.getId()+"|"+t.getPassword()+"|"+t.getState()+"|"+t.getType()+"|"+t.getUsername()+"|"+t.getTime();
				buf.write(str);
				buf.newLine();
				System.out.println(str);
			}
			
			//刷新和关闭
			buf.flush();
			buf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * 备份TeaDoLogEntity
	 */
	private boolean backUpTeaDoLogEntity(){

		Session session = HibernateSessionFactory.getSession();
		session.beginTransaction();
		//获取数据
		String hql = "select t from TeaDoLogEntity t";
		List<TeaDoLogEntity> list = session.createQuery(hql).list();
		session.getTransaction().commit();
		HibernateSessionFactory.closeSession();
		
		//存到文件中
		if(list==null)
			return false;
		
		try {
			//创建新文件夹
			File file = new File("/tea_do_log");
			if(!file.exists()){
//				file.mkdirs();
				file.createNewFile();
				System.out.println(file.exists());
			}
			
			//循环list，将数据写入文件
			BufferedWriter buf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			
			String str;
			for(TeaDoLogEntity t : list){
				str=t.getContent()+"|"+t.getId()+"|"+t.getState()+"|"+t.getTea_id()+"|"+t.getTea_name()+"|"+t.getType()+"|"+t.getTime();
				buf.write(str);
				buf.newLine();
				System.out.println(str);
			}
			
			//刷新和关闭
			buf.flush();
			buf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	
	}
	
	/**
	 * 备份TeaExpEntity
	 */
	private boolean backUpTeaExpEntity(){


		Session session = HibernateSessionFactory.getSession();
		session.beginTransaction();
		//获取数据
		String hql = "select t from TeaExpEntity t";
		List<TeaExpEntity> list = session.createQuery(hql).list();
		session.getTransaction().commit();
		HibernateSessionFactory.closeSession();
		
		//存到文件中
		if(list==null)
			return false;
		
		try {
			//创建新文件夹
			File file = new File("/exp_tea");
			if(!file.exists()){
//				file.mkdirs();
				file.createNewFile();
				System.out.println(file.exists());
			}
			
			//循环list，将数据写入文件
			BufferedWriter buf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			
			String str;
			for(TeaExpEntity t : list){
				str=t.getContent()+"|"+t.getId()+"|"+t.getName()+"|"+t.getState()+"|"+t.getTea_id()+"|"+t.getTea_name()+"|"+t.getTime();
				buf.write(str);
				buf.newLine();
				System.out.println(str);
			}
			
			//刷新和关闭
			buf.flush();
			buf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	
	
	}
	
	/**
	 * 备份TeaLoginLogEntity
	 */
	private boolean backUpTeaLoginLogEntity(){
		Session session = HibernateSessionFactory.getSession();
		session.beginTransaction();
		//获取数据
		String hql = "select t from TeaLoginLogEntity t";
		List<TeaLoginLogEntity> list = session.createQuery(hql).list();
		session.getTransaction().commit();
		HibernateSessionFactory.closeSession();
		
		//存到文件中
		if(list==null)
			return false;
		
		try {
			//创建新文件夹
			File file = new File("/tea_login_log");
			if(!file.exists()){
//				file.mkdirs();
				file.createNewFile();
				System.out.println(file.exists());
			}
			
			//循环list，将数据写入文件
			BufferedWriter buf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			
			String str;
			for(TeaLoginLogEntity t : list){
				str=t.getTea_name()+"|"+t.getId()+"|"+t.getState()+"|"+t.getTea_id()+"|"+t.getType()+"|"+t.getTime();
				buf.write(str);
				buf.newLine();
				System.out.println(str);
			}
			
			//刷新和关闭
			buf.flush();
			buf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	
	
	
	}
	
	/**
	 * 备份StateEntity
	 */
	private boolean backUpStateEntity(){

		Session session = HibernateSessionFactory.getSession();
		session.beginTransaction();
		//获取数据
		String hql = "select t from StateEntity t";
		List<StateEntity> list = session.createQuery(hql).list();
		session.getTransaction().commit();
		HibernateSessionFactory.closeSession();
		
		//存到文件中
		if(list==null)
			return false;
		
		try {
			//创建新文件夹
			File file = new File("/state");
			if(!file.exists()){
//				file.mkdirs();
				file.createNewFile();
				System.out.println(file.exists());
			}
			
			//循环list，将数据写入文件
			BufferedWriter buf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			
			String str;
			for(StateEntity t : list){
				str=t.getExp_id()+"|"+t.getBusy()+"|"+t.getExp_id()+"|"+t.getExp_name()+"|"+t.getIp()+"|"+t.getOpen()+"|"+t.getSshPass()+"|"+t.getSshUser()+"|"+t.getStu_id()+"|"+t.getStu_name();
				buf.write(str);
				buf.newLine();
				System.out.println(str);
			}
			
			//刷新和关闭
			buf.flush();
			buf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * 备份StudentEntity
	 */
	private boolean backUpStudentEntity(){

		Session session = HibernateSessionFactory.getSession();
		session.beginTransaction();
		//获取数据
		String hql = "select t from StudentEntity t";
		List<StudentEntity> list = session.createQuery(hql).list();
		session.getTransaction().commit();
		HibernateSessionFactory.closeSession();
		
		//存到文件中
		if(list==null)
			return false;
		
		try {
			//创建新文件夹
			File file = new File("/stu_user");
			if(!file.exists()){
//				file.mkdirs();
				file.createNewFile();
				System.out.println(file.exists());
			}
			
			//循环list，将数据写入文件
			BufferedWriter buf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			
			String str;
			for(StudentEntity t : list){
				str=t.getPassword()+"|"+t.getId()+"|"+t.getState()+"|"+t.getType()+"|"+t.getUsername()+"|"+t.getTime();
				buf.write(str);
				buf.newLine();
				System.out.println(str);
			}
			
			//刷新和关闭
			buf.flush();
			buf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * 备份StuDoLogEntity
	 */
	private boolean backUpStuDoLogEntity(){

		Session session = HibernateSessionFactory.getSession();
		session.beginTransaction();
		//获取数据
		String hql = "select t from StuDoLogEntity t";
		List<StuDoLogEntity> list = session.createQuery(hql).list();
		session.getTransaction().commit();
		HibernateSessionFactory.closeSession();
		
		//存到文件中
		if(list==null)
			return false;
		
		try {
			//创建新文件夹
			File file = new File("/stu_do_log");
			if(!file.exists()){
//				file.mkdirs();
				file.createNewFile();
				System.out.println(file.exists());
			}
			
			//循环list，将数据写入文件
			BufferedWriter buf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			
			String str;
			for(StuDoLogEntity t : list){
				str=t.getContent()+"|"+t.getId()+"|"+t.getState()+"|"+t.getStu_id()+"|"+t.getStu_name()+"|"+t.getType()+"|"+t.getTime();
				buf.write(str);
				buf.newLine();
				System.out.println(str);
			}
			
			//刷新和关闭
			buf.flush();
			buf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * 备份StuExpEntity
	 */
	private boolean backUpStuExpEntity(){

		Session session = HibernateSessionFactory.getSession();
		session.beginTransaction();
		//获取数据
		String hql = "select t from StuExpEntity t";
		List<StuExpEntity> list = session.createQuery(hql).list();
		session.getTransaction().commit();
		HibernateSessionFactory.closeSession();
		
		//存到文件中
		if(list==null)
			return false;
		
		try {
			//创建新文件夹
			File file = new File("/exp_stu");
			if(!file.exists()){
//				file.mkdirs();
				file.createNewFile();
				System.out.println(file.exists());
			}
			
			//循环list，将数据写入文件
			BufferedWriter buf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			
			String str;
			for(StuExpEntity t : list){
				str=t.getExp_name()+"|"+t.getExp_id()+"|"+t.getFinish()+"|"+t.getId()+"|"+t.getPath()+"|"+t.getState()+"|"+t.getStu_id()+"|"+t.getStu_name()+"|"+t.getEnd_time()+"|"+t.getStart_time();
				buf.write(str);
				buf.newLine();
				System.out.println(str);
			}
			
			//刷新和关闭
			buf.flush();
			buf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * 备份StuLoginLogEntity
	 */
	private boolean backUpStuLoginLogEntity(){

		Session session = HibernateSessionFactory.getSession();
		session.beginTransaction();
		//获取数据
		String hql = "select t from StuLoginLogEntity t";
		List<StuLoginLogEntity> list = session.createQuery(hql).list();
		session.getTransaction().commit();
		HibernateSessionFactory.closeSession();
		
		//存到文件中
		if(list==null)
			return false;
		
		try {
			//创建新文件夹
			File file = new File("/stu_login_log");
			if(!file.exists()){
//				file.mkdirs();
				file.createNewFile();
				System.out.println(file.exists());
			}
			
			//循环list，将数据写入文件
			BufferedWriter buf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			
			String str;
			for(StuLoginLogEntity t : list){
				str=t.getStu_name()+"|"+t.getId()+"|"+t.getState()+"|"+t.getStu_id()+"|"+t.getType()+"|"+t.getTime();
				buf.write(str);
				buf.newLine();
				System.out.println(str);
			}
			
			//刷新和关闭
			buf.flush();
			buf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	
}
