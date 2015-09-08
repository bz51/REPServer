package com.rep.tea.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 教师登陆日志表对应的实体类
 * @author chaibozhou
 */
@Entity
@Table(name="tea_login_log")
public class TeaLoginLogEntity {
	private long id;
	private String tea_name;//教师用户名
	private long tea_id;//教师id
	private int type;//教师类型
	private Timestamp time;//登陆时间
	private int state;//本条记录的状态
	
	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTea_name() {
		return tea_name;
	}
	public void setTea_name(String tea_name) {
		this.tea_name = tea_name;
	}
	public long getTea_id() {
		return tea_id;
	}
	public void setTea_id(long tea_id) {
		this.tea_id = tea_id;
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
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	
}
