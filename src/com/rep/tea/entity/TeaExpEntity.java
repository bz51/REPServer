package com.rep.tea.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 实验表所对应的实体类
 * @author chaibozhou
 *
 */
@Entity
@Table(name="exp_tea")
public class TeaExpEntity {
	private long id;
	private String name;//实验名称
	private String content;//实验内容
	private long tea_id;//指导老师id
	private String tea_name;//指导老师用户名
	private Timestamp time;//创建时间
	private int state;//本条记录状态

	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getTea_id() {
		return tea_id;
	}
	public void setTea_id(long tea_id) {
		this.tea_id = tea_id;
	}
	public String getTea_name() {
		return tea_name;
	}
	public void setTea_name(String tea_name) {
		this.tea_name = tea_name;
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
