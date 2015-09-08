package com.rep.tea.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.rep.tea.entity.TeaDoLogEntity;
import com.rep.tea.entity.TeaLoginLogEntity;
import com.rep.tea.service.LogService;

public class LogAction extends ActionSupport {
	private String result = "yes";
	private String reason;
	private String type;
	private String tea_id;
	private String tea_name;
	private String content;
	private String startTime;
	private String endTime;
	private List<TeaLoginLogEntity> teaLoginLogList = new ArrayList<TeaLoginLogEntity>();
	private List<TeaDoLogEntity> teaDoLogList = new ArrayList<TeaDoLogEntity>();
	private LogService service = new LogService();
	
	/**
	 * 获取教师登陆日志
	 */
	public String getTeaLoginLog(){
		if((!"0".equals(type) && !"1".equals(type) && !"2".equals(type)) ||
				(startTime==null || endTime==null ||
				"".equals(startTime) || "".equals(endTime))){
			this.result = "no";
			this.reason = "type、startTime、endTime不能为空，且type只能为012";
			return "getTeaLoginLog";
		}
		
		//将时间字符串构造成Date对象
		Date dateStartTime;
		Date dateEndTime;
		//若传来的startTime和endTime是个日期格式，而不是0（0代表不限时间），则判断日期格式是否正确；如果是0的话，在dao层判断，再写一条不包含日期范围的sql语句
		if(!this.startTime.equals("0")&&!this.endTime.equals("0")){
			try {
				dateStartTime = new SimpleDateFormat("yyyy-MM-dd").parse(this.startTime);
				dateEndTime = new SimpleDateFormat("yyyy-MM-dd").parse(this.endTime);
			} catch (ParseException e) {
				this.result = "no";
				this.reason = "startTime、endTime格式不正确！格式为必须是：“2015-05-04”";
				return "getTeaLoginLog";
			}

			// 验证客户端传来的日期是否正确
			if (dateStartTime.after(dateEndTime)) {
				this.result = "no";
				this.reason = "startTime必须要小于endTime！";
				return "getTeaLoginLog";
			}
		}
		
		//开始查询
		teaLoginLogList = service.getTeaLoginLog(tea_name,type,startTime,endTime);
		
		//查询失败
		if(teaLoginLogList==null){
			this.result = "no";
			this.reason = "获取日志发生异常";
			return "getTeaLoginLog";
		}
		
		//查询成功
		return "getTeaLoginLog";
	}
	
	
	/**
	 * 获取教师操作日志
	 */
	public String getTeaDoLog(){
		if(!"0".equals(type) && !"1".equals(type) && !"2".equals(type)
				||(startTime==null || endTime==null ||
				"".equals(startTime) || "".equals(endTime))){
			this.result = "no";
			this.reason = "type、startTime、endTime不能为空，且type只能为012";
			return "getTeaDoLog";
		}

		//将时间字符串构造成Date对象
		Date dateStartTime;
		Date dateEndTime;
		//若传来的startTime和endTime是个日期格式，而不是0（0代表不限时间），则判断日期格式是否正确；如果是0的话，在dao层判断，再写一条不包含日期范围的sql语句
		if(!this.startTime.equals("0")&&!this.endTime.equals("0")){
			try {
				dateStartTime = new SimpleDateFormat("yyyy-MM-dd").parse(this.startTime);
				dateEndTime = new SimpleDateFormat("yyyy-MM-dd").parse(this.endTime);
			} catch (ParseException e) {
				this.result = "no";
				this.reason = "startTime、endTime格式不正确！格式为必须是：“2015-05-04”";
				return "getTeaDoLog";
			}

			// 验证客户端传来的日期是否正确
			if (dateStartTime.after(dateEndTime)) {
				this.result = "no";
				this.reason = "startTime必须要小于endTime！";
				return "getTeaDoLog";
			}
		}
		
		//开始查询
		teaDoLogList = service.getTeaDoLog(tea_name,type,startTime,endTime);
		
		//查询失败
		if(teaDoLogList==null){
			this.result = "no";
			this.reason = "获取日志发生异常";
			return "getTeaDoLog";
		}
		
		//查询成功
		return "getTeaDoLog";
	}
	
	
	/**
	 * 添加教师操作日志
	 * @return
	 */
	public String addTeaDoLog(){
		if(tea_id==null || "".equals(tea_id) || content==null || "".equals(content)){
			this.result = "no";
			this.reason = "tea_id、content不能为空";
			return "getTeaDoLog";
		}
		
		//开始添加操作日志－失败
		if(!service.addTeaDoLog(tea_id,content)){
			this.result = "no";
			this.reason = "教师操作日志添加失败";
			return "getTeaDoLog";
		}
		
		//开始添加操作日志－成功
		return "addTeaDoLog";
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

	public List<TeaLoginLogEntity> getTeaLoginLogList() {
		return teaLoginLogList;
	}

	public void setTeaLoginLogList(List<TeaLoginLogEntity> teaLoginLogList) {
		this.teaLoginLogList = teaLoginLogList;
	}

	public List<TeaDoLogEntity> getTeaDoLogList() {
		return teaDoLogList;
	}

	public void setTeaDoLogList(List<TeaDoLogEntity> teaDoLogList) {
		this.teaDoLogList = teaDoLogList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTea_name() {
		return tea_name;
	}

	public void setTea_name(String tea_name) {
		this.tea_name = tea_name;
	}


	public String getTea_id() {
		return tea_id;
	}


	public void setTea_id(String tea_id) {
		this.tea_id = tea_id;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getEndTime() {
		return endTime;
	}


	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


	public String getStartTime() {
		return startTime;
	}


	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}




	
	
}
