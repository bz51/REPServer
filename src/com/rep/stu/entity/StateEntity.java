package com.rep.stu.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="state")
public class StateEntity {
	private long id;
	private String ip;
	private int open;
	private int busy;
	private String stu_id;
	private String stu_name;
	private String exp_id;
	private String exp_name;
	private String sshUser;
	private String sshPass;
	
	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getOpen() {
		return open;
	}
	public void setOpen(int open) {
		this.open = open;
	}
	public int getBusy() {
		return busy;
	}
	public void setBusy(int busy) {
		this.busy = busy;
	}
	public String getStu_id() {
		return stu_id;
	}
	public void setStu_id(String stu_id) {
		this.stu_id = stu_id;
	}
	public String getStu_name() {
		return stu_name;
	}
	public void setStu_name(String stu_name) {
		this.stu_name = stu_name;
	}
	public String getExp_id() {
		return exp_id;
	}
	public void setExp_id(String exp_id) {
		this.exp_id = exp_id;
	}
	public String getExp_name() {
		return exp_name;
	}
	public void setExp_name(String exp_name) {
		this.exp_name = exp_name;
	}
	public String getSshUser() {
		return sshUser;
	}
	public void setSshUser(String sshUser) {
		this.sshUser = sshUser;
	}
	public String getSshPass() {
		return sshPass;
	}
	public void setSshPass(String sshPass) {
		this.sshPass = sshPass;
	}
	
	
}
