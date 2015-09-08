package com.rep.tea.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.rep.stu.entity.StudentEntity;
import com.rep.tea.entity.TeacherEntity;
import com.rep.tea.service.AccountService;

public class AccountAction extends ActionSupport {
	private String result = "yes";
	private String reason;
	private String tea_id;
	private String stu_id;
	private String type;
	private String state;
	private String tea_name;
	private String stu_name;
	private String user_id;//当前登陆用户的id
	private String user_name;//当前登陆用户的username
	private List<TeacherEntity> teaList = new ArrayList<TeacherEntity>();
	private List<StudentEntity> stuList = new ArrayList<StudentEntity>();
	private AccountService service = new AccountService();
	
	/**
	 * 获取未激活教师列表
	 * @return
	 */
	public String getInactiveTeas(){
		teaList = service.getInactiveTeas();
		
		//查询发生异常
		if(teaList==null){
			this.result = "no";
			this.reason = "服务器发生异常";
		}
		
		return "getInactiveTeas";
	}
	
	/**
	 * 获取未激活学生列表
	 * @return
	 */
	public String getInactiveStus(){
		stuList = service.getInactiveStus();
		
		//查询发生异常
		if(stuList==null){
			this.result = "no";
			this.reason = "服务器发生异常";
		}
		
		return "getInactiveStus";
	}
	
	
	/**
	 * 认证教师
	 * @return
	 */
	public String authorTea(){
		//tea_id若为空
		if(tea_id==null || "".equals(tea_id)
				|| tea_name==null || "".equals(tea_name)
				|| user_id==null || "".equals(user_id)
				|| user_name==null || "".equals(user_name)){
			this.result = "no";
			this.reason = "tea_id、tea_name、user_id、user_name不能为空";
			return "authorTea";
		}
		
		//认证失败
		if(!service.authorTea(tea_id,tea_name,user_id,user_name)){
			this.result = "no";
			this.reason = "认证失败";
			return "authorTea";
		}
		
		//认证成功
		return "authorTea";
	}
	
	
	/**
	 * 认证学生
	 * @return
	 */
	public String authorStu(){
		//stu_id若为空
		if(stu_id==null || "".equals(stu_id)
				|| stu_name==null || "".equals(stu_name)
				|| user_id==null || "".equals(user_id)
				|| user_name==null || "".equals(user_name)){
			this.result = "no";
			this.reason = "stu_id、stu_name、user_id、user_name不能为空";
			return "authorStu";
		}
		
		//认证失败
		if(!service.authorStu(stu_id,stu_name,user_id,user_name)){
			this.result = "no";
			this.reason = "认证失败";
			return "authorStu";
		}
		
		//认证成功
		return "authorStu";
	}
	
	
	/**
	 * 批量认证全部教师
	 * @return
	 */
	public String authorTeaAll(){
		//tea_id若为空
		if(user_id==null || "".equals(user_id)
				|| user_name==null || "".equals(user_name)){
			this.result = "no";
			this.reason = "user_id、user_name不能为空";
			return "authorTeaAll";
		}
		
		//认证失败
		if(!service.authorTeaAll(user_id,user_name)){
			this.result = "no";
			this.reason = "认证失败";
			return "authorTeaAll";
		}
		
		//认证成功
		return "authorTeaAll";
	}
	
	/**
	 * 批量认证全部学生
	 * @return
	 */
	public String authorStuAll(){
		if(user_id==null || "".equals(user_id)
				|| user_name==null || "".equals(user_name)){
			this.result = "no";
			this.reason = "user_id、user_name不能为空";
			return "authorStuAll";
		}
		
		//认证失败
		if(!service.authorStuAll(user_id,user_name)){
			this.result = "no";
			this.reason = "认证失败";
			return "authorStuAll";
		}
		
		//认证成功
		return "authorStuAll";
	}
	
	/**
	 * 搜索教师信息
	 * @return
	 */
	public String searchTea(){
		if((!"0".equals(type) && !"1".equals(type) && !"2".equals(type))
				|| (!"0".equals(state) && !"1".equals(state) && !"2".equals(state) && !"3".equals(state))){
			this.result = "no";
			this.reason = "type或state不能为空，且type只能为012，state只能是0123";
			return "searchTea";
		}
		
		//搜索教师信息
		teaList = service.searchTea(type,state,tea_name);
		
		//若搜索发生异常
		if(teaList==null){
			this.result = "no";
			this.reason = "搜索失败";
			return "searchTea";
		}
		
		//搜索成功
		return "searchTea";
	}
	
	
	/**
	 * 搜索学生信息
	 * @return
	 */
	public String searchStu(){
		if((!"0".equals(type) && !"1".equals(type) && !"2".equals(type) && !"3".equals(type))
				|| (!"0".equals(state) && !"1".equals(state) && !"2".equals(state) && !"3".equals(state))){
			this.result = "no";
			this.reason = "type和state不能为空，且只能为0123";
			return "searchStu";
		}
		
		//搜索学生信息
		stuList = service.searchStu(type,state,stu_name);
		
		//若搜索发生异常
		if(stuList==null){
			this.result = "no";
			this.reason = "搜索失败";
			return "searchStu";
		}
		
		//搜索成功
		return "searchStu";
	}
	
	
	/**
	 * 修改教师信息
	 * @return
	 */
	public String updateTea(){
		//传入空参的判断
		if(tea_id==null || "".equals(tea_id) || tea_name==null || "".equals(tea_name)
			||(!"0".equals(type) && !"1".equals(type))
				|| (!"0".equals(state) && !"1".equals(state) && !"2".equals(state))
				|| user_id==null || "".equals(user_id)
				|| user_name==null || "".equals(user_name)){
			this.result = "no";
			this.reason = "tea_id、tea_name、state、type、user_id,user_name不能为空，且state只能是012，type只能是01";
			return "updateTea";
		}
		
		//开始修改-发生错误
		if(!service.updateTea(tea_id,tea_name,type,state,user_id,user_name)){
			this.result = "no";
			this.reason = "修改教师账户出现异常";
			return "updateTea";
		}
		
		//开始修改－成功
		return "updateTea";
	}
	
	
	/**
	 * 修改学生信息
	 * @return
	 */
	public String updateStu(){
		//传入空参的判断
		if(stu_id==null || "".equals(stu_id) || stu_name==null || "".equals(stu_name)
				||(!"0".equals(type) && !"1".equals(type) && !"2".equals(type))
				|| (!"0".equals(state) && !"1".equals(state) && !"2".equals(state))
				|| user_id==null || "".equals(user_id)
				|| user_name==null || "".equals(user_name)){
			this.result = "no";
			this.reason = "stu_id、stu_name、state、type、user_id、user_name不能为空，且state只能是012，type只能是012";
			return "updateStu";
		}
		
		//开始修改-发生错误
		if(!service.updateStu(stu_id,stu_name,type,state,user_id,user_name)){
			this.result = "no";
			this.reason = "修改学生账户出现异常";
			return "updateStu";
		}
		
		//开始修改－成功
		return "updateStu";
	}
	
	
	/**
	 * 认证多个教师账户
	 */
	public String authorSomeTea(){
		//判断tea_id、tea_name、user_id、user_name是否为空
		if(tea_id==null || "".equals(tea_id) || tea_name==null || "".equals(tea_name)
				|| user_id==null || "".equals(user_id)
				|| user_name==null || "".equals(user_name)){
			this.result = "no";
			this.reason = "tea_id、tea_name、user_id、user_name不能为空！";
			return "authorSomeTea";
		}
		
		//判断tea_id、tea_name中是否有.
		if(!(tea_id.contains(".")||tea_name.contains("."))){
			this.result = "no";
			this.reason = "tea_id、tea_name中必须包含用.连接的多个id或name！";
			return "authorSomeTea";
		}
		
		//获取多个教师账户id-name集合
		Map<String,String> map = service.splitIdsAndNames(this.tea_id,this.tea_name);
		if(map==null){
			this.result = "no";
			this.reason = "tea_id和tea_name数量不匹配！";
			return "authorSomeTea";
		}
		
		//认证这些账户
		boolean result = service.authorSomeTea(map, user_id, user_name);
		if(!result){
			this.result = "no";
			this.reason = "服务器发生异常，请重试！";
			return "authorSomeTea";
		}else{
			this.result = "yes";
			this.reason = "认证成功";
			return "authorSomeTea";
		}
	}
	
	
	
	/**
	 * 认证多个学生账户
	 */
	public String authorSomeStu(){
		//判断stu_id、stu_name、user_id、user_name是否为空
		if(stu_id==null || "".equals(stu_id) || stu_name==null || "".equals(stu_name)
				|| user_id==null || "".equals(user_id)
				|| user_name==null || "".equals(user_name)){
			this.result = "no";
			this.reason = "stu_id、stu_name、user_id、user_name不能为空！";
			return "authorSomeStu";
		}
		
		//判断stu_id、stu_name中是否有.
		if(!(stu_id.contains(".")||stu_name.contains("."))){
			this.result = "no";
			this.reason = "stu_id、stu_name中必须包含用.连接的多个id或name！";
			return "authorSomeStu";
		}
		
		//获取多个学生账户id-name集合
		Map<String,String> map = service.splitIdsAndNames(this.stu_id,this.stu_name);
		if(map==null){
			this.result = "no";
			this.reason = "tea_id和tea_name数量不匹配！";
			return "authorSomeStu";
		}
		
		//认证这些账户
		boolean result = service.authorSomeStu(map, user_id, user_name);
		if(!result){
			this.result = "no";
			this.reason = "服务器发生异常，请重试！";
			return "authorSomeStu";
		}else{
			this.result = "yes";
			this.reason = "认证成功";
			return "authorSomeStu";
		}
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

	public List<TeacherEntity> getTeaList() {
		return teaList;
	}

	public void setTeaList(List<TeacherEntity> teaList) {
		this.teaList = teaList;
	}

	public List<StudentEntity> getStuList() {
		return stuList;
	}

	public void setStuList(List<StudentEntity> stuList) {
		this.stuList = stuList;
	}

	public String getTea_id() {
		return tea_id;
	}

	public void setTea_id(String tea_id) {
		this.tea_id = tea_id;
	}

	public String getStu_id() {
		return stu_id;
	}

	public void setStu_id(String stu_id) {
		this.stu_id = stu_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTea_name() {
		return tea_name;
	}

	public void setTea_name(String tea_name) {
		this.tea_name = tea_name;
	}

	public String getStu_name() {
		return stu_name;
	}

	public void setStu_name(String stu_name) {
		this.stu_name = stu_name;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}


	
	
}
