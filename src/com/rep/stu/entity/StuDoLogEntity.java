package com.rep.stu.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 学生操作日志表所对用的实体类
 * @author chaibozhou
 *
 */
@Entity
@Table(name="stu_do_log")
public class StuDoLogEntity {
	private long id;
	private String stu_name;//学生用户名
	private long stu_id;//学生id
	private int type;//学生类型
	private Timestamp time;//操作时间
	private String content;//操作内容
	private int state;//本条记录的状态
	
	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getStu_name() {
		return stu_name;
	}
	public void setStu_name(String stu_name) {
		this.stu_name = stu_name;
	}
	public long getStu_id() {
		return stu_id;
	}
	public void setStu_id(long stu_id) {
		this.stu_id = stu_id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	
}
