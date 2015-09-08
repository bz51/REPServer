package com.rep.stu.action;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import com.rep.core.Tools;
import com.rep.stu.entity.StudentEntity;
import com.rep.stu.service.LoginService;
import com.rep.tea.service.LogService;

public class LoginAction extends ActionSupport  implements SessionAware {
	private String username;
	private String password;
	private String stu_id;
	private String stu_name;
	private String result = "yes";
	private String reason;
	private String access_token;
	private String phone;
	private String authcode;
	private LoginService service = new LoginService();
	private Map<String,Object> session;
	//验证码图片
	private ByteArrayInputStream imageStream;
	private String checkCode;
	
	/**
	 * 判断用户名username是否已经被注册
	 */
	public String isLogin(){
		if(username==null || "".equals(username)){
			this.result = "no";
			this.reason = "用户名不得为空！";
			return "signin";
		}
		
		//判断用户名是否被注册
		if(!service.isLogin(username)){
			this.result = "no";
			this.reason = "用户名已经被注册！";
			return "signin";
		}
		
		return "isLogin";
	}
	
	/**
	 * 登陆
	 */
	public String signin(){
		if(username==null || "".equals(username) || password==null || "".equals(password)){
			this.result = "no";
			this.reason = "用户名或密码不得为空！";
			return "signin";
		}
		
		
		//如果该IP访问超过了5次，则需要输入验证码
		////////////////////////
		String ip = ServletActionContext.getRequest().getRemoteAddr();
		System.out.println("IP:"+ip);
		int count = 0;
		if(session.get(ip)==null)
			++count;
		else
			count = (int) session.get(ip) + 1;
		session.put(ip, count);
		
		System.out.println("当前IP:"+ip+"登录次数："+session.get(ip));
		//若该IP已经失败登录了5次，则判断提交的验证码是否正确
		if(count>5){
			//判断验证码是否为空
			if(checkCode==null || "".equals(checkCode)){
				this.result = "no";
				this.reason = "checkCode不能为空";
				return "signin";
			}
			//判断验证码是否正确
			System.out.println("用户输入的code："+this.checkCode+",服务器中的code："+session.get(ip+"checkCode")+"");
			if(!this.checkCode.equals(session.get(ip+"checkCode")+"")){
				this.result = "no";
				this.reason = "验证码不正确";
				return "signin";
			}
		}
		////////////////////////
		
		
		//用户鉴权
		StudentEntity teaEntity = new StudentEntity();
		teaEntity.setUsername(username);
		teaEntity.setPassword(password);
		
		teaEntity = service.signin(teaEntity);
		
		//登陆失败
		if("no".equals(teaEntity.getResult())){
			this.result = "no";
			this.reason = teaEntity.getReason();
		}else{//登陆成功
			//返回数据
			this.stu_id = teaEntity.getId()+"";
			this.stu_name = teaEntity.getUsername();
			//添加登陆日志
			LogService service = new LogService();
			service.addStuLoginLog(teaEntity.getId(),teaEntity.getUsername(),teaEntity.getType());
			//去掉session中该IP登录失败的次数 和 验证码
			session.remove(ip+"checkCode");
			session.remove(ip);
		}
		
		return "signin";
	}
	
	/**
	 * 注册
	 */
	public String login(){
		if(username==null || "".equals(username) || password==null || "".equals(password)	|| access_token==null || "".equals(access_token)
				|| authcode==null || "".equals(authcode)
				|| phone==null || "".equals(phone)){
			this.result = "no";
			this.reason = "用户名、密码、access_token、authcode、phone不得为空！";
			return "login";
		}
		

		//判断验证码是否正确
		System.out.println("access_token:"+access_token);
		System.out.println(session.size());
		Set<String> sets = session.keySet();
		for(String str : sets){
			System.out.println("key:"+str);
		}
		Collection<Object> values = session.values();
		for(Object o : values){
			System.out.println("value:"+o+"");
		}
		String code = (String) session.get(access_token);
		System.out.println("服务器中的code："+code);
		if(!authcode.equals(code)){
			this.result = "no";
			this.reason = "验证码不正确";
			return "login";
		}
		//验证码正确，把验证码从session中remove掉
		session.remove(access_token);

		
		//进行注册
		StudentEntity teaEntity = new StudentEntity();
		teaEntity.setUsername(username);
		teaEntity.setPassword(password);
		teaEntity.setPhone(phone);
		
		teaEntity = service.login(teaEntity);
		
		if("no".equals(teaEntity.getResult())){
			this.result = "no";
			this.reason = teaEntity.getReason();
		}else{
			this.stu_id = teaEntity.getId()+"";
			this.stu_name = teaEntity.getUsername();
		}
		
		return "login";
	}
	
	
	
	/**
	 * 获取验证码，并向手机发送
	 */
	public String verfiyCode(){
		if(authcode==null || "".equals(authcode) || access_token==null || "".equals(access_token)){
			this.result = "no";
			this.reason = "authcode、access_token不能为空";
			return "verfiyCode";
		}
		
		
		/**----------*/
		Set<String> sets = session.keySet();
		for(String set : sets)
			System.out.println("key:"+set);
		
		Collection<Object> values = session.values();
		for(Object o : values)
			System.out.println("value:"+o);
		/**----------*/
		
		
		//根据tea_id获取session中的code
		String code = (String) session.get(access_token);
		//判断code与authcode是否一致
		if(!authcode.equals(code)){
			this.result = "no";
			this.reason = "验证码不正确";
			return "verfiyCode";
		}
		
		session.remove(access_token);
		this.result = "yes";
		this.reason = "验证成功";
		return "verfiyCode";

	}
	
	
	/**
	 * 修改密码
	 */
	public String modifyPass(){
		if(password==null || "".equals(password) || phone==null || "".equals(phone)){
			this.result = "no";
			this.reason = "password、phone不能为空";
			return "modifyPass";
		}
		
		//修改密码
		boolean result = service.modifyPass(password,phone);
		if(!result){
			this.result = "no";
			this.reason = "服务器发生异常，请重试!";
			return "modifyPass";
		}
		return "modifyPass";
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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
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


	public Map<String, Object> getSession() {
		return session;
	}
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAuthcode() {
		return authcode;
	}

	public void setAuthcode(String authcode) {
		this.authcode = authcode;
	}

	public ByteArrayInputStream getImageStream() {
		return imageStream;
	}

	public void setImageStream(ByteArrayInputStream imageStream) {
		this.imageStream = imageStream;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}



	
	
	
}
