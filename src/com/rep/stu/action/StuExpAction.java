package com.rep.stu.action;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.rep.stu.entity.StateEntity;
import com.rep.stu.entity.StuExpEntity;
import com.rep.stu.service.StuExpService;

public class StuExpAction extends ActionSupport {
	private String result = "yes";
	private String reason;
	private String exp_id;
	private String exp_name;
	private String stu_id;
	private String stu_name;
	private String ip;
	private String sshUser;
	private String sshPass;
	private List<StuExpEntity> stuExpList = new ArrayList<StuExpEntity>();
	private StuExpService service = new StuExpService();
	
	/**
	 * 开始实验
	 * @return
	 */
	public String startExp(){
		if(exp_name==null || "".equals(exp_name) || exp_id==null || "".equals(exp_id) ||
				stu_name==null || "".equals(stu_name) || stu_id==null || "".equals(stu_id)){
			this.result = "no";
			this.reason = "exp_name、exp_id、stu_name、stu_id均不能为空！";
			return "startExp";
		}
		
		//开始实验失败
		StateEntity stateEntity = service.startExp(exp_name,exp_id,stu_name,stu_id);
		if(stateEntity==null){
			this.result = "no";
			this.reason = "实验箱已经分配完！请等待";
			return "startExp";
		}
		
		//开始实验成功
		this.ip = stateEntity.getIp();
		this.sshUser = stateEntity.getSshUser();
		this.sshPass = stateEntity.getSshPass();
		return "startExp";
	}
	
	/**
	 * 结束实验
	 * @return
	 */
	public String endExp(){
		if(exp_name==null || "".equals(exp_name) || exp_id==null || "".equals(exp_id) ||
				stu_name==null || "".equals(stu_name) || stu_id==null || "".equals(stu_id)
				|| ip==null || "".equals(ip)){
			this.result = "no";
			this.reason = "exp_name、exp_id、stu_name、stu_id、ip均不能为空！";
			return "endExp";
		}
		
		//更新记录失败
		if(!service.endExp(exp_name,exp_id,stu_name,stu_id,ip)){
			this.result = "no";
			this.reason = "服务器发生异常，实验结束失败！";
			return "endExp";
		}
		
		//更新记录失败成功
		return "endExp";
	}
	
	/**
	 * 获取当前登录学生所做过的所有实验 
	 */
	public String getMyExp(){
		if(stu_id==null || "".equals(stu_id)){
			this.result = "no";
			this.reason = "stu_id不能为空";
			return "getMyExp";
		}
		
		stuExpList = service.getMyExp(stu_id);
		if(stuExpList==null){
			this.result = "no";
			this.reason = "服务器发生异常，请重试！";
			return "getMyExp";
		}
		
		this.result = "yes";
		this.reason = "获取成功！";
		return "getMyExp";
	}
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
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

	public List<StuExpEntity> getStuExpList() {
		return stuExpList;
	}

	public void setStuExpList(List<StuExpEntity> stuExpList) {
		this.stuExpList = stuExpList;
	}

	
	
}
