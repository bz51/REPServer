package com.rep.tea.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.rep.tea.service.BackupService;

public class BackupAction extends ActionSupport {
	private String result = "yes";
	private String reason;
	private String time;
	private BackupService service = new BackupService();
	private String list;
	
	/**
	 * 立即备份
	 * 传入list为7位：list=0,0,0,1,1,1,1
	 * @return
	 */
	public String backUpNow(){
		if(list==null || "".equals(list)){
			this.result = "no";
			this.reason = "list不能为空！";
			return "backUpNow";
		}
		
		//切割list
		String[] lists = list.split(",");
		List<String> list = new ArrayList<String>();
		for(int i=0;i<lists.length;i++){
			System.out.println(lists[i]);
			if(lists[i].equals("1"))
				list.add((i+1)+"");
		}
		
		
		//开始根据条件备份
		boolean result = service.backUpNow(list.toArray(new String[list.size()]));
		return "backUpNow";
	}
	
	
	/**
	 * 获取上次备份时间
	 * @return
	 */
	public String getLastBackupTime(){
		File file = new File("/tea_user");
		if(!file.exists()){
			this.result = "no";
			this.reason = "尚未备份";
			return "getLastBackupTime";
		}
		
		long time_pre = file.lastModified();
		Date currentTime = new Date(time_pre);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		this.time = dateString;
		System.out.println(this.time);
		
		return "getLastBackupTime";
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


	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}


	public String getList() {
		return list;
	}


	public void setList(String list) {
		this.list = list;
	}

	
	
}
