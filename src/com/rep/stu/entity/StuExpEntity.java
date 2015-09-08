package com.rep.stu.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 学生实验情况表所对应的实体类
 * @author chaibozhou
 *
 */
@Entity
@Table(name="exp_stu")
public class StuExpEntity {
	private long id;
	private long exp_id;//实验id
	private String exp_name;//实验名称
	private String stu_name;//学生用户名
	private long stu_id;//学生id
	private int finish;//是否完成
	private Timestamp start_time;//实验开始时间
	private Timestamp end_time;//实验完成时间
	private String path;//实验报告存放的路径
	private int state;//本条记录的状态
	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getExp_id() {
		return exp_id;
	}
	public void setExp_id(long exp_id) {
		this.exp_id = exp_id;
	}
	public String getExp_name() {
		return exp_name;
	}
	public void setExp_name(String exp_name) {
		this.exp_name = exp_name;
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
	public int getFinish() {
		return finish;
	}
	public void setFinish(int finish) {
		this.finish = finish;
	}
	public Timestamp getStart_time() {
		return start_time;
	}
	public void setStart_time(Timestamp start_time) {
		this.start_time = start_time;
	}
	public Timestamp getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Timestamp end_time) {
		this.end_time = end_time;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}

	
	
}
