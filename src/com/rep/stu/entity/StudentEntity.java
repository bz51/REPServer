package com.rep.stu.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 学生账户表所对应的实体类
 * @author chaibozhou
 *
 */
@Entity
@Table(name="stu_user")
public class StudentEntity {
	private long id;
	private String username;
	private String password;
	private int type;//学生类型
	private Timestamp time;//注册时间
	private int state;//本条记录的状态
	private String result;
	private String reason;
	private String phone;
	
	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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

	@Transient
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	@Transient
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
